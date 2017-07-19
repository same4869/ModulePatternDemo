package com.xun.mpd.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 888;

    private TextView tv1;
    private Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.main_btn1);
        btn1.setOnClickListener(this);
        tv1 = (TextView) findViewById(R.id.main_tv1);
        tv1.setText(getResources().getString(R.string.app_name));
        btn2 = (Button) findViewById(R.id.main_btn2);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_btn1) {
            Intent intent = new Intent(MainActivity.this, MainOtherActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.main_btn2) {
            ARouter.getInstance().build("/girl/GirlMainActivity").withString("kkkk", "hello hehehe!").navigation
                    (this, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == 8888 && data != null) {
            int result = data.getIntExtra("kkkk", 0);
            Toast.makeText(getApplicationContext(), result + "", Toast.LENGTH_SHORT).show();
        }
    }
}
