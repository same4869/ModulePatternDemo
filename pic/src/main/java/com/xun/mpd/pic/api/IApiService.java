package com.xun.mpd.pic.api;

import com.xun.mpd.pic.bean.PicBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by xunwang on 2017/7/21.
 */

public interface IApiService {

    @GET("/myAppPro/audio/getDropDownAudio.do?cId=0")
    Call<PicBean> getPicAndInfo();
}
