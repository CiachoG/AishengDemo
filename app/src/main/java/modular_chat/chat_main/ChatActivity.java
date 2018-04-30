package modular_chat.chat_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ciacho.aishengdemo.R;
import java.util.Map;
import modular_chat.chat_main_list.ChatUIListManager;
import modular_chat.chat_setup.chat_speech_setup.SpeechSetupActivity;
import modular_chat.chat_tools.chat_speech_tools.SpeechSynthesizeExecutor;
import modular_chat.chat_voice_listener.RealTimeSpeechListener;

//发声交流的主页面
public class ChatActivity extends AppCompatActivity {
    private TextView text_solvedText;
    private EditText edit_inputRow;

    private Bundle bundle_speechSetup;    //存储语音参数数据
    private QuickResponser quickResponser;      //快速响应的数据集
    private ChatInputMethodManager chatInputMethodManager;
    private ChatUIListManager chatUIListManager;
    private RealTimeSpeechListener realTimeSpeechListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_main);

        iniView();
        iniReadParams();
        iniObject();
    }

    private void iniView(){
        edit_inputRow= (EditText) findViewById(R.id.edit_inputRow);
        edit_inputRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatUIListManager.scrollToLastPosition();
            }
        });
        edit_inputRow.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND||actionId==EditorInfo.IME_ACTION_DONE
                        ||(event!=null&&KeyEvent.KEYCODE_ENTER ==event.getKeyCode() &&
                        KeyEvent.ACTION_DOWN == event.getAction())) {
                    //监听到回车键按键事件

                    chatInputMethodManager.confirmInput();
                }
                return true;
            }
        });
        text_solvedText= (TextView) findViewById(R.id.text_solvedText);
    }

    //读取本地语音设置的初始化参数
    private void iniReadParams(){
        SharedPreferences pref=getSharedPreferences("SpeechSetupData",MODE_PRIVATE);
        bundle_speechSetup =new Bundle();
        bundle_speechSetup.putString(SpeechSetupActivity.KEY_VOICENAME,pref.getString(SpeechSetupActivity.KEY_VOICENAME,"xiaoyan"));
        bundle_speechSetup.putString(SpeechSetupActivity.KEY_SPEED,pref.getString(SpeechSetupActivity.KEY_SPEED,"50"));
        bundle_speechSetup.putString(SpeechSetupActivity.KEY_PITCH,pref.getString(SpeechSetupActivity.KEY_PITCH,"50"));
        bundle_speechSetup.putString(SpeechSetupActivity.KEY_VOLUME,pref.getString(SpeechSetupActivity.KEY_VOLUME,"50"));
    }

    private void iniObject(){
        quickResponser=new QuickResponser(this);

        chatInputMethodManager=new ChatInputMethodManager(this,edit_inputRow,text_solvedText);
        chatUIListManager=new ChatUIListManager(this);
        realTimeSpeechListener=new RealTimeSpeechListener(this);
        realTimeSpeechListener.setExternalAfterRecoRunnable(new Runnable() {
            @Override
            public void run() {
                String content=realTimeSpeechListener.getRecoText();
                chatUIListManager.addListOtherRow(content);
                Toast.makeText(ChatActivity.this,"识别结束",Toast.LENGTH_SHORT).show();
            }
        });
        edit_inputRow.addTextChangedListener(chatInputMethodManager.getSyllableIMTextWatcher());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Bundle bundle;
            switch (requestCode){
                case ChatToolBar.REQ_GETSETUPDATA:
                    bundle=data.getExtras();
                    bundle_speechSetup.clear();
                    bundle_speechSetup.putAll(bundle);    //获取新的设置数据，并进行对语音合成进行新设置,保存
                    chatInputMethodManager.setSpeechSynthParam(bundle_speechSetup);
                    writeParams(bundle_speechSetup);
                    break;
                case ChatToolBar.REQ_QUICKRESPONSE:
                    bundle=data.getExtras();
                    quickResponser.clear();
                    quickResponser.putAll(bundle);
                    writeQuickResponseData();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //写入发声设置数据到硬盘中
    private void writeParams(Bundle bundle){
        SharedPreferences.Editor editor=getSharedPreferences("SpeechSetupData",MODE_PRIVATE).edit();
        editor.clear();

        editor.putString(SpeechSetupActivity.KEY_VOICENAME,bundle.getString(SpeechSetupActivity.KEY_VOICENAME));
        editor.putString(SpeechSetupActivity.KEY_SPEED,bundle.getString(SpeechSetupActivity.KEY_SPEED));
        editor.putString(SpeechSetupActivity.KEY_PITCH,bundle.getString(SpeechSetupActivity.KEY_PITCH));
        editor.putString(SpeechSetupActivity.KEY_VOLUME,bundle.getString(SpeechSetupActivity.KEY_VOLUME));
        editor.apply();
    }

    //写入快速应答词条数据到硬盘
    private void writeQuickResponseData(){
        SharedPreferences.Editor editor=getSharedPreferences(QuickResponser.QUICK_RESPONSE,MODE_PRIVATE).edit();
        editor.clear();

        Bundle bundle=quickResponser.getDataSet();
        for(String key:bundle.keySet())
            editor.putString(key,bundle.getString(key));
        editor.apply();
    }

    public void addListSelfRow(String str){
        this.chatUIListManager.addListSelfRow(str);
    }


    public Bundle getSpeechSetUpBundle(){
        return this.bundle_speechSetup;
    }
    public Bundle getQuickResponseBundle(){
        return this.quickResponser.getDataSet();
    }
    public RealTimeSpeechListener getRealTimeSpeechListener(){
        return this.realTimeSpeechListener;
    }
    public SpeechSynthesizeExecutor getSpeechSynthesizeExecutor(){
        return this.chatInputMethodManager.getSpeechSynthExecutor();
    }
    public QuickResponser getQuickResponser(){
        return this.quickResponser;
    }
    public Map<Integer,String> getSyllableMapData(){
        return chatInputMethodManager.getSyllableIMTextWatcher().getSyllableMapData();
    }
    public ChatUIListManager getChatUIListManager(){
        return this.chatUIListManager;
    }
}
