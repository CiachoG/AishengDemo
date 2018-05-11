package modular_chat.chat_text_watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import modular_chat.chat_main.ChatActivity;
import modular_chat.chat_main.ChatInputMethodManager;

public class SyllableIMTextWatcher implements TextWatcher {
    private ChatActivity context;

    private EditText edit_targetText;
    private char solvedCNChar;    //最新解析成中文的字符数据
    private SpellDataReader dataReader;

    private Runnable run_solveOneChar=null;      //解析完一个汉字的回调函数
    private Runnable run_delEmptyEdit=null;      //当编辑文本框为空，依然输入删除字符的命令时的回调函数
    private Runnable run_finishInput=null;

    public SyllableIMTextWatcher(ChatActivity context, EditText edit_targetText){
        this.edit_targetText=edit_targetText;
        this.context=context;

        dataReader=new SpellDataReader(context);
        try {
            dataReader.work();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after){
        context.getChatUIListManager().scrollToLastPosition();
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        //当用户输入完
        //在该函数内不能处理edit_targetText.setText()操作
        //try{
            String result=inputTextSolving(s.toString());
            edit_targetText.removeTextChangedListener(this);
            edit_targetText.setText(result);
            edit_targetText.setSelection(result.length());

            edit_targetText.addTextChangedListener(this);
        //}catch (Exception e){
            //Log.e("错误信息：",e.toString());
        //}
    }

    //对于编辑文本框里所有字符串，即从inputData中解析出一个汉字，并删除相应前缀
    private String inputTextSolving(String inputData){
        int strLength=inputData.length();

        if(strLength==0)   return "";

        //当目标编辑文本框为空，并按下删除键,则尝试删除已经解析好的汉字列表
        if(strLength==1&&inputData.charAt(0)==
                ChatInputMethodManager.SPECIAL_CANCEL){
            if(run_delEmptyEdit!=null)  run_delEmptyEdit.run();
            return "";
        }

        //刚输入一个删除含义的字符
        if(strLength>=2&&inputData.charAt(inputData.length()-1)
                ==ChatInputMethodManager.SPECIAL_CANCEL){
            //将最后两个字符删掉,删除含义的字符和原来的最后一个字符
            return inputData.substring(0,inputData.length()-2);
        }

        if(inputData.charAt(strLength-1)==ChatInputMethodManager.OK_CHAR){
            if(run_finishInput!=null)   run_finishInput.run();
            return "";
        }

        int type;
        for(int i=0;i<inputData.length();++i){
            if((type=isToneChar(inputData.charAt(i)))>=0){      //是音调字符，表示输入完一个汉字编码
                List<String>dataList=getCutElementList(inputData.substring(0,i));

                StringBuilder builder=new StringBuilder("");
                Map<Integer,String> map=dataReader.getMapPartPhonetic();
                for(int j=0;j<dataList.size();++j){
                    int key=Integer.parseInt(dataList.get(j));     //获取到输入的键值
                    String str=map.get(key);
                    if(str==null){
                        Log.e("错误:","输入了无效音节编码");
                        return inputData.substring(i+1);
                    }

                    builder.append(str);
                }
                String phonetic=builder.toString();     //某个汉字的拼音

                List<SpellExlRow>list=SpellDataReader.list_charRowData;
                SpellExlRow row=new SpellExlRow();
                row.setPhonetic(phonetic);
                int index= Collections.binarySearch(list,row,null);
                if(index>=0){
                    char c=list.get(index).getCharacter(type);
                    if(c=='N'){
                        Log.e("错误:","无法找到对应的拼音与音调");
                        return inputData.substring(i+1);
                    }

                    solvedCNChar=c;    //解析出一个汉字
                    if(run_solveOneChar!=null)  run_solveOneChar.run();
                    solvedCNChar=' ';
                }else{
                    Log.e("错误:","汉字解析异常");
                }
                return inputData.substring(i+1);    //返回解析完后的剩余字符串
            }
        }
        return inputData;   //没有进行解析工作表示尚未输入超过一个字符的编码
    }

    public List<String> getCutElementList(String str){
        List<String>dataList=new ArrayList<>();
        for(int i=0;i<str.length();++i){
            dataList.add(str.substring(i,i+2));
            i++;
        }
        return dataList;
    }

    public void clearEditContent(){
        edit_targetText.removeTextChangedListener(this);
        edit_targetText.setText("");
        edit_targetText.addTextChangedListener(this);
    }

    //判断是否为音调字符
    private int isToneChar(char c){
        for(int i=0;i<ChatInputMethodManager.TONE_CHARS.length;++i)
            if(ChatInputMethodManager.TONE_CHARS[i]==c)
                return i;
        return -1;
    }
    public char getSolvedCNChar(){
        return this.solvedCNChar;
    }

    public Map<Integer,String> getSyllableMapData(){
        return this.dataReader.getMapPartPhonetic();
    }

    public void setSolveOneCharRunnable(Runnable runnable){
        this.run_solveOneChar=runnable;
    }

    public void setDeleteEmptyEditRunnable(Runnable runnable){
        this.run_delEmptyEdit=runnable;
    }

    public void setFinishInputRunnable(Runnable runnable){
        this.run_finishInput=runnable;
    }
}
