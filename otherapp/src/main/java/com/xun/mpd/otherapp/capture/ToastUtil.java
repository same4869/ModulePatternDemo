package com.xun.mpd.otherapp.capture;

import android.widget.Toast;

import com.xun.mpd.otherapp.MyApp;

/**
 * Created by xunwang on 2017/7/28.
 */

public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(MyApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
    public static void show(int rid){
        Toast.makeText(MyApp.getInstance(), rid, Toast.LENGTH_SHORT).show();
    }
    public static void showLong(int rid){
        Toast.makeText(MyApp.getInstance(), rid, Toast.LENGTH_LONG).show();
    }
}
