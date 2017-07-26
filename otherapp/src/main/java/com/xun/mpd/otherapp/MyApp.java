package com.xun.mpd.otherapp;

import android.app.Application;

import com.xun.mpd.commlib.appconn.manager.MessengerConnManager;

/**
 * Created by xunwang on 2017/7/26.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MessengerConnManager.getInstance().init(this);
    }
}
