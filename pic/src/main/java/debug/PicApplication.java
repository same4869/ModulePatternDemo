package debug;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xun.mpd.commlib.base.BaseApplication;

/**
 * Created by xunwang on 2017/7/24.
 */

public class PicApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.openDebug();
        ARouter.init(this);
    }
}