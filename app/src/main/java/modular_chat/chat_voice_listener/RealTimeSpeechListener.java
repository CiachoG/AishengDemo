package modular_chat.chat_voice_listener;

import android.content.Context;
import android.widget.Toast;

import modular_chat.chat_tools.chat_speech_tools.SpeechRecognizeExecutor;

//实时监听外界语言输入的类
public class RealTimeSpeechListener {
    private Context context;

    private SpeechRecognizeExecutor speechRecognizeExecutor;//语音识别具体执行者
    private String reco_dataText;    //实时语音识别的最新文本的临时缓冲区

    public RealTimeSpeechListener(final Context context){
        this.context=context;

        speechRecognizeExecutor=new SpeechRecognizeExecutor(context);
        speechRecognizeExecutor.setInternalAfterRecoRunnable(new Runnable() {
            @Override
            public void run() {
                //在非主线程中执行
                reco_dataText=speechRecognizeExecutor.getFinshRecoText();
                Toast.makeText(context,"语音监听结束",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void work(){
        speechRecognizeExecutor.startSpeechRecognition();
    }

    public String getRecoText(){
        return this.reco_dataText;
    }

    public void setExternalAfterRecoRunnable(Runnable runnable){
        speechRecognizeExecutor.setExternalAfterRecoRunnable(runnable);
    }
}
