package com.xun.mpd.pic.model;

import com.xun.mpd.pic.bean.AndroidInfoBean;

/**
 * Created by xunwang on 2017/7/24.
 */

public interface IPicModel {
    void loadPicInfo(PicInfoListener picInfoListener, int count, int page);

    interface PicInfoListener {
        void onComplete(AndroidInfoBean picBean);

        void onError();
    }
}
