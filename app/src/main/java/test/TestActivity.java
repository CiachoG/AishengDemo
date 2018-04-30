package test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;

/**
 * Created by Ciacho on 2018/4/29.
 */

public class TestActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        imageView=findViewById(R.id.image);
        Glide.with(this)
                .load(Quantity.SERVER_URL+"image/head/banner2.jpg")
                .into(imageView);
    }

}
