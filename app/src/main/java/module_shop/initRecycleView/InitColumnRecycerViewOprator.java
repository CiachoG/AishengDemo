package module_shop.initRecycleView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;

import module_shop.adapter.ColumnRecyclerAdapter;

/**
 * Created by Ciacho on 2018/5/3.
 */

public class InitColumnRecycerViewOprator {
    private RecyclerView recyclerview;
    private Context context;
    private DelegateAdapter adapter;
    private ColumnRecyclerAdapter columnRecyclerAdapter;
    public InitColumnRecycerViewOprator(RecyclerView recyclerview,Context context) {
        this.recyclerview = recyclerview;
        this.context=context;
    }

    public void init()
    {
        VirtualLayoutManager manager1 = new VirtualLayoutManager(context);
        recyclerview.setLayoutManager(manager1);
        adapter =new DelegateAdapter(manager1, true);
        adapter.addAdapter(mGetAdapter(context));
        recyclerview.setAdapter(adapter);

    }
    private DelegateAdapter.Adapter mGetAdapter(Context context) {
        ColumnLayoutHelper columnLayoutHelper = new ColumnLayoutHelper();
        // 创建对象

        // 公共属性
        columnLayoutHelper.setItemCount(3);// 设置布局里Item个数
        columnLayoutHelper.setPadding(20, 20, 20, 60);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        columnLayoutHelper.setMargin(20, 30, 20, 0);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        columnLayoutHelper.setBgColor(Color.WHITE);// 设置背景颜色
        columnLayoutHelper.setAspectRatio(6);// 设置设置布局内每行布局的宽与高的比

        // columnLayoutHelper特有属性
        columnLayoutHelper.setWeights(new float[]{30, 40, 30});// 设置该行每个Item占该行总宽度的比例
        columnRecyclerAdapter=new ColumnRecyclerAdapter(context,columnLayoutHelper);
        return columnRecyclerAdapter;
    }
}
