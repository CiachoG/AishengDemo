package modular_chat.chat_setup.chat_speech_setup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ciacho.aishengdemo.R;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;

import modular_chat.chat_setup.chat_speech_setup.chat_sounder_setup.SpeechSounderActivity;

public class SpeechSetupActivity extends AppCompatActivity {

    private LinearLayout linear_voiceName;
    private TextView text_voiceName;
    private LinearLayout[] linear_rows;//speed,pitch,volume
    private TextView[] text_params;//speed,pitch,volume
    private ImageButton btn_back;
    private Bundle param_show_map;   //语音设置界面的参数集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_speech_setup);
        linear_rows=new LinearLayout[3];
        text_params=new TextView[3];

        iniMap();
        iniView();
        iniEvent();
    }

    public static final String KEY_VOICENAME="VoiceName",KEY_SPEED="Speed",
            KEY_PITCH="Pitch",KEY_VOLUME="Volume";
    private static final String[] items_speed={"慢","较慢","正常","较快","快"};
    private static final String[] items_pitch={"音调1","音调2","音调3","音调4","音调5"};
    private static final String[] items_volume={"低","稍低","平常","稍高","高"};
    private static final String[] array_num={"10","30","50","70","90"};

    //将设置的显示文本与实际的参数值互相对应
    private void iniMap(){
        param_show_map=new Bundle();
        for(int i=0;i<array_num.length;++i){
            param_show_map.putString(items_speed[i],array_num[i]);
            param_show_map.putString(KEY_SPEED+array_num[i],items_speed[i]);
        }

        for(int i=0;i<array_num.length;++i){
            param_show_map.putString(items_pitch[i],array_num[i]);
            param_show_map.putString(KEY_PITCH+array_num[i],items_pitch[i]);
        }

        for(int i=0;i<array_num.length;++i){
            param_show_map.putString(items_volume[i],array_num[i]);
            param_show_map.putString(KEY_VOLUME+array_num[i],items_volume[i]);
        }

        for(int i = 0; i< SpeechSounderActivity.SOUNDER_NAME.length; ++i){
            param_show_map.putString(SpeechSounderActivity.SOUNDER_NAME[i],SpeechSounderActivity.SOUNDER_PARAMS[i]);
            param_show_map.putString(SpeechSounderActivity.SOUNDER_PARAMS[i],SpeechSounderActivity.SOUNDER_NAME[i]);
        }
    }

    private void iniView(){
        linear_voiceName= (LinearLayout) findViewById(R.id.linear_voiceName);
        linear_rows[0]= (LinearLayout) findViewById(R.id.linear_speed);
        linear_rows[1]= (LinearLayout) findViewById(R.id.linear_pitch);
        linear_rows[2]= (LinearLayout) findViewById(R.id.linear_volume);

        text_voiceName= (TextView) findViewById(R.id.text_voiceName);
        text_params[0]= (TextView) findViewById(R.id.text_speed);
        text_params[1]= (TextView) findViewById(R.id.text_pitch);
        text_params[2]= (TextView) findViewById(R.id.text_volume);

        btn_back= (ImageButton) findViewById(R.id.btn_back);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();   //来自上一个活动的数据

        text_voiceName.setText(param_show_map.getString(bundle.getString(KEY_VOICENAME)));
        text_params[0].setText(param_show_map.getString(KEY_SPEED+bundle.getString(KEY_SPEED)));
        text_params[1].setText(param_show_map.getString(KEY_PITCH+bundle.getString(KEY_PITCH)));
        text_params[2].setText(param_show_map.getString(KEY_VOLUME+bundle.getString(KEY_VOLUME)));
    }

    private void iniEvent(){
        linear_voiceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SpeechSetupActivity.this,SpeechSounderActivity.class);

                startActivityForResult(intent,0);
            }
        });
        linear_rows[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBottomOptionalMenu(items_speed,0,"语速设置");
            }
        });
        linear_rows[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBottomOptionalMenu(items_pitch,1,"语调设置");
            }
        });
        linear_rows[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBottomOptionalMenu(items_volume,2,"音量设置");
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLastActivity();
                finish();
            }
        });
    }

    //点击行设置单元，弹出选项供用户点击
    private void popBottomOptionalMenu(final String[] items,final int clickPos,
                                       String dialogTitle){
        CircleDialog.Builder builder=new CircleDialog.Builder(this);
        builder.setCanceledOnTouchOutside(true);
        builder.setCancelable(true);

        builder.configDialog(new ConfigDialog() {
            @Override
            public void onConfig(DialogParams params) {
                params.animStyle = R.style.dialogWindowAnim;
            }
        });
        builder.setTitle(dialogTitle);
        builder.setItems(items, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<items.length;++i){
                    if(i==position){
                        text_params[clickPos].setText(items[position]);
                        break;
                    }
                }
            }
        });
        builder.setNegative("取消",null);
        builder.show();
    }

    //返回新的设置数据给上一个活动
    private void backLastActivity(){
        Bundle bundle=new Bundle();
        bundle.putString(KEY_VOICENAME,param_show_map.getString(text_voiceName.getText().toString()));
        bundle.putString(KEY_SPEED,param_show_map.getString(text_params[0].getText().toString()));
        bundle.putString(KEY_PITCH,param_show_map.getString(text_params[1].getText().toString()));
        bundle.putString(KEY_VOLUME,param_show_map.getString(text_params[2].getText().toString()));
        Intent intent=new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
    }

    @Override
    public void onBackPressed() {
        backLastActivity();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            String sounder=data.getStringExtra("Sounder");
            //传递过来的英文参数
            text_voiceName.setText(param_show_map.getString(sounder));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
