package module_shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.ciacho.aishengdemo.R;

import module_shop.Dialog.MyDialog;
import module_shop.app.ApplyActivity;


/**
 * Created by Ciacho on 2018/4/30.
 */

public class ColumnRecyclerAdapter extends DelegateAdapter.Adapter {
    public Context context;
    private LayoutHelper helper;
    private LayoutInflater inflater;
    private  int[] images={R.drawable.ic_shop_car,R.drawable.ic_shop_qd,R.drawable.ic_shop_sq};
    private String[] titles={"购物车","签到","贫困申请"};
    public ColumnRecyclerAdapter(Context context, LayoutHelper helper){
        this.inflater = LayoutInflater.from(context);
        this.helper = helper;
        this.context=context;
        this.images=images;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColumnRecyclerAdapter.MyViewHolder(inflater.inflate(R.layout.layout_item_shop2,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ColumnRecyclerAdapter.MyViewHolder myViewHolder=(ColumnRecyclerAdapter.MyViewHolder)holder;
        myViewHolder.name.setText(titles[position]);
        myViewHolder.imageView.setImageResource(images[position]);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        MyDialog myDialog=new MyDialog(context);
                        myDialog.show();
                        break;
                    case  2:
                        Intent intent=new Intent(context, ApplyActivity.class);
                        context.startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView imageView;
        public TextView price;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.item_name);
            imageView=itemView.findViewById(R.id.item_image);
        }
    }
}
