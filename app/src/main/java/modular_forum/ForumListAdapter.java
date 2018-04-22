package modular_forum;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.ciacho.aishengdemo.R;
import java.util.List;

public class ForumListAdapter extends ArrayAdapter<ForumListRow> {
    private Context context;
    private List<ForumListRow>dataList;

    public ForumListAdapter(Context context,List<ForumListRow> dataList) {
        super(context, android.R.layout.simple_list_item_1, dataList);
        this.context=context;
        this.dataList=dataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowview=null;
        ViewHolder vh=null;
        if(convertView==null){
            rowview= LayoutInflater.from(context).inflate(R.layout.layout_frag_forum_row,null,false);
            vh=new ViewHolder();
            vh.text_title= (TextView) rowview.findViewById(R.id.text_title);
            vh.text_userName= (TextView) rowview.findViewById(R.id.text_userName);
            vh.text_commentNum= (TextView) rowview.findViewById(R.id.text_commentNum);
            vh.text_postDate= (TextView) rowview.findViewById(R.id.text_postDate);
            rowview.setTag(vh);
        }else{
            rowview=convertView;
            vh= (ViewHolder) rowview.getTag();
        }

        vh.text_title.setText(dataList.get(position).getPostTitle());
        vh.text_userName.setText(dataList.get(position).getUserName());
        vh.text_commentNum.setText(dataList.get(position).getCommentNum());
        vh.text_postDate.setText(dataList.get(position).getPostDate());
        return rowview;
    }

    class ViewHolder{
        TextView text_title,text_userName,text_postDate,text_commentNum;
    }
}
