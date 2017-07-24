package com.xun.mpd.pic.presenter;

import com.xun.mpd.commlib.base.BasePresenter;
import com.xun.mpd.pic.IPicView;
import com.xun.mpd.pic.bean.PicBean;
import com.xun.mpd.pic.model.IPicModel;
import com.xun.mpd.pic.model.PicModelImpl;

/**
 * Created by xunwang on 2017/7/24.
 */

public class PicPresenter extends BasePresenter<IPicView> {
    private PicModelImpl picModelImpl = new PicModelImpl();

    public void loadPicInfo() {
        if (getView() != null) {
            getView().showDialog();
            picModelImpl.loadPicInfo(new IPicModel.PicInfoListener() {
                @Override
                public void onComplete(PicBean picBean) {
                    if (getView() != null) {
                        getView().showPicInfo(picBean);
                        getView().dismissDialog();
                    }
                }

                @Override
                public void onError() {
                    getView().dismissDialog();
                }
            });
        }
    }
}
