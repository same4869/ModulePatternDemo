package com.xun.mpd.commlib.appconn.event;

import com.xun.mpd.commlib.appconn.manager.MessengerConnManager;

/**
 * Created by xunwang on 2017/7/26.
 */

public class MsgBean {
    private String msg;
    private MessengerConnManager.MsgType type;//消息发送目的地

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessengerConnManager.MsgType getType() {
        return type;
    }

    public void setType(MessengerConnManager.MsgType type) {
        this.type = type;
    }
}
