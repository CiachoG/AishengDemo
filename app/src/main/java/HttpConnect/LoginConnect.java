package HttpConnect;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ciacho on 2018/4/22.
 */

public class LoginConnect {
    private String loginFlag=null;

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public void SendByHttpClient(final String id, final String pw){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL("http://192.168.1.7:8080/Aisheng_server/Login");
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
                   setLoginFlag(response.toString());
                    System.out.println(response);
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
