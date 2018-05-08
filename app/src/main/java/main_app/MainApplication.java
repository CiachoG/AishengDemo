package main_app;

import android.app.Application;
import android.content.SharedPreferences;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getUserId() {
        SharedPreferences pref=this.getSharedPreferences("userInfo",MODE_PRIVATE);
        return pref.getString("current_userInfo","-1");
    }
}
