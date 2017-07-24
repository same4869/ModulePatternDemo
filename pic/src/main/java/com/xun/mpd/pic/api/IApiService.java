package com.xun.mpd.pic.api;

import com.xun.mpd.pic.bean.AndroidInfoBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by xunwang on 2017/7/21.
 */

public interface IApiService {

    @GET("Android/{page_count}/{page_num}")
    Call<AndroidInfoBean> getPicAndInfo(@Path("page_count") int count, @Path("page_num") int page);
}
