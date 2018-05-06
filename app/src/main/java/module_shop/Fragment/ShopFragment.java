package module_shop.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;

import module_shop.entity.Goods;
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

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import HttpConnect.GetGoodsInfoConnect;
import module_shop.initRecycleView.InitColumnRecycerViewOprator;
import module_shop.initRecycleView.InitOnePlusAnyRecycerViewOprator;

@SuppressLint("ValidFragment")
public class ShopFragment extends Fragment {

    private Banner banner;
    private Handler handler;
    private  GetGoodsInfoConnect getGoodsInfoConnect;
    private List<Goods> goodsList;
    private List<String> images=new ArrayList<>();
    private List<String> titles=new ArrayList<>();
    private   RecyclerView recyclerview1;
    private   RecyclerView recyclerview2;
    private InitOnePlusAnyRecycerViewOprator initOnePlusAnyRecycerViewOprator;
    private InitColumnRecycerViewOprator initColumnRecycerViewOprator;
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
        goodsList=new ArrayList<>();
        initColumnRecycerViewOprator=new InitColumnRecycerViewOprator(recyclerview1,getContext());
        initColumnRecycerViewOprator.init();
        initOnePlusAnyRecycerViewOprator=new InitOnePlusAnyRecycerViewOprator(recyclerview2,getContext(),goodsList);
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.LEFT);
        banner.setViewPagerIsScroll(true);
        InitGoods();

        //设置图片集合
        handler =new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .disableHtmlEscaping()
                        .create();
                JsonObject jsonObject = new JsonParser().parse(getGoodsInfoConnect.getLoginFlag()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("FileData");
                for (JsonElement jsonElement : jsonArray) {
                    Goods goods = gson.fromJson(jsonElement, new TypeToken<Goods>() {}.getType());
                    goods.save();
                    goodsList.add(goods);
                    images.add(Quantity.SERVER_URL+goods.getGoods_image());
                    titles.add(goods.getGoods_name());
                }
                banner.setImages(images);
                banner.setBannerTitles(titles);
                banner.start();
                initOnePlusAnyRecycerViewOprator.updateList(goodsList);
                return true;
            }
        });
        return v;
    }


    private void InitGoods() {
        goodsList= DataSupport.findAll(Goods.class);
       if(goodsList.size()>0){
           initOnePlusAnyRecycerViewOprator.updateList(goodsList);
           images.clear();
           titles.clear();
           for(Goods goods : goodsList)
           {
               images.add(Quantity.SERVER_URL+goods.getGoods_image());
               titles.add(goods.getGoods_name());
               Log.e("fff:",images.get(0));

           }
           banner.setImages(images);
           banner.setBannerTitles(titles);
           banner.start();
      }
       else {
            getGoodsInfoConnect=new GetGoodsInfoConnect(handler);
            getGoodsInfoConnect.SendByHttpClient();
       }

    }
}