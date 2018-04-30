package modular_chat.chat_main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.ciacho.aishengdemo.R;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import java.util.Map;
import modular_chat.chat_setup.chat_quick_response_setup.QuickResponseActivity;
import modular_chat.chat_setup.chat_speech_setup.SpeechSetupActivity;
import modular_chat.chat_setup.chat_syllable_setup.SyllableLibraryActivity;

//交流活动的工具栏
public class ChatToolBar extends LinearLayout {
    public static final int REQ_GETSETUPDATA=0,REQ_QUICKRESPONSE=1;
    private ChatActivity context;
    public ChatToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=(ChatActivity)context;

        LayoutInflater.from(context).inflate(R.layout.layout_chat_toolbar,this);
        ImageButton btn_optional= (ImageButton)findViewById(R.id.btn_optional);
        btn_optional.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popBottomMenu();
            }
        });
    }

    static final String[] items={"语音设置","快速应答词库","音节输入法标准库"};
    private void popBottomMenu(){
        CircleDialog.Builder builder=new CircleDialog.Builder(context);
        builder.setCanceledOnTouchOutside(true);
        builder.setCancelable(true);
        builder.configDialog(new ConfigDialog() {
            @Override
            public void onConfig(DialogParams params) {
                params.animStyle = R.style.dialogWindowAnim;
            }
        });
        builder.setTitle("交流设置");
        builder.setItems(items, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position){
                    case 0:     //语音设置
                    intent=new Intent(context,SpeechSetupActivity.class);
                    intent.putExtras(context.getSpeechSetUpBundle());
                    context.startActivityForResult(intent,REQ_GETSETUPDATA);
                    break;
                    case 1:     //快速应答词库
                        intent=new Intent(context,QuickResponseActivity.class);
                        intent.putExtras(context.getQuickResponseBundle());
                        //传递ChatActivity的快速应答Bundle数据到下一个活动

                        context.startActivityForResult(intent,REQ_QUICKRESPONSE);
                        break;
                    case 2:     //音节输入法标准库
                        intent=new Intent(context, SyllableLibraryActivity.class);
                        Bundle bundle=new Bundle();
                        Map<Integer,String> map=context.getSyllableMapData();
                        for(Map.Entry entry:map.entrySet()){
                            Integer key=(Integer)entry.getKey();
                            String value=(String)entry.getValue();
                            bundle.putString(String.valueOf(key),value);
                        }

                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        break;
                }
            }
        });
        builder.setNegative("取消",null);
        builder.show();
    }
}
