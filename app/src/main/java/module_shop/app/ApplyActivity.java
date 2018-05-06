package module_shop.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.ciacho.aishengdemo.R;

public class ApplyActivity extends AppCompatActivity {
    private  ImageButton imageButton_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_apply);
        initView();
    }
    public void initView(){
        imageButton_back=findViewById(R.id.btn_back);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
