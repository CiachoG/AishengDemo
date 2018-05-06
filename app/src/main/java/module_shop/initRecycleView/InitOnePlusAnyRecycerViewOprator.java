package module_shop.initRecycleView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.example.ciacho.aishengdemo.bean.Goods;

import java.util.List;

import module_shop.adapter.OneplusAnyRecyclerAdapter;

/**
 * Created by Ciacho on 2018/5/3.
 */

public class InitOnePlusAnyRecycerViewOprator {
    private RecyclerView recyclerview;
    private Context context;
    private DelegateAdapter adapter;
    private List<Goods> goodsList;
    private OneplusAnyRecyclerAdapter oneplusAnyRecyclerAdapter;

    public InitOnePlusAnyRecycerViewOprator(RecyclerView recyclerview, Context context, List<Goods> goodsList) {
        this.recyclerview = recyclerview;
        this.context = context;
        this.goodsList = goodsList;
    }

    public void init(){
        VirtualLayoutManager manager1 = new VirtualLayoutManager(context);
        recyclerview.setLayoutManager(manager1);
        adapter =new DelegateAdapter(manager1, true);
        adapter.addAdapter(mGetAdapter(context));
        recyclerview.setAdapter(adapter);
    }
    public DelegateAdapter.Adapter mGetAdapter(Context context){
        OnePlusNLayoutHelper onePlusNLayoutHelper = new OnePlusNLayoutHelper(3);
        onePlusNLayoutHelper.setItemCount(2);// 设置布局里Item个数
        onePlusNLayoutHelper.setPadding(20, 50, 20, 0);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        onePlusNLayoutHelper.setMargin(20, 0, 20, 0);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        onePlusNLayoutHelper.setBgColor(Color.WHITE);// 设置背景颜色
        onePlusNLayoutHelper.setAspectRatio(1.5f);// 设置设置布局内每行布局的宽与高的比
        oneplusAnyRecyclerAdapter =new OneplusAnyRecyclerAdapter(context,onePlusNLayoutHelper,goodsList);
        return oneplusAnyRecyclerAdapter;
    }
    public void updateList(List<Goods> goodsList)
    {
        this.goodsList=goodsList;
        init();
    }
}
