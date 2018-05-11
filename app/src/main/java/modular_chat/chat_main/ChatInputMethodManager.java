package modular_chat.chat_main;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import modular_chat.chat_text_watcher.SyllableIMTextWatcher;
import modular_chat.chat_tools.CNCharTransfor;
import modular_chat.chat_tools.chat_speech_tools.SpeechSynthesizeExecutor;

//调度各种输入法的操作
public class ChatInputMethodManager {

    //四个音调键和取消、回车键的字符映射
    public static final char[] TONE_CHARS={'q','w','e','r'};
    //删除字符，确认回车键
    public static final char SPECIAL_CANCEL='t',OK_CHAR='y';

    private EditText edit_targetText;           //目标侦听的编辑文本窗口
    private TextView text_solvedText;
    private ChatActivity context;

    private SpeechSynthesizeExecutor speechSynthExecutor;  //语言合成者
    private ChatOrderManager chatOrderManager;    //处理用户接受的指令

    private SyllableIMTextWatcher syllableIMTextWatcher;    //音节输入法文本监听者
    private StringBuilder builder_finalSolvedStr;   //各种输入法最终确定的即将输出的字符串

    public ChatInputMethodManager(ChatActivity context,
            EditText edit_targetText, TextView text_solvedText){
        this.context=context;
        this.edit_targetText=edit_targetText;
        this.text_solvedText=text_solvedText;

        builder_finalSolvedStr=new StringBuilder("");
        this.edit_targetText.setFocusable(true);
        this.edit_targetText.setFocusableInTouchMode(true);
        this.edit_targetText.requestFocus();

        iniTools();
        iniSyllableInputMethod();
    }

    private void iniTools(){
        speechSynthExecutor=new SpeechSynthesizeExecutor(context);
        setSpeechSynthParam(context.getSpeechSetUpBundle());

        chatOrderManager=new ChatOrderManager(context);
    }

    private void iniSyllableInputMethod(){
        syllableIMTextWatcher=new SyllableIMTextWatcher(context,edit_targetText);
        syllableIMTextWatcher.setSolveOneCharRunnable(new Runnable() {
            @Override
            public void run() {
                //完成一个字的解析

                //把新解析的伪字包含进来
                char ch=syllableIMTextWatcher.getSolvedCNChar();
                builder_finalSolvedStr.append(ch);   //加入进最终解析文本

                String[] array=CNCharTransfor.transformationWithTone(ch);
                if(array==null||array.length==0)  return;

                String newSyllStr=array[0];     //对应的带音调的字符串
                String solvedText=text_solvedText.getText().toString();

                if(solvedText.length()!=0){
                    String str=solvedText+" "+newSyllStr;
                    text_solvedText.setText(str);
                }else{
                    text_solvedText.setText(newSyllStr);
                }
            }
        });
        syllableIMTextWatcher.setDeleteEmptyEditRunnable(new Runnable() {
            @Override
            public void run() {
                //当文本框为空并按下删除键
                if(builder_finalSolvedStr.length()>0)   //删除最后一个字符
                    builder_finalSolvedStr.deleteCharAt(builder_finalSolvedStr.length()-1);

                StringBuilder builder=new StringBuilder("");
                for(int i=0;i<builder_finalSolvedStr.length();++i){
                    if(CNCharTransfor.isCNChar(builder_finalSolvedStr.charAt(i))){
                        //如果为汉字
                        String[] array=CNCharTransfor.
                                transformationWithTone(builder_finalSolvedStr.charAt(i));

                        if(array!=null&&array.length>0){
                            builder.append(i==0?array[0]:" "+array[0]);
                        }else
                            Log.e("错误信息:","汉字转音调拼音出错");
                    }else
                        builder.append(builder_finalSolvedStr.charAt(i));
                }
                text_solvedText.setText(builder.toString());
            }
        });
        syllableIMTextWatcher.setFinishInputRunnable(new Runnable() {
            @Override
            public void run() {
                confirmInput();
            }
        });
    }

    public void confirmInput(){     //按下回车键确认输入
        String edit_str=edit_targetText.getText().toString();
        if(edit_str.length()>0&&edit_str.charAt(edit_str.length()-1)==OK_CHAR){
            edit_str=edit_str.substring(0,edit_str.length()-1);
        }

        String solved_str=builder_finalSolvedStr.toString();

        if(edit_str.length()%2!=0){     //奇数位指令
            chatOrderManager.orderSolve(edit_str);
            syllableIMTextWatcher.clearEditContent();
        }else if(solved_str.length()>0){    //确认输出解析后的内容

            speechSynthExecutor.startSpeechSynthesize(solved_str);
            StringBuilder builder=new StringBuilder("");
            for(int i=0;i<builder_finalSolvedStr.length();++i){
                if(i!=0)    builder.append(" ");
                char ch=builder_finalSolvedStr.charAt(i);
                if(CNCharTransfor.isCNChar(ch)){
                    String[] array=CNCharTransfor.transformationWithTone(ch);
                    if(array!=null&&array.length>0)
                        builder.append(array[0]);
                }else
                    builder.append(ch);
            }

            //添加交流的行布局
            context.addListSelfRow(builder.toString());

            //清掉所有解析出来的字符串
            builder_finalSolvedStr.delete(0,builder_finalSolvedStr.length());
            text_solvedText.setText("");
        }
    }

    public void setSpeechSynthParam(Bundle bundle){
        speechSynthExecutor.setParameter(bundle);
    }

    public SpeechSynthesizeExecutor getSpeechSynthExecutor(){
        return this.speechSynthExecutor;
    }

    public SyllableIMTextWatcher getSyllableIMTextWatcher(){
        return this.syllableIMTextWatcher;
    }
}
