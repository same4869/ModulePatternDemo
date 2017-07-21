package com.xun.mpd.girl.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xunwang on 2017/7/21.
 */

public class ApiBase {

    public static IApiService getService() {
        return getService(null);
    }

    public static IApiService getService(String ip) {
        return getService(ip, 0, 0);
    }

    public static IApiService getService(String ip, long readTime, long connectTime) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(readTime <= 0 ? 30 : readTime, TimeUnit.SECONDS)
                .connectTimeout(connectTime <= 0 ? 30 : connectTime, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip == null ? "http://appapi.tingtoutiao.com" : ip)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(IApiService.class);
    }
}
