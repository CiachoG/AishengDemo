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
import com.bumptech.glide.Glide;
import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;

import module_shop.app.GoodDeatail;
import module_shop.entity.Goods;

import java.util.List;

/**
 * Created by Ciacho on 2018/4/30.
 */

public class OneplusAnyRecyclerAdapter extends DelegateAdapter.Adapter{
    public Context context;
    private LayoutHelper helper;
    private LayoutInflater inflater;
    List<Goods> goodsArrayList;

    public OneplusAnyRecyclerAdapter(Context context, LayoutHelper helper, List<Goods> goodsArrayList){
        this.inflater = LayoutInflater.from(context);
        this.helper = helper;
        this.context=context;
        this.goodsArrayList=goodsArrayList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.layout_item_shop1,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        myViewHolder.name.setText(goodsArrayList.get(position+2).getGoods_name());
        Glide.with(context).load(Quantity.SERVER_URL+goodsArrayList.get(position+2).getGoods_image()).into(myViewHolder.imageView);
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, GoodDeatail.class);
                context.startActivity(intent);
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
