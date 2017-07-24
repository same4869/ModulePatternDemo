package com.xun.mpd.pic.presenter;

import com.xun.mpd.commlib.base.BasePresenter;
import com.xun.mpd.pic.IPicView;
import com.xun.mpd.pic.bean.AndroidInfoBean;
import com.xun.mpd.pic.model.IPicModel;
import com.xun.mpd.pic.model.PicModelImpl;

/**
 * Created by xunwang on 2017/7/24.
 */

public class PicPresenter extends BasePresenter<IPicView> {
    private PicModelImpl picModelImpl = new PicModelImpl();

    public void loadPicInfo(int count, final int page) {
        if (getView() != null) {
            getView().showDialog();
            picModelImpl.loadPicInfo(new IPicModel.PicInfoListener() {
                @Override
                public void onComplete(AndroidInfoBean picBean) {
                    if (getView() != null) {
                        if (page == 1) {
                            getView().showPicInfo(picBean);
                        } else {
                            getView().showPicAddInfo(picBean);
                        }
                        getView().dismissDialog();
                    }
                }

                @Override
                public void onError() {
                    getView().dismissDialog();
                }
            }, count, page);
        }
    }
}
