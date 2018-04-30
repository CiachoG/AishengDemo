package main_app;

import android.app.Application;

public class MainApplication extends Application {
    private String UserId,UserName;

    @Override
    public void onCreate() {
        super.onCreate();

        UserId="2";
        UserName="15211160230";

    }

    public String getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }
}
