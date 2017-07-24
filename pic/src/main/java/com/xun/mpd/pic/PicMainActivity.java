package com.xun.mpd.pic;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.xun.mpd.commlib.base.BaseActivity;
import com.xun.mpd.pic.adapter.PicListAdapter;
import com.xun.mpd.pic.bean.AndroidInfoBean;
import com.xun.mpd.pic.presenter.PicPresenter;

import java.util.ArrayList;
import java.util.List;

public class PicMainActivity extends BaseActivity<IPicView, PicPresenter> implements IPicView, RecyclerArrayAdapter
        .OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private EasyRecyclerView recyclerView;
    private PicListAdapter picAdapter;

    private List<AndroidInfoBean.ResultsBean> datas = new ArrayList<>();
    private int count = 20;
    private int curPage = 1;

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
        picAdapter.setOnMyItemClickListener(new PicListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder holder) {
                Toast.makeText(getApplicationContext(), "position --> " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        presenter.loadPicInfo(count, curPage);
    }

    @Override
    public void showPicInfo(AndroidInfoBean picBean) {
        picAdapter.clear();
        datas.clear();
        picAdapter.addAll(picBean.getResults());
        datas.addAll(picBean.getResults());
        recyclerView.setRefreshing(false);
    }

    @Override
    public void showPicAddInfo(AndroidInfoBean picBean) {
        picAdapter.addAll(picBean.getResults());
        datas.addAll(picBean.getResults());
    }

    @Override
    public void onLoadMore() {
        if (datas.size() % count == 0) {
            curPage++;
            presenter.loadPicInfo(count, curPage);
        }
    }

    @Override
    public void onRefresh() {
        curPage = 1;
        presenter.loadPicInfo(count, curPage);
    }
}
