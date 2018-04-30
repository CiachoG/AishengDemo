package chat_modular.chat_tools.chat_speech_tools;

import android.content.Context;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

public class SpeechRecognizeExecutor {
    private Context context;

    private InitListener mInitListener;

    private SpeechRecognizer speechRecognizer;  //讯飞自带的语音识别者
    private SpeechRecognizerListener mRecoListener; //获取识别的文本

    public SpeechRecognizeExecutor(Context context){
        this.context=context;
        this.mInitListener=null;

        SpeechUtility.createUtility(context,SpeechConstant.APPID+"=5aa3bf6f");
        iniParams();
    }

    private void iniParams(){
        mRecoListener=new SpeechRecognizerListener();

        speechRecognizer=SpeechRecognizer.createRecognizer(context,mInitListener);
        speechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat"); //设置听写参数
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//设置为中文
        speechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin ");//设置为普通话
    }

    public void startSpeechRecognition() {       //开始语音识别
        speechRecognizer.startListening(this.mRecoListener);
    }

    public String getFinshRecoText(){       //一定要在识别后调用
        return this.mRecoListener.getRecoResultText();
    }

    public void setInitListener(InitListener mInitListener) {
        this.mInitListener = mInitListener;
    }

    public void setExternalAfterRecoRunnable(Runnable runnable){
        this.mRecoListener.setExternalAfterRecoRunnable(runnable);
    }

    public void setInternalAfterRecoRunnable(Runnable runnable){
        this.mRecoListener.setInternalAfterRecoRunnable(runnable);
    }
}