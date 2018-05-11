package modular_chat.chat_main_list;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ChatTuringRobot {
    private static final String APIKEY = "e7a1447ed2182d57758ca845e5a0f36e";
    private Handler handler;

    public ChatTuringRobot(Handler handler){
        this.handler=handler;
    }

    public void work(final String question){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // {"code":100000,"text":"你多和我聊聊都知道了。"}
                    String info = URLEncoder.encode(question, "UTF-8");
                    String utl_str = "http://www.tuling123.com/openapi/api?key="+APIKEY+"&info="+info;

                    URL url=new URL(utl_str);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader( connection.getInputStream(), "utf-8"));
                    StringBuilder sb = new StringBuilder("");
                    String line="";
                    while((line=reader.readLine())!=null){
                        sb.append(line);
                    }

                    reader.close();
                    connection.disconnect();

                    String solvedAns="";
                    String jsonResult=sb.toString();
                    JSONArray jsonArray=new JSONArray("["+jsonResult+"]");
                    for(int i=0;i<jsonArray.length();++i){
                        JSONObject obj=jsonArray.getJSONObject(i);
                        solvedAns=obj.getString("text");
                    }

                    Message msg=handler.obtainMessage();
                    msg.what=ChatUIListManager.CODE_UPDATE_UIROW;
                    Bundle bundle=msg.getData();
                    bundle.putString("DATA",solvedAns);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Log.e("图灵机器人智能应答错误:",e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
