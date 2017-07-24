package com.xun.mpd.pic;

import com.xun.mpd.commlib.base.IBaseView;
import com.xun.mpd.pic.bean.AndroidInfoBean;

/**
 * Created by xunwang on 2017/7/24.
 */

public interface IPicView extends IBaseView {
    void showPicInfo(AndroidInfoBean picBean);

    void showPicAddInfo(AndroidInfoBean picBean);//加载更多
}
