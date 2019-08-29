package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;

/**
 * Created by lizhi on 2017/10/17.
 */

public class WindowUtils {

    private static   WindowManager mWindowManager;

    private  static FloatView mLayout;

    public static void showView(Activity context){
        if(mLayout != null){
            return;
        }
        mLayout=new FloatView(context);
        mLayout.setBackgroundResource(R.drawable.camera_shap_bg);
        mLayout.setImageResource(R.drawable.camera_im);
        //获取WindowManager
        mWindowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        //设置LayoutParams(全局变量）相关参数
        WindowManager.LayoutParams param = SmartHomeApp.getInstance().getMywmParams();

        param.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;     // 系统提示类型,重要
        param.format=1;
        param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制

        param.alpha = 1.0f;

        param.gravity=Gravity.LEFT|Gravity.TOP;   //调整悬浮窗口至左上角



        //以屏幕左上角为原点，设置x、y初始值
        param.x= 0;
        param.y= 0;

        //设置悬浮窗口长宽数据
        param.width=140;
        param.height=140;

        //显示myFloatView图像
        mWindowManager.addView(mLayout, param);

    }

    public static void destroy(){
        if(mWindowManager != null && mLayout != null){
            mWindowManager.removeView(mLayout);
            mLayout = null;
        }
    }

}
