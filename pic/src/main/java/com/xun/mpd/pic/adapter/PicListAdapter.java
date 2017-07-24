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
import com.xun.mpd.pic.bean.PicBean;

/**
 * Created by xunwang on 2017/7/24.
 */

public class PicListAdapter extends RecyclerArrayAdapter<PicBean.ListBean> {
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

    private class PicViewHolder extends BaseViewHolder<PicBean.ListBean> {

        private ImageView imageView;
        private TextView textView;

        private PicViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_pic_list);
            imageView = $(R.id.item_pic_iv);
            textView = $(R.id.item_pic_tv);
        }

        @Override
        public void setData(PicBean.ListBean data) {
            super.setData(data);
            Glide.with(getContext()).load(data.getAdoImgUrl()).into(imageView);
            textView.setText(data.getAdoTitle());
        }
    }

    public interface OnMyItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
