package com.xun.mpd.pic;

import com.xun.mpd.commlib.base.IBaseView;
import com.xun.mpd.pic.bean.PicBean;

/**
 * Created by xunwang on 2017/7/24.
 */

public interface IPicView extends IBaseView{
    void showPicInfo(PicBean picBean);
}
