package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.lib.R;

public class LoadTipsRelativeLayout extends RelativeLayout {

    private RelativeLayout.LayoutParams centerParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
    private View emptyView;
    private View errorView;
    private View loadingView;

    public LoadTipsRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        centerParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    }

    public LoadTipsRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        centerParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    }

    public LoadTipsRelativeLayout(Context context) {
        super(context);
        centerParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    }

    /**
     * 什么数据都没有
     */
    public void empty() {
        for (int i = 0; i < this.getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
//        if (null == emptyView) {
//            emptyView = LayoutInflater.from(getContext()).inflate(R.layout.load_empty_view, null);
//            emptyView.setLayoutParams(centerParams);
//            this.addView(emptyView);
//        }
//        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败，点击重试
     */
    public void error(OnClickListener onErrorClick) {

        for (int i = 0; i < this.getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        if (null == errorView) {
            errorView = LayoutInflater.from(getContext()).inflate(R.layout.load_error_view, null);
            errorView.setLayoutParams(centerParams);
            errorView.setOnClickListener(onErrorClick);
            this.addView(errorView);
        }
        errorView.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(SmartHomeAppLib.getInstance().getContext(), R.anim.fade_in);
        anim.setDuration(600);
        errorView.startAnimation(anim);
    }

    /**
     * 显示内容
     */
    public void showContent() {
        removeChild(emptyView);
        removeChild(errorView);
        removeChild(loadingView);
        emptyView = null;
        errorView = null;
        for (int i = 0; i < this.getChildCount(); i++) {
            getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * loading提示
     * 
     * @param tipRes
     */
    public void showLoadingPop(int tipRes) {
        showLoadingPop(getContext().getString(tipRes));
    }

    /**
     * loading提示
     * 
     * @param tipText
     */
    public void showLoadingPop(String tipText) {
        removeChild(emptyView);
        removeChild(errorView);
        emptyView = null;
        errorView = null;
        if (null == loadingView) {
            loadingView = LayoutInflater.from(getContext()).inflate(R.layout.loading_view, null);
            loadingView.setLayoutParams(centerParams);
            loadingView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            this.addView(loadingView);
        }
        TextView tipTv = (TextView) loadingView.findViewById(R.id.pop_text);
        tipTv.setText(tipText);
        loadingView.setVisibility(View.VISIBLE);
    }

    /**
     * 取消loading提示
     */
    public void dismissLoadingPop() {
        removeChild(loadingView);
        loadingView = null;
    }

    private void removeChild(View view) {
        if (view != null && view.getParent() == this) {
            this.removeView(view);
        }
    }
}
