package com.xun.mpd.pic;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.xun.mpd.commlib.base.BaseActivity;
import com.xun.mpd.pic.adapter.PicListAdapter;
import com.xun.mpd.pic.bean.PicBean;
import com.xun.mpd.pic.presenter.PicPresenter;

public class PicMainActivity extends BaseActivity<IPicView, PicPresenter> implements IPicView, RecyclerArrayAdapter
        .OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private EasyRecyclerView recyclerView;
    private PicListAdapter picAdapter;

    @Override
    protected PicPresenter createPresenter() {
        return new PicPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_main);
        initView();
        initData();
    }

    private void initView() {
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        picAdapter = new PicListAdapter(getApplicationContext());
        picAdapter.setNoMore(R.layout.view_nomore);
        picAdapter.setMore(R.layout.view_more, this);
        recyclerView.setAdapter(picAdapter);
        recyclerView.setRefreshListener(this);
    }

    private void initData() {
        presenter.loadPicInfo();
    }

    @Override
    public void showPicInfo(PicBean picBean) {
        picAdapter.clear();
        picAdapter.addAll(picBean.getList());
//        Toast.makeText(getApplicationContext(), picBean.getFeedList().get(0).getContent(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(getApplicationContext(), "onLoadMore", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(), "onRefresh", Toast.LENGTH_SHORT).show();
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(false);
            }
        }, 2000);
    }
}
