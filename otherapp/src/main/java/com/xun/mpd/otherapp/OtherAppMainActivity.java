package com.xun.mpd.otherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xun.mpd.IMyAidlInterface;
import com.xun.mpd.commlib.appconn.manager.MessengerConnManager;
import com.xun.mpd.commlib.util.AppUtil;

public class OtherAppMainActivity extends AppCompatActivity {
    private Button aidlBtn, messengerBtn;
    private boolean isConn;
    private IMyAidlInterface mIMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_app_main);

        aidlBtn = (Button) findViewById(R.id.aidl_btn);
        aidlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConn) {
                    Intent intent = new Intent();
                    intent.setAction("com.xun.mpd.girl.service.MyService");//你定义的service的action
                    bindService(AppUtil.getExplicitIntent(getApplicationContext(), intent), mConnection, Context
                            .BIND_AUTO_CREATE);
                } else {
                    if (mIMyAidlInterface != null) {
                        try {
                            mIMyAidlInterface.onMsgRev("来自otherapp的消息");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        messengerBtn = (Button) findViewById(R.id.messenger_btn);
        messengerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessengerConnManager.getInstance().sendMsg(MessengerConnManager.MsgType.OTHERAPP, MessengerConnManager
                        .MsgType.GIRL, "我来自other呵呵", null);
            }
        });
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            Log.i("TAG", "onServiceConnected: " + mIMyAidlInterface);
            isConn = true;
            try {
                mIMyAidlInterface.onMsgRev("World!!!");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("TAG", "onServiceDisconnected: " + name);
            isConn = false;
        }
    };
}
