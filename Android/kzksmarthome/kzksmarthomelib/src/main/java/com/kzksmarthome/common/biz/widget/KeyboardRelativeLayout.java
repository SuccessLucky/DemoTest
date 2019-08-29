package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kzksmarthome.common.module.log.L;

public class KeyboardRelativeLayout extends RelativeLayout {

    private boolean mHasInit = false;
    private boolean mHasKeyboard = false;
    private int mHeight;
    private int hms;

    private IOnKeyboardStateChangedListener onKeyboardStateChangedListener;

    public KeyboardRelativeLayout(Context context) {
        super(context);
    }

    public KeyboardRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnKeyboardStateChangedListener(
            IOnKeyboardStateChangedListener onKeyboardStateChangedListener) {
        this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        L.d("KeyboardRelativeLayout mHeight:%s, b:%s", mHeight, b);
        super.onLayout(changed, l, t, r, b);
        if (!mHasInit) {
            mHasInit = true;
            mHeight = b;
        } else {
            mHeight = mHeight < b ? b : mHeight;
        }
        if (mHasInit && mHeight > b) {
            mHasKeyboard = true;
            if (onKeyboardStateChangedListener != null) {
                onKeyboardStateChangedListener.onKeyboardStateChanged(true);
            }
        }
        if (mHasInit && mHasKeyboard && mHeight == b) {
            mHasKeyboard = false;
            if (onKeyboardStateChangedListener != null) {
                onKeyboardStateChangedListener.onKeyboardStateChanged(false);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        L.d("KeyboardRelativeLayout heightMeasureSpec:%s, hms:%s", heightMeasureSpec, hms);
        if (!mHasInit) {
            hms = heightMeasureSpec;
        }

        int newHspec = hms;
        if (hms != heightMeasureSpec) {
            // 高度减一
            // newHspec = MeasureSpec.makeMeasureSpec(mHeight - 1, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, newHspec);
        L.d("KeyboardRelativeLayout onMeasure =>%s", " widthMeasureSpec=" + widthMeasureSpec + ", heightMeasureSpec="
                + newHspec);
    }

    public interface IOnKeyboardStateChangedListener {
        public void onKeyboardStateChanged(boolean state);
    }
}