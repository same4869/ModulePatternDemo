package com.xun.mpd.girl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xun.mpd.IMyAidlInterface;
import com.xun.mpd.commlib.appconn.manager.MessengerConnManager;
import com.xun.mpd.commlib.base.BaseActivity;
import com.xun.mpd.commlib.util.AppUtil;
import com.xun.mpd.girl.bean.NewsBean;
import com.xun.mpd.girl.presenter.GirlsPresenter;
import com.xun.mpd.girl.view.IGirlView;

@Route(path = "/girl/GirlMainActivity")
public class GirlMainActivity extends BaseActivity<IGirlView, GirlsPresenter> implements IGirlView {
    @Autowired
    String kkkk;
    private Button loadDataBtn, getAppDataBtn, sendMessenger;
    private boolean isConn;
    private IMyAidlInterface mIMyAidlInterface;

    @Override
    protected GirlsPresenter createPresenter() {
        return new GirlsPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl_main);
        ARouter.getInstance().inject(this);

        Toast.makeText(getApplicationContext(), kkkk, Toast.LENGTH_SHORT).show();

        initView();

    }

    private void initView() {
        loadDataBtn = (Button) findViewById(R.id.load_data_btn);
        loadDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadData();
            }
        });
        getAppDataBtn = (Button) findViewById(R.id.get_app_data);
        getAppDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConn) {
                    Intent intent = new Intent();
                    intent.setAction("com.xun.mpd.otherapp.service.MyService");//你定义的service的action
                    bindService(AppUtil.getExplicitIntent(getApplicationContext(), intent), mConnection, Context
                            .BIND_AUTO_CREATE);
                } else {
                    if (mIMyAidlInterface != null) {
                        try {
                            mIMyAidlInterface.onMsgRev("来自girl的消息");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        sendMessenger = (Button) findViewById(R.id.send_data_messenger);
        sendMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessengerConnManager.getInstance().sendMsg(MessengerConnManager.MsgType.GIRL, MessengerConnManager
                        .MsgType.OTHERAPP, "我来自girl呵呵", null);
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

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("kkkk", 9999);
        setResult(8888, intent);
        super.finish();
    }

    @Override
    public void showGirls(NewsBean newsBean) {
        Toast.makeText(getApplicationContext(), newsBean.getList().get(0).getAdoContent(), Toast.LENGTH_SHORT).show();
    }
}
