package com.xun.mpd.pic.model;

import com.xun.mpd.pic.api.ApiBase;
import com.xun.mpd.pic.bean.PicBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xunwang on 2017/7/24.
 */

public class PicModelImpl implements IPicModel {
    @Override
    public void loadPicInfo(final PicInfoListener picInfoListener) {
        ApiBase.getService().getPicAndInfo().enqueue(new Callback<PicBean>() {
            @Override
            public void onResponse(Call<PicBean> call, Response<PicBean> response) {
                if (picInfoListener != null) {
                    picInfoListener.onComplete(response.body());
                }
            }

            @Override
            public void onFailure(Call<PicBean> call, Throwable t) {
                if (picInfoListener != null) {
                    picInfoListener.onError();
                }
            }
        });
    }
}
