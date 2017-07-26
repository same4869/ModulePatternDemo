package com.xun.mpd.girl.service;

import android.util.Log;

import com.xun.mpd.commlib.appconn.service.BaseMessengerConnService;


/**
 * Created by xunwang on 2017/7/26.
 */

public class MyService extends BaseMessengerConnService {
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    public final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
//
//        @Override
//        public void onMsgRev(String msg) throws RemoteException {
//            Log.d("kkkkkkkk", "aidl onMsgRev --> " + msg);
//        }
//    };

    @Override
    protected void messageHandle(String msg, int srcType) {
        super.messageHandle(msg, srcType);
        Log.d("kkkkkkkk", "messenger messageHandle msg --> " + msg + " srcType --> " + srcType);
    }
}
