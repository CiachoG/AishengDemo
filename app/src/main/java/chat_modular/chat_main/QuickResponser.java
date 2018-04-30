package chat_modular.chat_main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.Map;

//负责存储快速应答的词条数据、读取数据集、编辑数据集
public class QuickResponser {
    public static final String QUICK_RESPONSE="quick_response";
    private Bundle dataSet;     //存储实时快速响应词典数据
    private Context context;
    public QuickResponser(Context context){
        this.context=context;

        ReadDataSet();
    }

    private void ReadDataSet(){
        SharedPreferences pref=context.getSharedPreferences(QUICK_RESPONSE,Context.MODE_PRIVATE);
        Map<String,?> map=pref.getAll();
        dataSet=new Bundle();
        for(Map.Entry<String,?>  entry:map.entrySet())
            dataSet.putString(entry.getKey(),entry.getValue().toString());
    }

    public String getResponseText(String code){
        return this.dataSet.getString(code);
    }

    public void putAll(Bundle bundle){
        dataSet.putAll(bundle);
    }
    public Bundle getDataSet(){
        return this.dataSet;
    }

    public void clear(){
        dataSet.clear();
    }

    public static class Unit{
        private String code;
        private String content;

        public Unit(String code, String content) {
            this.code = code;
            this.content = content;
        }
        public String getCode() {return code;}
        public void setCode(String code) {
            this.code = code;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    }

}
