package debug;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xun.mpd.commlib.appconn.manager.MessengerConnManager;

/**
 * Created by xunwang on 2017/7/31.
 */

public class VideoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.openDebug();
        ARouter.init(this);
        MessengerConnManager.getInstance().init(this);
    }
}
