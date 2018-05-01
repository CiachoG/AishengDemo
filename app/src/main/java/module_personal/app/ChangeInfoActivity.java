package module_personal.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;
import com.example.ciacho.aishengdemo.app.MainActivity;
import com.example.ciacho.aishengdemo.bean.User;
import com.google.gson.Gson;

/**
 * Created by Ciacho on 2018/5/1.
 */

public class ChangeInfoActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameText;
    private EditText nameEdit;
    private EditText ageEdit;
    private EditText passwordEdit;
    private User user;
    SharedPreferences preferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_changeinfo);

        getuser();
        initView();
    }

    private void initView() {
        imageView=findViewById(R.id.info_image_view);
        Glide.with(this).load(Quantity.SERVER_URL+user.getImage()).into(imageView);
        nameText=findViewById(R.id.info_name_text);
        nameText.setText(user.getUserid());
        nameEdit=findViewById(R.id.info_name_edit);
        nameEdit.setText(user.getUsername());
        ageEdit=findViewById(R.id.info_age_edit);
        ageEdit.setText(user.getAge()+"");
        passwordEdit=findViewById(R.id.info_password_edit);

    }

    public void getuser()
    {
        preferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userInfo=preferences.getString("current_userInfo","");
        user=new Gson().fromJson(userInfo,User.class);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChangeInfoActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
