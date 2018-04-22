package personal_moudel.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ciacho.aishengdemo.R;


/**
 * Created by Ciacho on 2018/4/15.
 */

public class FunctionActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_layout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.function_toolbar);
        toolbar.setTitle("功能介绍");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
