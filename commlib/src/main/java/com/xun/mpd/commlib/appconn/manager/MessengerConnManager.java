package com.xun.mpd.commlib.appconn.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.xun.mpd.commlib.appconn.event.MsgBean;
import com.xun.mpd.commlib.util.AppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static com.xun.mpd.commlib.appconn.config.MessengerConfigParm.GIRL_SERVICE_ACTION;
import static com.xun.mpd.commlib.appconn.config.MessengerConfigParm.MSG_ARG;
import static com.xun.mpd.commlib.appconn.config.MessengerConfigParm.MSG_FROM_GIRL;
import static com.xun.mpd.commlib.appconn.config.MessengerConfigParm.MSG_FROM_OTHERAPP;
import static com.xun.mpd.commlib.appconn.config.MessengerConfigParm.OTHERAPP_SERVICE_ACTION;


/**
 * Messenger信息管理类
 * Created by xunwang on 16/12/21.
 */

public class MessengerConnManager implements IBinder.DeathRecipient {

    public static final String TAG = "kkkkkkkk";
    public static final int MAX_RETRY_COUNT = 10;

    private static MessengerConnManager instance;
    private Context mApplicationContext;
    private Handler mHandler;
    private IBinder mBinder;

    private MessengerConnListener mMessengerConnListener;

    private HashMap<MsgType, AppServiceConn> mConnMap = new HashMap<>();
    private HashMap<MsgType, Integer> mRetryCount = new HashMap<>();

    private HashMap<MsgType, LinkedBlockingQueue<SendMsgBean>> mMsgMap = new LinkedHashMap<>();

    private MessengerConnManager() {
        EventBus.getDefault().register(this);
    }

    public static MessengerConnManager getInstance() {
        if (instance == null) {
            synchronized (MessengerConnManager.class) {
                if (instance == null) {
                    instance = new MessengerConnManager();
                }
            }
        }
        return instance;
    }

