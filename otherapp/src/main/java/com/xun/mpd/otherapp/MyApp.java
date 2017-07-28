package com.xun.mpd.otherapp;

import android.app.Application;

import com.xun.mpd.commlib.appconn.manager.MessengerConnManager;

/**
 * Created by xunwang on 2017/7/26.
 */

public class MyApp extends Application {
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MessengerConnManager.getInstance().init(this);
    }

    public static MyApp getInstance(){
        return instance;
    }
}
