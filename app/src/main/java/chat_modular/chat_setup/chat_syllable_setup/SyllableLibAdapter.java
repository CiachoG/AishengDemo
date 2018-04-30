package chat_modular.chat_setup.chat_syllable_setup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ciacho.aishengdemo.R;

import java.util.List;

public class SyllableLibAdapter extends ArrayAdapter<SyllableLibRow> {
    private Context context;
    private List<SyllableLibRow>dataList;

    public SyllableLibAdapter(Context context, List<SyllableLibRow> dataList) {
        super(context, android.R.layout.simple_list_item_1, dataList);
        this.context=context;
        this.dataList=dataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowview=null;
        ViewHolder vh=null;
        if(convertView==null){
            rowview= LayoutInflater.from(context).inflate(R.layout.layout_syll_lib_row,null,false);
            vh=new ViewHolder();
            vh.text_syll_code= (TextView) rowview.findViewById(R.id.text_syll_code);
            vh.text_syll_content= (TextView) rowview.findViewById(R.id.text_syll_content);
            rowview.setTag(vh);
        }else{
            rowview=convertView;
            vh= (ViewHolder) rowview.getTag();
        }

        vh.text_syll_code.setText(dataList.get(position).getCode());
        vh.text_syll_content.setText(dataList.get(position).getContent());

        return rowview;
    }

    private class ViewHolder{
        TextView text_syll_code;
        TextView text_syll_content;
    }
}
