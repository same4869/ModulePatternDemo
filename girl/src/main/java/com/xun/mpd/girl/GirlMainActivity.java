package com.xun.mpd.girl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xun.mpd.commlib.base.BaseActivity;
import com.xun.mpd.commlib.base.BasePresenter;
import com.xun.mpd.girl.bean.NewsBean;
import com.xun.mpd.girl.presenter.GirlsPresenter;
import com.xun.mpd.girl.view.IGirlView;

@Route(path = "/girl/GirlMainActivity")
public class GirlMainActivity extends BaseActivity<IGirlView, GirlsPresenter> implements IGirlView {
    @Autowired
    String kkkk;
    private Button loadDataBtn;

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
    }

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
