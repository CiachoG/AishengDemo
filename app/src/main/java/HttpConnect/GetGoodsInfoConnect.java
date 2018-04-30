package HttpConnect;

import android.os.Handler;
import android.os.Message;

import com.example.ciacho.aishengdemo.Quantity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ciacho on 2018/4/30.
 */

public class GetGoodsInfoConnect {
    private String loginFlag=null;
    private Handler handler;
    public GetGoodsInfoConnect(Handler handler){
        this.handler=handler;
    }
    public String getLoginFlag() {
        return loginFlag;
    }
    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }
    public void SendByHttpClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL(Quantity.SERVER_URL+"GetGoods");
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream=connection.getInputStream();
                    reader =new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    setLoginFlag(response.toString());
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
