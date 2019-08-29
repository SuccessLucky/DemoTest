package com.kzksmarthome.common.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.user.UserMgr;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.kzksmarthome.common.sharedpreference.SharedPreferenceWrapper;
import com.kzksmarthome.lib.R;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 * 
 */
public class SmartHomeAppLib {
    private static SmartHomeAppLib mAppLib;

    private Application mApplication;

    private boolean mIsInitialized = false;
    private SharedPreferences mSharePref = null;
    private Toast mToast;
    private int mToastY = 0;// toast默认显示高度
    private UserMgr mUserMgr;

    public static SmartHomeAppLib getInstance() {
        return mAppLib;
    }

    private boolean mIsForeProcess = false;
    private boolean mIsBackProcess = false;

    // private LockPatternUtils mLockPatternUtils;

    public static void onCreate(Application app, boolean isForeProcess, boolean isBackProcess) {
        mAppLib = new SmartHomeAppLib(app, isForeProcess, isBackProcess);
    }
    
    public static void attachUserMgr(UserMgr userMgr) {
        mAppLib.mUserMgr = userMgr;
    }

    private SmartHomeAppLib(Application app, boolean isForeProcess, boolean isBackProcess) {
        mApplication = app;
        this.mIsForeProcess = isForeProcess;
        this.mIsBackProcess = isBackProcess;
    }

    public Context getContext() {
        return mApplication;
    }

    // public LockPatternUtils getLockPatternUtils() {
    // if (mLockPatternUtils == null) {
    // mLockPatternUtils = new LockPatternUtils(this);
    // }
    // return mLockPatternUtils;
    // }

    public boolean isInitialized() {
        return mIsInitialized;
    }


    public boolean isForeProcess() {
        return mIsForeProcess;
    }

    public boolean isBackProcess() {
        return mIsBackProcess;
    }

    /**
     * all get preference should use here,for multi process read and write
     * 
     * @return
     */
    public SharedPreferences getPreferences() {
        // warning: we should every time call getSharedPreferences here,because MODE_MULTI_PROCESS need it.
        /*
         * int mode = MODE_PRIVATE; if (Build.VERSION.SDK_INT > 9) { //For applications targetting SDK versions greater than Android 2.3,
         * this flag must be explicitly set if desired mode = mode | MODE_MULTI_PROCESS; } return
         * getSharedPreferences(SHARED_PREFERENCE_NAME, mode);
         */
        if (mSharePref == null) {
            int mode = mApplication.MODE_PRIVATE;

            if (mIsForeProcess) {
                mSharePref = new SharedPreferenceWrapper(mApplication.getSharedPreferences(
                        SharePrefConstant.SHARED_PREFERENCE_NAME, mode));

            } else {
                mSharePref = mApplication.getSharedPreferences(
                        SharePrefConstant.SHARED_PREFERENCE_NAME, mode);
            }
        }

        return mSharePref;
    }

    public void resetPreference() {
        mSharePref = null;
    }

    /**
     * toast 文字
     * 
     * @param textResId
     */
    public static void showToast(int textResId) {
        showToast(mAppLib.getContext().getString(textResId), 0);
    }

    /**
     * toast 文字带图片
     * 
     * @param textResId
     * @param iconResId
     */
    public static void showToast(int textResId, int iconResId) {
        showToast(mAppLib.getContext().getString(textResId), iconResId);
    }

    /**
     * toast 文字
     * 
     * @param text
     */
    public static void showToast(String text) {
        showToast(text, 0);
    }

    /**
     * toast 文字带图片
     * @param text
     * @param iconResId
     */
    public static void showToast(String text, int iconResId) {
        showToast( text,  iconResId,Toast.LENGTH_SHORT);
    }
    /**
     * toast 文字
     *
     * @param textResId
     */
    public static void showToastLongTime(int textResId) {
        showToast(mAppLib.getContext().getString(textResId), 0,Toast.LENGTH_LONG);
    }

    /**
     * toast 文字带图片
     *
     * @param text
     * @param iconResId
     * @param  time Toast.LENGTH_SHORT Toast.LENGTH_LONG
     */
    public static void showToast(String text, int iconResId,int time) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        TextView tv;
        if (null == mAppLib.mToast) {
            mAppLib.mToast = Toast.makeText(mAppLib.getContext(), text, time);
            LayoutInflater inflate = (LayoutInflater) mAppLib.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            tv = (TextView) inflate.inflate(R.layout.dialog_toast, null);
            mAppLib.mToast.setView(tv);
            mAppLib.mToastY = mAppLib.mToast.getYOffset();
        }
        tv = (TextView) mAppLib.mToast.getView();
        tv.setText(text);
        Drawable icon = null;
        if (iconResId > 0) {
            try {
                icon = mAppLib.getContext().getResources().getDrawable(iconResId);
            } catch (Exception e) {
                L.e(e);
            }
            mAppLib.mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mAppLib.mToast.setGravity(Gravity.BOTTOM, 0, mAppLib.mToastY);
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        mAppLib.mToast.show();
    }
    /**
     * 取消toast
     */
    public static void cancelToast() {
        if (null != mAppLib.mToast) {
            mAppLib.mToast.cancel();
        }
    }

    public static String getGjjPackageName() {
        return mAppLib.getContext().getPackageName();
    }
    
    public static UserMgr getUserMgr() {
        return mAppLib.mUserMgr;
    }
}
