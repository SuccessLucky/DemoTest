package com.kzksmarthome.common.biz.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kzksmarthome.common.module.log.L;


public class CustomViewPager extends ViewPager {
    private boolean mEnabledTouchScroll = true;
    private boolean mForceDisabledTouchScroll;

    private float mX;
    private float mY;
    private float mXmove;
    private float mYmove;
    private boolean needObtain = true;

    private boolean innerListenerSet;
    private OnPageChangeListener publicListener;

    private boolean canSlidingMen = false;
    private boolean isSlidingMenuEnable = true;

    public static final int INTERCEPT_DO_NOTHING = 0X000;
    public static final int INTERCEPT_YES = 0X001;
    public static final int INTERCEPT_NO = 0X010;
    private int mIntercept = INTERCEPT_DO_NOTHING;

    public void setIntercept(int action) {
        this.mIntercept = action;
    }

    public CustomViewPager(Context context) {
        super(context);
        setOnPageChangeListener(new ViewPagerChild());
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPageChangeListener(new ViewPagerChild());
    }


    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        if (!innerListenerSet) {
            super.setOnPageChangeListener(listener);
            innerListenerSet = true;

        } else {
            publicListener = listener;
        }
    }

    @SuppressLint("Recycle")
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            int action = ev.getAction();

            if (action == MotionEvent.ACTION_DOWN) {
                // This article is excellent! http://blog.csdn.net/seker_xinjian/article/details/6253617
                mEnabledTouchScroll = true;
                mX = ev.getX();
                mY = ev.getY();
//                if(canSlidingMen){//目前只有rankPage/MainGameDetailPage可以滑动
//                    if(mForceDisabledTouchScroll && getCurrentItem() == 0){
//                        getMessagePump().broadcastMessage(Message.Type.CHANGED_SLIDING_MENU_CAN_PAUSE, new MessageData2<Boolean, Boolean>(false, false), Message.PRIORITY_EXTREMELY_HIGH);
////                this.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, mX, mY, 0));
//                        menuEnable = true;
//                    }else if (!mForceDisabledTouchScroll && getCurrentItem() == 0){
//                        getMessagePump().broadcastMessage(Message.Type.CHANGED_SLIDING_MENU_CAN_PAUSE, new MessageData2<Boolean, Boolean>(true, false), Message.PRIORITY_EXTREMELY_HIGH);
//                    }
//                }else {
//                    getMessagePump().broadcastMessage(Message.Type.CHANGED_SLIDING_MENU_CAN_PAUSE, new MessageData2<Boolean, Boolean>(false, false), Message.PRIORITY_EXTREMELY_HIGH);
//                }

            } else if (action == MotionEvent.ACTION_MOVE) {
                mXmove = ev.getX();
                mYmove = ev.getY();

                if (Math.abs(mX - mXmove) > Math.abs(mY - mYmove)) {
                    if (!mForceDisabledTouchScroll && needObtain) {
                        this.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, mX, mY, 0));

                        needObtain = false;

                    } else if (mForceDisabledTouchScroll && needObtain) {
                    }
                }
            } else if (action == MotionEvent.ACTION_UP) {
//                if(menuEnable){
//                    getMessagePump().broadcastMessage(Message.Type.CHANGED_SLIDING_MENU_CAN_PAUSE, new MessageData2<Boolean, Boolean>(true, false), Message.PRIORITY_EXTREMELY_HIGH);
//                    menuEnable = false;
//                }
            }

//        L.d(action+",mEnabledTouchScroll="+mEnabledTouchScroll+",mForceDisabledTouchScroll="+mForceDisabledTouchScroll);
//            return mEnabledTouchScroll && !mForceDisabledTouchScroll && super.onInterceptTouchEvent(ev);

            switch (mIntercept) {
            case INTERCEPT_YES:
                super.onInterceptTouchEvent(ev);
                if (mForceDisabledTouchScroll) {//强制不可滑动，则不拦截事件传递给webview
                    return false;
                }
                return true;
            case INTERCEPT_NO:
                return false;
            case INTERCEPT_DO_NOTHING:
            default:
                return mEnabledTouchScroll && !mForceDisabledTouchScroll && super.onInterceptTouchEvent(ev);
            }

        } catch (Exception e) {
            L.w(e);
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);

        } catch (Exception e) {
            L.w(e);
        }

        return false;
    }

    public void setEnableTouchScrollForViewPager(boolean enableTouchScroll) {
        this.mEnabledTouchScroll = enableTouchScroll;
    }

    public void setForceDisableTouchScroll(boolean forceDisableTouchScroll) {
        mForceDisabledTouchScroll = forceDisableTouchScroll;

        if (mForceDisabledTouchScroll) {
            needObtain = true;
        }
    }
    
    public boolean getForceDisableTouchScroll(){
        return mForceDisabledTouchScroll;
    }
    
    public void setNeedObtain(boolean bt) {
        needObtain = bt;
    }

    public void setCanSlidingMen(boolean bt) {
        canSlidingMen = bt;
    }

    public boolean getIsSlidingMenuEnable() {
        return isSlidingMenuEnable;
    }

    class ViewPagerChild implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i2) {
            //To change body of implemented methods use File | Settings | File Templates.
            if (publicListener != null) {
                publicListener.onPageScrolled(i, v, i2);
            }
        }

        @Override
        public void onPageSelected(int i) {

            if (canSlidingMen) {
                if (i == 0) {
                    isSlidingMenuEnable = true;

                } else {
                    isSlidingMenuEnable = false;
                }

            }

            if (publicListener != null) {
                publicListener.onPageSelected(i);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            if (publicListener != null) {
                publicListener.onPageScrollStateChanged(i);
            }
        }
    }
    
    
}
