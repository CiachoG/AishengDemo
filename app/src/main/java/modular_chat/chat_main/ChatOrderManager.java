package modular_chat.chat_main;

import android.os.Bundle;
import android.widget.Toast;

//键入的指令解析，指令有系统自定义和快速应答编码两大类
public class ChatOrderManager {
    private static final String START_SPEECHLISTENING="1";       //开始一轮语音识别
    private static final String TURING_RESPONSE="2";         //进入图灵应答模式

    public static final String[] ARRAY_SYSORDER={START_SPEECHLISTENING,
            TURING_RESPONSE};
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
            case TURING_RESPONSE:
                boolean isTuring=context.isTuring();
                if(isTuring){   //关闭图灵机器人
                    context.getStatusTextView().setText("已关闭半自动应答");
                }else{      //开启图灵机器人
                    context.getStatusTextView().setText("已开启半自动应答");
                }
                context.setTuring(!isTuring);
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
