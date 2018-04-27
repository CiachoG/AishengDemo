package HttpConnect;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ciacho on 2018/4/26.
 */

public class RegisterConnect {
    private String responseString=null;
    private Handler handler;
    public RegisterConnect(Handler handler){
        this.handler=handler;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public void SendByHttpClient(final String id, final String pw){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL("http://47.100.170.83:8080/aisheng/Regist");
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes("ID="+id+"&PW="+pw);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream=connection.getInputStream();
                    reader =new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    setResponseString(response.toString());
                    System.out.println(response);
                    Message message=handler.obtainMessage();
                    message.what=1;
                    handler.sendMessage(message);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }if(connection!=null)
                {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
