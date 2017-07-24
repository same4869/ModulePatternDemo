package com.xun.mpd.pic.model;

import com.xun.mpd.pic.bean.PicBean;

/**
 * Created by xunwang on 2017/7/24.
 */

public interface IPicModel {
    void loadPicInfo(PicInfoListener picInfoListener);

    interface PicInfoListener{
        void onComplete(PicBean picBean);

        void onError();
    }
}
