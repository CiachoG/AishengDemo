package chat_modular.chat_main_list;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.cpiz.android.bubbleview.BubbleTextView;
import com.example.ciacho.aishengdemo.R;

import java.util.List;

public class ChatUIListAdapter extends ArrayAdapter<ChatListRow>{
    private Context context;
    private List<ChatListRow>dataList;

    public ChatUIListAdapter(Context context, List<ChatListRow> dataList) {
        super(context, android.R.layout.simple_list_item_1, dataList);
        this.context=context;
        this.dataList=dataList;
    }


    private View rowview=null;
    private ViewHolder vh=null;
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type=dataList.get(position).getChatType();  //最新类型
        if(convertView==null){
            bindView(position);
        }else{
            rowview=convertView;
            vh= (ViewHolder) rowview.getTag();
            if(vh.type!=type)   bindView(position);
        }
        vh.text_chatContent.setText(dataList.get(position).getChatContent());
        return rowview;
    }

    private void bindView(int position){
        int type=dataList.get(position).getChatType();
        switch (type){
            case ChatListRow.TYPE_SLEF:
                rowview= LayoutInflater.from(context).inflate(R.layout.layout_chat_row_self,null,false);
                break;
            case ChatListRow.TYPE_OTHER:
                rowview= LayoutInflater.from(context).inflate(R.layout.layout_chat_row_other,null,false);
                break;
        }
        vh=new ViewHolder();
        vh.text_chatContent= (BubbleTextView) rowview.findViewById(R.id.text_chatContent);
        vh.type=type;
        rowview.setTag(vh);
    }


    class ViewHolder{
        BubbleTextView text_chatContent;
        int type;
    }
}
