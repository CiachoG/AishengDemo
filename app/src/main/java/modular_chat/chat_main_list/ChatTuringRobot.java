package modular_chat.chat_main_list;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import modular_chat.chat_tools.AssetSDCardUnpacker;
import modular_chat.chat_tools.CNCharTool;

public class ChatTuringRobot {
    private static final String APIKEY = "e7a1447ed2182d57758ca845e5a0f36e";
    private static final int MAX_SENTENCE_LENGTH=3;
    private Context context;
    private Handler handler;

    private List<TuringExchangeObject> turingCustomList;
    private boolean isDataLoad;

    public ChatTuringRobot(Context context, Handler handler){
        this.context=context;
        this.handler=handler;

        iniData();
    }

    private void iniData(){
        isDataLoad=false;
        turingCustomList=new ArrayList<>();
        loadCustomResponseData();
    }

    //加载本地xml数据
    private void loadCustomResponseData(){
        try{
            String xmlPath=context.getFilesDir().getAbsolutePath()+"/CustRespDataSet.xml";
            File file=new File(xmlPath);
            if(!file.exists()){
                AssetSDCardUnpacker.work(context,xmlPath,"CustRespDataSet.xml");
            }

            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            Document doc=builder.parse(file);
            NodeList itemList=doc.getElementsByTagName("Item");
            for(int i=0;i<itemList.getLength();++i){
                Element element=(Element) itemList.item(i);
                String question=element.getElementsByTagName("Question").
                        item(0).getFirstChild().getNodeValue();
                String answer=element.getElementsByTagName("Answer").
                        item(0).getFirstChild().getNodeValue();

                turingCustomList.add(new TuringExchangeObject(question,answer));
            }
            isDataLoad=true;
        }catch (Exception e){
            Log.e("自定义应答数据加载错误",e.getMessage());
            isDataLoad=false;
        }
    }




    public void work(final String question){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i=0;i<turingCustomList.size();++i){
                        int spacing=CNCharTool.getSentenceSpacing(question,
                                turingCustomList.get(i).getQuestion());
                        Log.e("222",spacing+"");
                        if(spacing<=MAX_SENTENCE_LENGTH){

                            Message msg=handler.obtainMessage();
                            msg.what=ChatUIListManager.CODE_UPDATE_UIROW;
                            Bundle bundle=msg.getData();
                            bundle.putString("DATA",turingCustomList.get(i).getAnswer());
                            handler.sendMessage(msg);
                            return;
                        }
                    }


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

    public static class TuringExchangeObject{
        private String question;
        private String answer;

        public TuringExchangeObject(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }


}
