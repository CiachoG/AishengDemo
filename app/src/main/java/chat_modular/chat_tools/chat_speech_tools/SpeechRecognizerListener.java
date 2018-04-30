package chat_modular.chat_tools.chat_speech_tools;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import java.util.List;

//语音开始识别的监听函数
public class SpeechRecognizerListener implements RecognizerListener {

    //从RealTimeListeningThread外部设置的识别之后执行的动作
    private Runnable run_externalAfterReco;

    //从RealTimeListeningThread内部设置的识别之后执行的动作
    //一般在实时监听线程中每当识别出一段文本，会记录文本数据
    private Runnable run_internalAfterReco;

    private String recoResultText;

    public SpeechRecognizerListener(){
        this.run_externalAfterReco=null;
        this.run_internalAfterReco=null;

        recoResultText=null;
    }

    private String resultJson = "[";//放置在外边做类的变量则报错，会造成json格式不对（？）
    @Override
    public void onResult(RecognizerResult recognizerResult, boolean isLast) {
        if(!isLast)
            resultJson+=recognizerResult.getResultString()+",";
        else
            resultJson+=recognizerResult.getResultString()+"]";

        if(isLast) {//解析语音识别后返回的json格式的结果
            Gson gson=new Gson();
            List<DictationResult>resultList=gson.fromJson(resultJson,
                    new TypeToken<List<DictationResult>>(){}.getType());
            String result = "";
            for (int i=0;i<resultList.size()-1;i++) {
                result+=resultList.get(i).toString();
            }

            resultJson="[";     //还原文本
            recoResultText=result;

            if(run_internalAfterReco!=null) run_internalAfterReco.run();
            if(run_externalAfterReco!=null) run_externalAfterReco.run();
        }
    }

    public String getRecoResultText(){
        return this.recoResultText;
    }

    public void setExternalAfterRecoRunnable(Runnable runnable){
        this.run_externalAfterReco=runnable;
    }

    public void setInternalAfterRecoRunnable(Runnable runnable){
        this.run_internalAfterReco=runnable;
    }

    @Override
    public void onVolumeChanged(int i, byte[] bytes) {}

    @Override
    public void onBeginOfSpeech() {}

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(SpeechError speechError) {
    }
    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
    }
}