    /**
     * application onCreate()时初始化，传入applicationContext
     *
     * @param appContext
     */
    public void init(Context appContext) {
        mApplicationContext = appContext;
        if (mApplicationContext != null) {
            mHandler = new Handler(mApplicationContext.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    sendMsgFromMap();
                    mHandler.sendEmptyMessage(0);
                }
            };
            mHandler.sendEmptyMessage(0);
        }
    }

    public void destroy() {
        if (mApplicationContext != null) {
            try {
                for (AppServiceConn conn : mConnMap.values()) {
                    mApplicationContext.unbindService(conn);
                    Log.e(TAG, "destroy conn:" + conn.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().unregister(this);
        mMsgMap = null;
        mConnMap = null;
        mHandler.removeCallbacksAndMessages(null);
        mMessengerConnListener = null;
        instance = null;
        mApplicationContext = null;
    }

    /**
     * 从内存map中读取相应msg并发送
     */
    private void sendMsgFromMap() {
        Iterator<Map.Entry<MsgType, LinkedBlockingQueue<SendMsgBean>>> iterator = mMsgMap.entrySet().iterator();
        //遍历map
        while (iterator.hasNext()) {
            Map.Entry<MsgType, LinkedBlockingQueue<SendMsgBean>> entry = iterator.next();
            MsgType targetType = entry.getKey();
            LinkedBlockingQueue<SendMsgBean> msgs = entry.getValue();
            if (!msgs.isEmpty()) {  //某个targettype消息队列不为空
                AppServiceConn conn = mConnMap.get(targetType);
                if (conn == null) {         //未建立connection连接
                    mMsgMap.remove(targetType);
                    initConn(targetType);

                    int retryCount;     //尝试发送逻辑
                    if (mRetryCount.get(targetType) == null) {
                        retryCount = 1;
                    } else {
                        retryCount = mRetryCount.get(targetType);
                    }
                    if (retryCount < MAX_RETRY_COUNT) {
                        mRetryCount.put(targetType, ++retryCount);
                        Log.e(TAG, "sendMsgFromMap() targettype: " + targetType + " retryCount :" + retryCount);
                    } else {
                        Log.e(TAG, "sendMsgFromMap() targettype: " + targetType + " retryCount > " + MAX_RETRY_COUNT);
                        return;
                    }

                    mMsgMap.put(targetType, msgs);
                    return;
                }
                if (conn.getMessager() != null) {           //取map里value中queue的首个消息
                    SendMsgBean msgBean = msgs.poll();
                    Message msg = initMessage(msgBean);

                    try {
                        conn.getMessager().send(msg);       //service发送消息
                        Log.e(TAG, "sendMsgFromMap() success source:" + msgBean
                                .getSourceType() + " target:" + targetType + " message:"
                                + msgBean.getMessage());
                        if (msgBean.getListener() != null) {
                            msgBean.getListener().OnSendMsgSuccess();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, " sendMsgFromMap() failed source:" + msgBean
                                .getSourceType() + " target:" + targetType + " message:"
                                + msgBean.getMessage());
                        if (msgBean.getListener() != null) {
                            msgBean.getListener().OnSendMsgFailed(e.getMessage());
                        }
                    }
                }
                break;
            } else {      //target hashmap消息为空
                mRetryCount.remove(targetType);
            }

        }

    }

    /**
     * 根据参数初始化 message
     *
     * @param msgBean
     * @return
     */
    private Message initMessage(SendMsgBean msgBean) {
        Message msg;
        switch (msgBean.getSourceType()) {
            case GIRL:
                msg = Message.obtain(null, MSG_FROM_GIRL);
                break;
            case OTHERAPP:
                msg = Message.obtain(null, MSG_FROM_OTHERAPP);
                break;
            default:
                msg = Message.obtain(null, MSG_FROM_GIRL);
                break;
        }
        Bundle data = new Bundle();
        data.putString(MSG_ARG, msgBean.getMessage());
        msg.setData(data);
        return msg;
    }

    /**
     * binder异常丢失回调，重新清空connection,并绑定
     */
    @Override
    public void binderDied() {
        Log.e(TAG, "binderDied()");
//        mConnMap.clear();
//        for (MsgType type : mMsgMap.keySet()) {
//            initConn(type);
//        }
    }

    /**
     * 发送消息入口，外界调用来发送消息
     *
     * @param sourceType 发送方app的类型
     * @param targetType 接收方app的类型
     * @param message    消息内容
     * @param listener   发送方回调
     */
    public void sendMsg(MsgType sourceType, MsgType targetType, String message, SendMsgListener listener) {
        SendMsgBean sendMsgBean = new SendMsgBean();
        sendMsgBean.setMessage(message);
        sendMsgBean.setSourceType(sourceType);
        sendMsgBean.setTargetType(targetType);
        sendMsgBean.setListener(listener);
        sendMsg(sendMsgBean);
    }

    /**
     * 发送消息入口，外界调用来发送消息，将消息添加入对应hashmap的queue中
     */
    public void sendMsg(SendMsgBean msgBean) {
        Log.e(TAG, "sendMsg source:" + msgBean.sourceType + " target:" + msgBean.targetType
                + " message:" + msgBean.getMessage());
        MsgType targetType = msgBean.getTargetType();
        if (mMsgMap.get(targetType) == null) {
            LinkedBlockingQueue<SendMsgBean> list = new LinkedBlockingQueue<>();
            mMsgMap.put(targetType, list);
        }
        LinkedBlockingQueue<SendMsgBean> msgList = mMsgMap.get(targetType);
        msgList.add(msgBean);
        mMsgMap.put(targetType, msgList);
    }

    /**
     * 根据接收方app类型初始化相应的ServiceConnection
     *
     * @param targetType 接收方type
     */
    private void initConn(MsgType targetType) {
        if (mConnMap.get(targetType) != null) {
            return;
        }
        AppServiceConn conn = new AppServiceConn(targetType);
        Intent intent = new Intent();
        switch (targetType) {
            case GIRL:
                intent.setAction(GIRL_SERVICE_ACTION);
                break;
            case OTHERAPP:
                intent.setAction(OTHERAPP_SERVICE_ACTION);
                break;
            default:
                break;
        }
        if (mApplicationContext != null) {
            Intent cintent = AppUtil.getExplicitIntent(mApplicationContext, intent);
            if (cintent == null) {
                return;
            }
            boolean isConnSucc = mApplicationContext.bindService(cintent, conn, Context.BIND_AUTO_CREATE);
            if (isConnSucc) {
                mConnMap.put(targetType, conn);
            }
            Log.e(TAG, "initConn() targetType:" + targetType + " isConnSucc:" + isConnSucc);
        } else {
            Log.e(TAG, "initConn() mApplicationContext == null targetType:" + targetType);
        }
    }

    /**
     * 接收消息
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MsgBean event) {
        if (event != null && mMessengerConnListener != null) {
            mMessengerConnListener.onMsgRec(event.getMsg(), event.getType());
        }
    }

    public void setMessengerConnListener(MessengerConnListener mMessengerConnListener) {
        this.mMessengerConnListener = mMessengerConnListener;
    }

    /**
     * *********************   相关 enum/interface/内部类 定义 start   ********************
     */

    public enum MsgType {
        GIRL, OTHERAPP
    }

    public interface MessengerConnListener {
        void onConnected();

        void onDisConnected();

        void onMsgRec(String msg, MsgType type);
    }

    public interface SendMsgListener {
        void OnSendMsgSuccess();

        void OnSendMsgFailed(String msg);
    }

    public class AppServiceConn implements ServiceConnection {

        private MsgType mTargetType;

        private Messenger mMessager = null;

        public AppServiceConn(MsgType targetType) {
            this.mTargetType = targetType;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected targetType:" + mTargetType + " mConn:" + this
                    .toString());
            try {
                service.linkToDeath(MessengerConnManager.this, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBinder = service;
            mMessager = new Messenger(service);

            if (mMessengerConnListener != null) {
                mMessengerConnListener.onConnected();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected targetType:" + mTargetType + " mConn:" + this
                    .toString());
            mConnMap.remove(mTargetType);
            mMessager = null;
            if (mMessengerConnListener != null) {
                mMessengerConnListener.onDisConnected();
            }
            if (null != mBinder) {
                mBinder.unlinkToDeath(MessengerConnManager.this, 0);
                mBinder = null;
            }
        }

        public Messenger getMessager() {
            return mMessager;
        }
    }

    public class SendMsgBean {
        private String message;
        private MsgType sourceType;
        private MsgType targetType;

        private int retryCount;
        private SendMsgListener listener;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public MsgType getSourceType() {
            return sourceType;
        }

        public void setSourceType(MsgType sourceType) {
            this.sourceType = sourceType;
        }

        public MsgType getTargetType() {
            return targetType;
        }

        public void setTargetType(MsgType targetType) {
            this.targetType = targetType;
        }

        public SendMsgListener getListener() {
            return listener;
        }

        public void setListener(SendMsgListener listener) {
            this.listener = listener;
        }

        public int getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(int retryCount) {
            this.retryCount = retryCount;
        }

    }

    /**
     * *********************   相关 enum/interface/内部类 定义 end   ********************
     */

}
