package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.common.event.EventOfOpenCamera;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;

/**
 * Created by lizhi on 2017/10/18.
 */

public class FloatView extends ImageView {
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;

    private float moveX;
    private float moveY;

    private final int moveSpace = 10;

    private int mStatusBarHeight = 25;

    private WindowManager wm=(WindowManager)getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams wmParams = ((SmartHomeApp)getContext().getApplicationContext()).getMywmParams();

    public FloatView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mStatusBarHeight = getTopBarHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - mStatusBarHeight;   //25是系统状态栏的高度
        Log.i("currP", "currX"+x+"====currY"+y);



        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX =  event.getX();
                mTouchStartY =  event.getY();
                moveX = x;
                moveY = y;
                Log.i("startP", "startX"+mTouchStartX+"====startY"+mTouchStartY);

                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP:
                if(Math.abs(x - moveX) > moveSpace || Math.abs(y - moveY) > moveSpace){
                    updateViewPosition();
                }else{
                    GjjEventBus.getInstance().post(new EventOfOpenCamera());
                }
                mTouchStartX=mTouchStartY=0;
                break;
        }
        return true;
    }

    private void updateViewPosition(){
        //更新浮动窗口位置参数
        wmParams.x=(int)( x-mTouchStartX);
        wmParams.y=(int) (y-mTouchStartY);
        wm.updateViewLayout(this, wmParams);

    }

    /**
     * 获取状态栏高度
     * @return
     */
    public int getTopBarHeight(){
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        if(statusBarHeight == -1){
            /**
             * 获取状态栏高度——方法2
             * */
            int statusBarHeight2 = -1;
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(object).toString());
                statusBarHeight = getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return statusBarHeight == -1 ? 25 : statusBarHeight;
    }

}
