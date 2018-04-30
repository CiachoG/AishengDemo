package chat_modular.chat_tools.chat_speech_tools;


import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

public class SpeechSynthesizeExecutor {
    private Context context;
    private InitListener mInitListener;

    private SpeechSynthesizer speechSynthesizer;    //执行具体语音合成操作的变量
    private SpeechSynthesizerListener mSynListener; //语音合成的监听变量

    public SpeechSynthesizeExecutor(Context context){
        this.context=context;
        this.mInitListener=null;
        this.mSynListener=null;

        SpeechUtility.createUtility(context,SpeechConstant.APPID+"=5aa3bf6f");
        iniParams();
    }

    private void iniParams(){
        mSynListener=new SpeechSynthesizerListener();

        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        speechSynthesizer=SpeechSynthesizer.createSynthesizer(context, mInitListener);

        speechSynthesizer.setParameter(SpeechConstant.PARAMS, null);// 清空参数
        speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        speechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, "3");

        //设置播放合成音频打断音乐播放，默认为true
        speechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
    }

    public void startSpeechSynthesize(String targetText){
        int code = speechSynthesizer.startSpeaking(targetText, mSynListener);
        if(code!= ErrorCode.SUCCESS) {
            if(code==ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                //上面的语音配置对象为初始化时：
                Toast.makeText(context, "语音组件未安装",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "语音合成失败,错误码: "+code,Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setParameter(Bundle bundle){
        String str;
        str=bundle.getString("VoiceName");
        if(str!=null)
            speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,str);//合成发音人

        str=bundle.getString("Speed");
        if(str!=null)
            speechSynthesizer.setParameter(SpeechConstant.SPEED,str);   //语速

        str=bundle.getString("Pitch");
        if(str!=null)
            speechSynthesizer.setParameter(SpeechConstant.PITCH,str);   //语调

        str=bundle.getString("Volume");
        if(str!=null)
            speechSynthesizer.setParameter(SpeechConstant.VOLUME,str);  //音量
    }
    public void setmInitListener(InitListener mInitListener) {
        this.mInitListener = mInitListener;
    }
}
