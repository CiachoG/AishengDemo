package module_shop.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;
import com.example.ciacho.aishengdemo.bean.Goods;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Ciacho on 2018/5/3.
 */

public class GoodDeatail extends AppCompatActivity {

    private int position;
    private List<Goods> goodsList;
    private Goods goods;
    private ImageView imageView;
    private TextView titleTxt;
    private TextView priceTxt;
    private ImageView imageViewBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shop_detail);
        initDate();
        initView();
    }
    public void initDate()
    {
        Intent intent=getIntent();
        position=intent.getIntExtra("position",0);
        //Toast.makeText(this,position+"",Toast.LENGTH_SHORT).show();
        goodsList= DataSupport.findAll(Goods.class);
        goods=goodsList.get(position);
    }
    public void initView(){
        imageView=findViewById(R.id.shop_image);
        Glide.with(this).load(Quantity.SERVER_URL+goods.getGoods_image()).into(imageView);
        titleTxt=findViewById(R.id.tit_desc_xiangqing);
        titleTxt.setText(goods.getGoods_name());
        priceTxt=findViewById(R.id.price_xiangqing);
        priceTxt.setText("¥"+goods.getGoods_price()+"");
        imageViewBack=findViewById(R.id.back_icons);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
