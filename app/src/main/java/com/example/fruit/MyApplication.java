package com.example.fruit;

import android.app.Activity;
import android.app.Application;

import org.litepal.LitePal;

public class MyApplication extends Application {
    public static MyApplication Instance;
    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        //初始化LitePal数据库
        LitePal.initialize(this);

    }
    private Activity mMainActivity;

    public Activity getMainActivity() {
        return mMainActivity;
    }

    public  void setMainActivity(Activity mainActivity) {
        mMainActivity = mainActivity;
    }
}
