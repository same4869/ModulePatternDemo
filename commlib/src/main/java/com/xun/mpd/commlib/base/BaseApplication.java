package com.xun.mpd.commlib.base;

import android.app.Application;

/**
 * Created by xunwang on 2017/7/19.
 */

public class BaseApplication extends Application {
    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }

}
