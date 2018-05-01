package module_personal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;
import com.example.ciacho.aishengdemo.bean.User;
import com.google.gson.Gson;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;

import module_login.LoginActivity;
import module_personal.app.AboutActivity;
import module_personal.app.ChangeInfoActivity;

@SuppressLint("ValidFragment")
public class PersonalFragment extends Fragment {
    private SuperTextView sText_about;
    private SuperTextView sText_Tx;
    private User user;
    private SharedPreferences preferences;
    public static PersonalFragment getInstance() {
        PersonalFragment sf = new PersonalFragment();
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_layout, null);
        sText_about=v.findViewById(R.id.about_aisheng);
        sText_Tx=v.findViewById(R.id.per_srcname);
        /*Glide.with(this)
                .load("http://192.168.2.101:8080/aisheng/image/head/banner2.jpg")
                .into(sText_Tx.getLeftIconIV());*/
        getUserInfo();
        sText_Tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Quantity.LOGIN_FLAG==1)
                {
                    showDialog();
                }
                else {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        sText_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),AboutActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private void getUserInfo() {
        preferences=getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userInfo=preferences.getString("current_userInfo","");
        if(userInfo!="")
        {
            Quantity.LOGIN_FLAG=1;
            user=new Gson().fromJson(userInfo,User.class);
            sText_Tx.setLeftString(user.getUsername());
             Glide.with(this)
                .load(Quantity.SERVER_URL+user.getImage())
                .into(sText_Tx.getLeftIconIV());
        }
    }
    public void showDialog()
    {
        final String[] items = {"修改个人信息","退出登录"};
        new CircleDialog.Builder()
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColorPress = Color.CYAN;
                        //增加弹出动画
                        //params.animStyle = R.style.dialogWindowAnim;
                    }
                })
                .setItems(items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int
                            position, long id) {
                        switch(position)
                        {
                            case 0:
                                Intent intent1=new Intent(getActivity(), ChangeInfoActivity.class);
                                startActivity(intent1);
                                break;
                            case 1:
                                Quantity.LOGIN_FLAG=0;
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent=new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                break;
                        }

                    }
                })
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = Color.RED;
                        params.backgroundColorPress = Color.WHITE;
                    }
                })
                .show(getActivity().getSupportFragmentManager());
    }
}