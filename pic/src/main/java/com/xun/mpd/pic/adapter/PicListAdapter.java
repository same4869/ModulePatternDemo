package com.xun.mpd.pic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.xun.mpd.pic.R;
import com.xun.mpd.pic.bean.AndroidInfoBean;

/**
 * Created by xunwang on 2017/7/24.
 */

public class PicListAdapter extends RecyclerArrayAdapter<AndroidInfoBean.ResultsBean> {
    public OnMyItemClickListener mOnItemClickListener;

    public PicListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicViewHolder(parent);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, holder);
                }
            }
        });
    }

    private class PicViewHolder extends BaseViewHolder<AndroidInfoBean.ResultsBean> {

        private ImageView imageView;
        private TextView textView;

        private PicViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_pic_list);
            imageView = $(R.id.item_pic_iv);
            textView = $(R.id.item_pic_tv);
        }

        @Override
        public void setData(AndroidInfoBean.ResultsBean data) {
            super.setData(data);
            if (data.getImages() != null) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(data.getImages().get(0)).override(400, 400).into(imageView);
            }else{
                imageView.setVisibility(View.GONE);
            }
            textView.setText(data.getDesc());
        }
    }

    public interface OnMyItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
