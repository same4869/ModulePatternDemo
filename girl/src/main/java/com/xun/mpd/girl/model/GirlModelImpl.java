package com.xun.mpd.girl.model;

import com.xun.mpd.girl.api.ApiBase;
import com.xun.mpd.girl.bean.NewsBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xunwang on 2017/7/21.
 */

public class GirlModelImpl implements IGirlModel {
    @Override
    public void loadGirl(final onGirlListener listener) {

        ApiBase.getService().searchHistory().enqueue(new Callback<NewsBean>() {

            @Override
            public void onResponse(Call<NewsBean> call, Response<NewsBean> response) {
                listener.onComplete(response.body());
            }

            @Override
            public void onFailure(Call<NewsBean> call, Throwable t) {
                listener.onError();
            }
        });
    }
}
