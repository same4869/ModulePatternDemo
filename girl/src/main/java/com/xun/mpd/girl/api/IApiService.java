package com.xun.mpd.girl.api;

import com.xun.mpd.girl.bean.NewsBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by xunwang on 2017/7/21.
 */

public interface IApiService {

    @GET("/myAppPro/audio/getRollShowAudioList.do")
    Call<NewsBean> searchHistory();
}
