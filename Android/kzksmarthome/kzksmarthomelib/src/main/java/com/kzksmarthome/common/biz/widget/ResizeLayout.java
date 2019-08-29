package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kzksmarthome.common.module.log.L;

public class ResizeLayout extends RelativeLayout {

    private int layoutW;// 布局大小
    private int layoutH;
    private int expectW;
    private int expectH;
    private int wms;
    private int hms;
    private OnResizeLayoutListener onResizeLayoutListener;

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // 确保布局大小不由软键盘影响
        if (oldw == 0 && oldh == 0) {
            layoutW = w;
            layoutH = h;
        }

        super.onSizeChanged(layoutW, layoutH, oldw, oldh);

        L.d("onSizeChanged =>%s", " w=" + layoutW + ",h=" + layoutH + ",oldw=" + oldw + ",oldh="
                + oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);
        L.d("onLayout =>%s", changed + " l=" + l + ", t=" + t + ",r=" + r + ",b=" + b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int myWidth = -1;
        int myHeight = -1;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.UNSPECIFIED) {
            myWidth = widthSize;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED) {
            myHeight = heightSize;
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            expectW = myWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            expectH = myHeight;
        }

        int offsetX = expectW - layoutW;
        int offsetY = expectH - layoutH;

        if (wms == 0) {
            // 页面初始化第一次计算
            wms = widthMeasureSpec;
            hms = heightMeasureSpec;
        } else {
            if (null != onResizeLayoutListener) {
                onResizeLayoutListener.onResizeLayout(offsetX, offsetY);
            }
        }

        super.onMeasure(wms, hms);
        L.d("onMeasure =>%s", " widthMeasureSpec=" + wms + ", heightMeasureSpec=" + hms);
    }

    public OnResizeLayoutListener getOnResizeLayoutListener() {
        return onResizeLayoutListener;
    }

    public void setOnResizeLayoutListener(OnResizeLayoutListener onResizeLayoutListener) {
        this.onResizeLayoutListener = onResizeLayoutListener;
    }

    public interface OnResizeLayoutListener {
        public void onResizeLayout(int offsetX, final int offsetY);
    }
}