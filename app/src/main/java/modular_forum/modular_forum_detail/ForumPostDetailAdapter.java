package modular_forum.modular_forum_detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.ciacho.aishengdemo.R;
import java.util.List;

import modular_forum.ForumDataTimeTool;

public class ForumPostDetailAdapter extends ArrayAdapter<ForumPostDetailListRow> {
    private Context context;
    private List<ForumPostDetailListRow>dataList;
    private View topView;

    public ForumPostDetailAdapter(@NonNull Context context, @NonNull List<ForumPostDetailListRow> dataList,View topView) {
        super(context, android.R.layout.simple_list_item_1, dataList);
        this.context=context;
        this.dataList=dataList;
        this.topView=topView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(position==0)
            return topView;

        View rowview=null;
        ViewHolder vh=null;
        if(convertView==null){
            rowview=LayoutInflater.from(context).inflate(R.layout.layout_frag_forum_detail_row,null,false);
            vh=new ViewHolder();
            vh.text_userName=rowview.findViewById(R.id.text_userName);
            vh.text_commDate=rowview.findViewById(R.id.text_commDate);
            vh.text_commText=rowview.findViewById(R.id.text_commText);
            vh.text_commFloor=rowview.findViewById(R.id.text_commFloor);
            rowview.setTag(vh);
        }else{
            rowview=convertView;
            vh= (ViewHolder) rowview.getTag();
            if(vh==null){
                rowview=LayoutInflater.from(context).inflate(R.layout.layout_frag_forum_detail_row,null,false);
                vh=new ViewHolder();
                vh.text_userName=rowview.findViewById(R.id.text_userName);
                vh.text_commDate=rowview.findViewById(R.id.text_commDate);
                vh.text_commText=rowview.findViewById(R.id.text_commText);
                vh.text_commFloor=rowview.findViewById(R.id.text_commFloor);
                rowview.setTag(vh);
            }
        }

        vh.text_userName.setText(dataList.get(position).getUserName());
        //vh.text_commDate.setText("发表于 "+ ForumDataTimeTool.timeChangeOver(dataList.get(position).getRowDate()));
        //vh.text_commText.setText(dataList.get(position).getContentText());
        vh.text_commFloor.setText("#"+(position+1));
        return rowview;
    }

    class ViewHolder{
        TextView text_userName,text_commDate,text_commText,text_commFloor;
    }
}
