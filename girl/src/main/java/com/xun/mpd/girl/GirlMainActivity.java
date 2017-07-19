package com.xun.mpd.girl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/girl/GirlMainActivity")
public class GirlMainActivity extends AppCompatActivity {
    @Autowired
    String kkkk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl_main);
        ARouter.getInstance().inject(this);

        Toast.makeText(getApplicationContext(), kkkk, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("kkkk", 9999);
        setResult(8888, intent);
        super.finish();
    }
}
