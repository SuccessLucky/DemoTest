package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kzksmarthome.lib.R;

/**
 * 记载的转圈圈
 */
public class LoadingView extends ImageView {
    private Context mContext;

    public LoadingView(Context context) {
        super(context);
        mContext = context;
        initAnimation();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAnimation();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initAnimation();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(getVisibility() == View.VISIBLE) {
            initAnimation();
        }
    }

    private void initAnimation(){
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                mContext, R.anim.loading);
        // 使用ImageView显示动画
        startAnimation(hyperspaceJumpAnimation);
    }

    @Override
    public void setVisibility(int visibility) {
        if(View.VISIBLE == visibility){
            initAnimation();
        } else {
            clearAnimation();
        }
        
        super.setVisibility(visibility);
    }
}
