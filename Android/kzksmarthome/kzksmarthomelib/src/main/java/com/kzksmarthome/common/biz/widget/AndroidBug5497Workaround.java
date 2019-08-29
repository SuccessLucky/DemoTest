//Workaround to get adjustResize functionality for input methos when the fullscreen mode is on
//found by Ricardo
//taken from http://stackoverflow.com/a/19494006

package com.kzksmarthome.common.biz.widget;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class AndroidBug5497Workaround {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround(activity);
    }

    private View mChildOfContent;
    private int mUsableHeightPrevious;
    private FrameLayout.LayoutParams mFrameLayoutParams;
    private OnKeyboardListener mOnKeyboardListener;

    private AndroidBug5497Workaround(Activity activity) {
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        possiblyResizeChildOfContent();
                    }
                });
        if (activity instanceof OnKeyboardListener) {
            mOnKeyboardListener = (OnKeyboardListener) activity;
        }
        mFrameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != mUsableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                // remove bty panrq, 2015-03-19，无需改变contentView高度
//                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                if (null != mOnKeyboardListener) {
                    mOnKeyboardListener.onShow(true, heightDifference);
                }
            } else {
                // keyboard probably just became hidden
                // remove bty panrq, 2015-03-19，无需改变contentView高度
//                frameLayoutParams.height = usableHeightSansKeyboard;
                if (null != mOnKeyboardListener) {
                    mOnKeyboardListener.onShow(false, 0);
                }
            }
            mChildOfContent.requestLayout();
            mUsableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    public void setOnKeyboardListener(OnKeyboardListener onKeyboardListener) {
        this.mOnKeyboardListener = onKeyboardListener;
    }

    public interface OnKeyboardListener {

        void onShow(boolean showKeyboard, int height);
    }
}