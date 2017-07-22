package com.xun.mpd.commlib.base;

import java.lang.ref.WeakReference;

/**
 * Created by xunwang on 2017/7/21.
 */

public class BasePresenter<V> {
    protected WeakReference<V> mReference;

    /**
     * 连接上View模型，类型于Activity与Fragment的连接onTachActivity()
     * @param view
     */
    public void attachView(V view){
        mReference=new WeakReference<V>(view);
    }
    /**
     * 断开与View模型的连接，类型于Activity与Fragment的断开ondeTachActivity()
     * 防止后面做一些无用的事情
     */
    public void detachView(){
        if (mReference!=null) {
            mReference.clear();
        }
    }
    protected V getView(){
        return mReference.get();
    }

}
