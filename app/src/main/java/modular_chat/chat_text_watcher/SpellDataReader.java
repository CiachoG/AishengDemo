package modular_chat.chat_text_watcher;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import jxl.Sheet;
import jxl.Workbook;
import modular_chat.chat_tools.AssetSDCardUnpacker;

public class SpellDataReader {
	public static List<SpellExlRow> list_charRowData;	//拼音加音调对象列表
	public static List<String>list_phonetic;		//拼音列表
	private AtomicBoolean isAvaliable;
	private Map<Integer,String>map_code_partPhonetic;

    private String SpellData_savePath;
    private Context context;
	public SpellDataReader(Context context){
        list_charRowData=null;
		list_phonetic=null;
		isAvaliable=new AtomicBoolean();
		isAvaliable.set(false);

        this.context=context;
        SpellData_savePath=context.getFilesDir().getAbsolutePath()+"/SpellDataSet.xls";
	}

	public void work()throws Exception{
		if(isAvaliable.get())	return;

		AssetSDCardUnpacker.work(context,SpellData_savePath,"SpellDataSet.xls");

		File exlFile=new File(SpellData_savePath);
		if(!exlFile.exists()||!exlFile.isFile())
			return;
				
		try{
			Workbook book=Workbook.getWorkbook(exlFile);
			
			Sheet sheet1=book.getSheet(0);
			int rows=sheet1.getRows();
            list_charRowData=new ArrayList<>();
			list_phonetic=new ArrayList<>();
			for(int i=0;i<rows;++i){
				String phonetic=sheet1.getCell(0, i).getContents();
				char a=sheet1.getCell(1, i).getContents().charAt(0);
				char b=sheet1.getCell(2, i).getContents().charAt(0);
				char c=sheet1.getCell(3, i).getContents().charAt(0);
				char d=sheet1.getCell(4, i).getContents().charAt(0);
				SpellExlRow row=new SpellExlRow(phonetic,a,b,c,d);
                list_charRowData.add(row);
				list_phonetic.add(phonetic);
			}
			Collections.sort(list_phonetic);
			Collections.sort(list_charRowData);

			map_code_partPhonetic=new HashMap<Integer, String>();
			Sheet sheet2=book.getSheet(1);
			rows=sheet2.getRows();
			for(int i=0;i<rows;++i){
				String partPhonetic=sheet2.getCell(0,i).getContents();
				String mapValue=sheet2.getCell(1,i).getContents();
				map_code_partPhonetic.put(Integer.parseInt(mapValue),partPhonetic);
			}
			
			isAvaliable.set(true);
			book.close();
		}catch(Exception e){
			throw e;
		}
	}

	public Map<Integer,String> getMapPartPhonetic(){
		return map_code_partPhonetic;
	}

}
