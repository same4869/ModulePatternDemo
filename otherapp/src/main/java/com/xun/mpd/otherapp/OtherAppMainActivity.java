package com.xun.mpd.otherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xun.mpd.IMyAidlInterface;
import com.xun.mpd.commlib.appconn.manager.MessengerConnManager;
import com.xun.mpd.commlib.util.AppUtil;
import com.xun.mpd.otherapp.capture.ScreenCaptureActivity;
import com.xun.mpd.otherapp.mediaprojection.CaptureManager;
import com.xun.mpd.otherapp.mediaprojection.ScreenRecorder;

import java.io.File;

public class OtherAppMainActivity extends AppCompatActivity {
    public static final int REQUEST_MEDIA_PROJECTION = 18;
    private static final int REQUEST_CODE = 19;
    private MediaProjectionManager mMediaProjectionManager;
    private Button aidlBtn, messengerBtn, recordBtn, captureBtn, coordinatorLayoutBtn;
    private boolean isConn;
    private IMyAidlInterface mIMyAidlInterface;
    private Button screen_capture_btn;
    private ScreenRecorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_app_main);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

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
        screen_capture_btn = (Button) findViewById(R.id.screen_capture_btn);
        screen_capture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherAppMainActivity.this, ScreenCaptureActivity.class);
                startActivity(intent);
            }
        });
        recordBtn = (Button) findViewById(R.id.recorder);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder != null) {
                    mRecorder.quit();
                    mRecorder = null;
                    recordBtn.setText("Restart recorder");
                } else {
                    Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, REQUEST_CODE);
                }
            }
        });
        captureBtn = (Button) findViewById(R.id.capture);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCapturePermission();
            }
        });

        coordinatorLayoutBtn = (Button) findViewById(R.id.coordinatorLayout_btn);
        coordinatorLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherAppMainActivity.this, CoordinatorActivity.class);
                startActivity(intent);
            }
        });
    }

    public void requestCapturePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            return;
        }
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (resultCode == RESULT_OK && data != null) {
                    CaptureManager.setResultData(data);
                    CaptureManager.getInstance(getApplicationContext()).startScreenShot();
                }
                break;
            case REQUEST_CODE:
                MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                if (mediaProjection == null) {
                    Log.e("@@", "media projection is null");
                    return;
                }
                // video size
                final int width = 1280;
                final int height = 720;
                File file = new File(Environment.getExternalStorageDirectory(),
                        "record-" + width + "x" + height + "-" + System.currentTimeMillis() + ".mp4");
                final int bitrate = 6000000;
                mRecorder = new ScreenRecorder(width, height, bitrate, 1, mediaProjection, file.getAbsolutePath());
                mRecorder.start();
                recordBtn.setText("Stop Recorder");
                Toast.makeText(this, "Screen recorder is running...", Toast.LENGTH_SHORT).show();
                moveTaskToBack(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecorder != null) {
            mRecorder.quit();
            mRecorder = null;
        }
    }
}
