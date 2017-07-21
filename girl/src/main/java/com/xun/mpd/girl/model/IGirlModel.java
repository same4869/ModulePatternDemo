package com.xun.mpd.girl.model;

import com.xun.mpd.girl.bean.NewsBean;

/**
 * Created by xunwang on 2017/7/21.
 */

public interface IGirlModel {

    void loadGirl(onGirlListener listener);//加载数据

    interface onGirlListener {//数据加载完成后的监听回调

        void onComplete(NewsBean newsBean);

        void onError();
    }
}
