package com.xun.mpd.app;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xun.mpd.commlib.base.BaseApplication;
import com.xun.mpd.commlib.config.Constants;

/**
 * Created by xunwang on 2017/7/19.
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Constants.IS_DEBUG) {
            //一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }
}
