package module_shop.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.FloatLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;
import com.example.ciacho.aishengdemo.bean.Goods;
import com.example.ciacho.aishengdemo.entity.GlideImageLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import HttpConnect.GetGoodsInfoConnect;
import module_shop.adapter.ColumnRecyclerAdapter;
import module_shop.adapter.OnplusAnyRecyclerAdapter;

@SuppressLint("ValidFragment")
public class ShopFragment extends Fragment {

    private Banner banner;
    private List<String> images;
    private List<String> titles;
    private Handler handler;
    private  GetGoodsInfoConnect getGoodsInfoConnect;
    private List<Goods> goodsList;
    private   RecyclerView recyclerview1;
    private   RecyclerView recyclerview2;
    private OnplusAnyRecyclerAdapter onplusAnyRecyclerAdapter;
    private ColumnRecyclerAdapter columnRecyclerAdapter;
    private DelegateAdapter adapter2;
    private DelegateAdapter adapter1;
    public static ShopFragment getInstance() {
        ShopFragment sf = new ShopFragment();
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop_layout, null);
        banner=v.findViewById(R.id.banner);
        recyclerview1=(RecyclerView)v.findViewById(R.id.shop_recycleview1);
        recyclerview2=(RecyclerView)v.findViewById(R.id.shop_recycleview2);
        InitGoods();

        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.LEFT);
        banner.setViewPagerIsScroll(true);
        //设置图片集合

        handler =new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                goodsList=new ArrayList<>();
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .disableHtmlEscaping()
                        .create();
                JsonObject jsonObject = new JsonParser().parse(getGoodsInfoConnect.getLoginFlag()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("FileData");
                for (JsonElement jsonElement : jsonArray) {
                    Goods goods = gson.fromJson(jsonElement, new TypeToken<Goods>() {}.getType());
                    titles.add(goods.getGoods_name());
                    images.add(Quantity.SERVER_URL+goods.getGoods_image());
                    goodsList.add(goods);
                }
                banner.setImages(images);
                banner.setBannerTitles(titles);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
                initRecycerview();
                return true;
            }
        });
        return v;
    }

    private void initRecycerview() {

        VirtualLayoutManager manager1 = new VirtualLayoutManager(getContext());
        recyclerview1.setLayoutManager(manager1);
        adapter1 =new DelegateAdapter(manager1, true);
        adapter1.addAdapter(init1(getContext()));
        recyclerview1.setAdapter(adapter1);



        VirtualLayoutManager manager2 = new VirtualLayoutManager(getContext());
        recyclerview2.setLayoutManager(manager2);
        adapter2 =new DelegateAdapter(manager2, true);
        adapter2.addAdapter(init2(getContext()));
        recyclerview2.setAdapter(adapter2);
    }

    private DelegateAdapter.Adapter init1(Context context) {
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

    public OnplusAnyRecyclerAdapter init2(Context context){
        OnePlusNLayoutHelper onePlusNLayoutHelper = new OnePlusNLayoutHelper(3);
        onePlusNLayoutHelper.setItemCount(2);// 设置布局里Item个数
        onePlusNLayoutHelper.setPadding(20, 50, 20, 0);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        onePlusNLayoutHelper.setMargin(20, 0, 20, 0);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        onePlusNLayoutHelper.setBgColor(Color.WHITE);// 设置背景颜色
        onePlusNLayoutHelper.setAspectRatio(1.5f);// 设置设置布局内每行布局的宽与高的比
        onplusAnyRecyclerAdapter =new OnplusAnyRecyclerAdapter(context,onePlusNLayoutHelper,goodsList);
        return onplusAnyRecyclerAdapter;
    }

    private void InitGoods() {
        images=new ArrayList<>();
        titles=new ArrayList<>();
        getGoodsInfoConnect=new GetGoodsInfoConnect(handler);
        getGoodsInfoConnect.SendByHttpClient();
    }
}