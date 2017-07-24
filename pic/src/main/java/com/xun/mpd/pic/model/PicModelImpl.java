package com.xun.mpd.pic.model;

import com.xun.mpd.pic.api.ApiBase;
import com.xun.mpd.pic.bean.AndroidInfoBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xunwang on 2017/7/24.
 */

public class PicModelImpl implements IPicModel {
    @Override
    public void loadPicInfo(final PicInfoListener picInfoListener, int count, int page) {
        ApiBase.getService().getPicAndInfo(count, page).enqueue(new Callback<AndroidInfoBean>() {
            @Override
            public void onResponse(Call<AndroidInfoBean> call, Response<AndroidInfoBean> response) {
                if (picInfoListener != null) {
                    picInfoListener.onComplete(response.body());
                }
            }

            @Override
            public void onFailure(Call<AndroidInfoBean> call, Throwable t) {
                if (picInfoListener != null) {
                    picInfoListener.onError();
                }
            }
        });
    }
}
