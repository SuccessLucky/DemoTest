package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class YScrollLinearLayout extends LinearLayout {

    private Scroller mScroller;

    public YScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void yScrollTo(int sy, int durtion) {

        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0,
                sy - mScroller.getCurrY(), durtion);
        invalidate();
    }

    public int getCurrY() {
        return mScroller.getCurrY();
    }

}
