package com.xun.mpd.otherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CoordinatorActivity extends AppCompatActivity {
    private Button btn1;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        toolbar.setTitle("This is Title");
//        toolbar.setSubtitle("subTitle");
//        toolbar.setNavigationIcon(android.R.drawable.ic_delete);
//        setSupportActionBar(toolbar);

        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        tv1 = (TextView) findViewById(R.id.tv1);
        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX() - v.getWidth() / 2);
                        v.setY(event.getRawY() - v.getHeight() / 2);
                        break;
                }
                return true;
            }
        });
    }
}
