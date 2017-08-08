package com.xun.mpd.otherapp.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by xunwang on 2017/8/3.
 */

public class EasyBehavior extends CoordinatorLayout.Behavior<TextView> {
    public EasyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("kkkkkkkk", "EasyBehavior");
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        //告知监听的dependency是Button
        Log.d("kkkkkkkk", "EasyBehavior layoutDependsOn");
        return dependency instanceof Button;
    }

    @Override  //当 dependency(Button)变化的时候，可以对child(TextView)进行操作
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        child.setX(dependency.getX() + 100);
        child.setY(dependency.getY() + 100);
        child.setText(dependency.getX() + "," + dependency.getY());
        Log.d("kkkkkkkk", "EasyBehavior onDependentViewChanged");
        return true;
    }
}
