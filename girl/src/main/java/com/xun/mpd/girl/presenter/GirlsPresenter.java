package com.xun.mpd.girl.presenter;

import com.xun.mpd.girl.bean.NewsBean;
import com.xun.mpd.girl.model.GirlModelImpl;
import com.xun.mpd.girl.model.IGirlModel;
import com.xun.mpd.girl.view.IGirlView;

/**
 * Created by xunwang on 2017/7/21.
 */

public class GirlsPresenter implements BasePresenter{
    private IGirlView mView;
    private GirlModelImpl mModel = new GirlModelImpl();

    public GirlsPresenter(IGirlView mView){
        this.mView = mView;
    }

    public void loadData(){
        if(mView!=null){
            mModel.loadGirl(new IGirlModel.onGirlListener() {
                @Override
                public void onComplete(NewsBean newsBean) {
                    mView.showGirls(newsBean);
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
