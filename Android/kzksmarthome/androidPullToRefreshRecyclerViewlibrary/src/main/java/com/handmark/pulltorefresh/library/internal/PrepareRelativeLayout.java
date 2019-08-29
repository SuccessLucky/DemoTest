package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class PrepareRelativeLayout extends RelativeLayout {

    private boolean isPrepareLayout = false;
    private RefreshPrepareLayoutListener refreshPrepareLayoutListener;

    public PrepareRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PrepareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrepareRelativeLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (getHeight() > 0) {
            if (!isPrepareLayout && null != refreshPrepareLayoutListener) {
                refreshPrepareLayoutListener.onPrepareLayout();
            }
            isPrepareLayout = true;
        }
    }

    public RefreshPrepareLayoutListener getRefreshPrepareLayoutListener() {
        return refreshPrepareLayoutListener;
    }

    public void setRefreshPrepareLayoutListener(RefreshPrepareLayoutListener refreshPrepareLayoutListener) {
        this.refreshPrepareLayoutListener = refreshPrepareLayoutListener;
    }

    public interface RefreshPrepareLayoutListener {
        public void onPrepareLayout();
    }
}