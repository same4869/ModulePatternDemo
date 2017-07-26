package com.xun.mpd.commlib.appconn.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xun.mpd.commlib.appconn.config.MessengerConfigParm;
import com.xun.mpd.commlib.appconn.event.MsgBean;
import com.xun.mpd.commlib.appconn.manager.MessengerConnManager;

import org.greenrobot.eventbus.EventBus;

import static com.xun.mpd.commlib.appconn.config.MessengerConfigParm.MSG_FROM_GIRL;
import static com.xun.mpd.commlib.appconn.config.MessengerConfigParm.MSG_FROM_OTHERAPP;


/**
 * 用于应用间消息接收
 * Created by xunwang on 16/12/21.
 */

public class BaseMessengerConnService extends Service {
    private Messenger mMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        mMessenger = new Messenger(new MessengerHandler());
        Log.d("kkkkkkkk", "BaseMessengerConnService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private void sendToManager(String receiveMsg, MessengerConnManager.MsgType type) {
        MsgBean msgBean = new MsgBean();
        msgBean.setMsg(receiveMsg);
        msgBean.setType(type);
        EventBus.getDefault().post(msgBean);
    }

    /**
     * 消息接收处理
     *
     * @param msg     消息内容
     * @param srcType 消息来源
     */
    protected void messageHandle(String msg, int srcType) {

    }

    /**
     * 信使的持有, 处理返回信息
     */
    private class MessengerHandler extends Handler {

        public MessengerHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("kkkkkkkk", "BaseMessengerConnService handleMessage");
            if (msg == null || msg.getData() == null) {
                return;
            }
            String receiveMsg = msg.getData().getString(MessengerConfigParm.MSG_ARG);
            switch (msg.what) {
                case MSG_FROM_GIRL:
                    Log.d("kkkkkkkk", "消息来自girl msg.arg1 --> " + receiveMsg);
                    sendToManager(receiveMsg, MessengerConnManager.MsgType.GIRL);
                    break;
                case MSG_FROM_OTHERAPP:
                    Log.d("kkkkkkkk", "消息来自otherapp msg.arg1 --> " + receiveMsg);
                    sendToManager(receiveMsg, MessengerConnManager.MsgType.OTHERAPP);
                    break;
                default:
                    super.handleMessage(msg);
            }
            messageHandle(receiveMsg, msg.what);
        }
    }
}
