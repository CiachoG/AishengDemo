package chat_modular.chat_setup.chat_quick_response_setup;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ciacho.aishengdemo.R;

import java.util.List;

import chat_modular.chat_main.QuickResponser;

public class QuickResponseAdapter extends ArrayAdapter<QuickResponser.Unit> {
    private Context context;
    private List<QuickResponser.Unit>dataList;

    public QuickResponseAdapter(Context context, List<QuickResponser.Unit> dataList) {
        super(context, android.R.layout.simple_list_item_1, dataList);
        this.context=context;
        this.dataList=dataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView=null;
        ViewHolder vh=null;
        if(convertView==null){
            rowView= LayoutInflater.from(context).inflate(R.layout.layout_quick_list_row,null,false);
            vh=new ViewHolder();
            vh.text_code= (TextView) rowView.findViewById(R.id.text_code);
            vh.text_content= (TextView) rowView.findViewById(R.id.text_content);
            rowView.setTag(vh);
        }else{
            rowView=convertView;
            vh= (ViewHolder) rowView.getTag();
        }

        vh.text_code.setText(dataList.get(position).getCode());
        vh.text_content.setText(dataList.get(position).getContent());

        return rowView;
    }


    private class ViewHolder{
        TextView text_code;
        TextView text_content;
    }
}
