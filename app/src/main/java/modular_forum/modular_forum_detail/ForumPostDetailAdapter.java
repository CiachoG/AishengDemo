package modular_forum.modular_forum_detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;
import java.text.ParseException;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import modular_forum.ForumDataLoader;
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
            vh.imgView_headport=rowview.findViewById(R.id.imgView_headport);
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
                vh.imgView_headport=rowview.findViewById(R.id.imgView_headport);
                rowview.setTag(vh);
            }
        }

        vh.text_userName.setText(dataList.get(position).getUserName());
        try {
            vh.text_commDate.setText("发表于 "+ ForumDataTimeTool.timeChangeOver(ForumDataLoader.DATE_FORMAT.parse(dataList.get(position).getCommDate())));
        } catch (ParseException e) {
            vh.text_commDate.setText(e.toString());
        }
        vh.text_commText.setText(dataList.get(position).getCommText());
        vh.text_commFloor.setText("#"+(position+1));
        Glide.with(context)
                .load(Quantity.SERVER_URL+dataList.get(position).getUserHeaderUrl())
                .animate(R.anim.anim_alpha)
                .placeholder(R.drawable.img_headport_default)
                .error(R.drawable.img_headport_default)     //加载失败后放置的默认图像
                .diskCacheStrategy(DiskCacheStrategy.RESULT)    //只缓存最终结果图像
                .into(vh.imgView_headport);
        return rowview;
    }

    class ViewHolder{
        TextView text_userName,text_commDate,text_commText,text_commFloor;
        CircleImageView imgView_headport;
    }
}
