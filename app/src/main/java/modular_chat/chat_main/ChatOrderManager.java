package modular_chat.chat_main;

import android.os.Bundle;
import android.widget.Toast;

//键入的指令解析，指令有系统自定义和快速应答编码两大类
public class ChatOrderManager {
    public static final String START_SPEECHLISTENING="1";       //开始一轮语音识别
    public static final String ENTER_SYLLABLE_INPUTMETHOD="2";  //进入音节输入法
    public static final String ENTER_LETTER_INPUTMETHOD="3";    //进入字母输入法

    public static final String[] ARRAY_SYSORDER={START_SPEECHLISTENING,
            ENTER_SYLLABLE_INPUTMETHOD,ENTER_LETTER_INPUTMETHOD};
    private ChatActivity context;

    public ChatOrderManager(ChatActivity context){
        this.context=context;
    }
    public void orderSolve(String order){   //指令编码
        switch(order){  //是系统预定指令
            case START_SPEECHLISTENING:         //开启一轮实时语音监听
                context.getRealTimeSpeechListener().work();
                Toast.makeText(context,"开启一轮语音监听",Toast.LENGTH_SHORT).show();
                return;
        }
        if(isQuickResponseOrder(order)){    //是快速应答指令
            String content=context.getQuickResponser().getResponseText(order);
            context.getSpeechSynthesizeExecutor().startSpeechSynthesize(content);

            //添加交流的行布局
            context.addListSelfRow(content);
        }else{
            Toast.makeText(context,"无效的指令",Toast.LENGTH_SHORT).show();
        }
    }

    //判断是否为快速应答编码的指令
    private boolean isQuickResponseOrder(String order){
        Bundle bundle=context.getQuickResponseBundle();
        for(String key:bundle.keySet())
            if(key.equals(order))
                return true;
        return false;
    }

}
