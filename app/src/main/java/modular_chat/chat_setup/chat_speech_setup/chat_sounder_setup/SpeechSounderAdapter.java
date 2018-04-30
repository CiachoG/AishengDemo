package modular_chat.chat_setup.chat_speech_setup.chat_sounder_setup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ciacho.aishengdemo.R;

import java.util.List;

public class SpeechSounderAdapter extends ArrayAdapter<Sounder>{
    private Context context;
    private List<Sounder>dataList;

    public SpeechSounderAdapter(Context context, List<Sounder> dataList) {
        super(context, android.R.layout.simple_list_item_1, dataList);
        this.dataList=dataList;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowview=null;
        ViewHolder vh=null;
        if(convertView==null){
            rowview= LayoutInflater.from(context).inflate(R.layout.layout_speech_sounder_row,null,false);
            vh=new ViewHolder();
            vh.text_name= (TextView) rowview.findViewById(R.id.text_sounderName);
            vh.text_lang= (TextView) rowview.findViewById(R.id.text_language);
            vh.text_attr= (TextView) rowview.findViewById(R.id.text_attr);
            rowview.setTag(vh);
        }else{
            rowview=convertView;
            vh= (ViewHolder) rowview.getTag();
        }

        vh.text_name.setText(dataList.get(position).getName());
        vh.text_lang.setText(dataList.get(position).getLanguage());
        vh.text_attr.setText(dataList.get(position).getAttr());
        return rowview;
    }

    class ViewHolder{
        TextView text_name,text_lang,text_attr;
    }
}
