package com.xun.mpd.girl.presenter;

import com.xun.mpd.commlib.base.BasePresenter;
import com.xun.mpd.girl.bean.NewsBean;
import com.xun.mpd.girl.model.GirlModelImpl;
import com.xun.mpd.girl.model.IGirlModel;
import com.xun.mpd.girl.view.IGirlView;

/**
 * Created by xunwang on 2017/7/21.
 */

public class GirlsPresenter extends BasePresenter<IGirlView> {
    private GirlModelImpl mModel = new GirlModelImpl();

    public void loadData() {
        if (getView() != null) {
            getView().showDialog();
            mModel.loadGirl(new IGirlModel.onGirlListener() {
                @Override
                public void onComplete(NewsBean newsBean) {
                    if (getView() != null) {
                        getView().showGirls(newsBean);
                        getView().dismissDialog();
                    }
                }

                @Override
                public void onError() {
                    if (getView() != null) {
                        getView().dismissDialog();
                    }
                }
            });
        }
    }
}
