package com.kzksmarthome.SmartHouseYCT.biz.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.login.LoginActivity;
import com.kzksmarthome.SmartHouseYCT.biz.login.LoginFragment;
import com.kzksmarthome.SmartHouseYCT.biz.login.RegisterInfoFragment;
import com.kzksmarthome.SmartHouseYCT.biz.login.RegisterSubmitFragment;
import com.kzksmarthome.SmartHouseYCT.biz.login.ResetPassword;


public class PageSwitcher {

    /**
     * 切换到带顶部导航界面
     * 
     * @param act
     * @param fragment
     * @param bundle
     * @param backText 返回按钮文字，空则隐藏按钮
     * @param title 顶部导航标题，空则隐藏按钮
     * @param rightTitle 右边按钮标题，空则隐藏按钮
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
            Bundle bundle, String backText, String title, String rightTitle) {
        switchToTopNavPage(act, fragment, bundle, backText, title, null, rightTitle, 0, null);
    }
    
    /**
     * 切换到带顶部导航界面
     * @param act
     * @param fragment
     * @param bundle
     * @param backTextResId 返回按钮文字，空则隐藏按钮
     * @param titleResId 顶部导航标题，空则隐藏按钮
     * @param rightTitleResId 右边按钮标题，空则隐藏按钮
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
                                          Bundle bundle, int backTextResId, int titleResId, int rightTitleResId) {
        SmartHomeApp ctx = SmartHomeApp.getInstance();
        String backText = backTextResId == 0 ? null : ctx.getString(backTextResId);
        String title = titleResId == 0 ? null : ctx.getString(titleResId);
        String rightTitle = rightTitleResId == 0 ? null : ctx.getString(rightTitleResId);
        switchToTopNavPage(act, fragment, bundle, backText, title, null, rightTitle, 0, null);
    }

    /**
     * 切换到带顶部导航界面
     * 
     * @param act
     * @param fragment
     * @param bundle
     * @param backText 返回按钮文字，空则隐藏按钮
     * @param title 顶部导航标题，空则隐藏按钮
     * @param rightTitle 右边按钮标题，空则隐藏按钮
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
            Bundle bundle, String backText, String title, String bottomText, String rightTitle) {
        switchToTopNavPage(act, fragment, bundle, backText, title, bottomText, rightTitle, 0, null);
    }

    /**
     * 切换到带顶部导航界面
     *
     * @param act
     * @param fragment
     * @param bundle
     * @param backText 返回按钮文字，空则隐藏按钮
     * @param title 顶部导航标题，空则隐藏按钮
     * @param rightTitle 右边按钮标题，空则隐藏按钮
     * @param rightDrawableId 标题right drawable
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
            Bundle bundle, String backText, String title, String rightTitle, int rightDrawableId) {
        switchToTopNavPage(act, fragment, bundle, backText, title, null, rightTitle,
                rightDrawableId, null);
    }

    /**
     * 切换到带顶部导航界面
     * 
     * @param act
     * @param fragment
     * @param bundle
     * @param backText 返回按钮文字，空则隐藏按钮
     * @param title 顶部导航标题，空则隐藏按钮
     * @param rightTitle 右边按钮标题，空则隐藏按钮
     * @param rightDrawableId 标题right drawable
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
            Bundle bundle, String backText, String title, String rightTitle, int rightDrawableId,
            String rightDeputyText) {
        switchToTopNavPage(act, fragment, bundle, backText, title, null, rightTitle,
                rightDrawableId, rightDeputyText);
    }

    /**
     * 切换到带顶部导航界面
     *
     * @param act
     * @param fragment
     * @param bundle
     * @param backText 返回按钮文字，空则隐藏按钮
     * @param title 顶部导航标题，空则隐藏按钮
     * @param rightTitle 右边按钮标题，空则隐藏按钮
     * @param rightDrawableId 标题right drawable
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
                                          Bundle bundle, String backText, String title, String rightTitle, int rightDrawableId,
                                          String rightDeputyText, int rightTvDrawableId) {
        switchToTopNavPage(act, fragment, bundle, backText, title, null, rightTitle,
                rightDrawableId, rightDeputyText, rightTvDrawableId);
    }

    /**
     * 切换到带顶部导航界面
     * 
     * @param act
     * @param fragment
     * @param bundle
     * @param backText 返回按钮文字，空则隐藏按钮
     * @param title 顶部导航标题，空则隐藏按钮
     * @param bottomText 顶部导航标题底部文字，空则隐藏按钮
     * @param rightTitle 右边按钮标题，空则隐藏按钮
     * @param rightDrawableId 标题right drawable
     * @param rightDeputyText 右边按钮标题副标题
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
            Bundle bundle, String backText, String title, String bottomText, String rightTitle,
            int rightDrawableId, String rightDeputyText) {
        if (null == bundle) {
            bundle = new Bundle();
        }
        bundle.putString(TopNavSubActivity.PARAM_BACK_TITLE, backText);
        bundle.putString(TopNavSubActivity.PARAM_TOP_TITLE, title);
        bundle.putString(TopNavSubActivity.PARAM_BOTTOM_TITLE, bottomText);
        bundle.putString(TopNavSubActivity.PARAM_TOP_RIGHT, rightTitle);
        bundle.putInt(TopNavSubActivity.PARAM_TITLE_RIGHT_DRAWABLE, rightDrawableId);
        bundle.putString(TopNavSubActivity.PARAM_TOP_RIGHT_DEPUTY, rightDeputyText);
        switchToTopNavPage(act, fragment, bundle);
    }

    /**
     * 切换到带顶部导航界面
     *
     * @param act
     * @param fragment
     * @param bundle
     * @param backText 返回按钮文字，空则隐藏按钮
     * @param title 顶部导航标题，空则隐藏按钮
     * @param bottomText 顶部导航标题底部文字，空则隐藏按钮
     * @param rightTitle 右边按钮标题，空则隐藏按钮
     * @param rightDrawableId 标题right drawable
     * @param rightDeputyText 右边按钮标题副标题
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
                                          Bundle bundle, String backText, String title, String bottomText, String rightTitle,
                                          int rightDrawableId, String rightDeputyText, int rightTvDrawableId) {
        if (null == bundle) {
            bundle = new Bundle();
        }
        bundle.putString(TopNavSubActivity.PARAM_BACK_TITLE, backText);
        bundle.putString(TopNavSubActivity.PARAM_TOP_TITLE, title);
        bundle.putString(TopNavSubActivity.PARAM_BOTTOM_TITLE, bottomText);
        bundle.putString(TopNavSubActivity.PARAM_TOP_RIGHT, rightTitle);
        bundle.putInt(TopNavSubActivity.PARAM_TITLE_RIGHT_DRAWABLE, rightDrawableId);
        bundle.putString(TopNavSubActivity.PARAM_TOP_RIGHT_DEPUTY, rightDeputyText);
        bundle.putInt(TopNavSubActivity.PARAM_TOP_RIGHT_DRAWABLE, rightTvDrawableId);
        switchToTopNavPage(act, fragment, bundle);
    }

    /**
     * 切换到带顶部导航界面
     * 
     * @param act
     * @param fragment
     * @param bundle
     */
    public static void switchToTopNavPage(Activity act, Class<? extends Fragment> fragment,
            Bundle bundle) {
    	SmartHomeApp app = SmartHomeApp.getInstance();
        Intent intent = new Intent(app, TopNavSubActivity.class);
        intent.putExtra(BaseFragmentActivity.INTENT_EXTRA_FRAGMENT_CLASS_NAME, fragment.getName());
        if (fragment == RegisterInfoFragment.class ||fragment == LoginFragment.class
                || fragment == RegisterSubmitFragment.class || fragment == ResetPassword.class) {
            intent.putExtra("noCheckLogin", true);
        }
        if(null != bundle && bundle.getInt("layoutid", -1) != -1){
            intent.putExtra(TopNavSubActivity.PARAM_LAYOUT, bundle.getInt("layoutid"));
        }
        intent.putExtra(BaseFragmentActivity.INTENT_EXTRA_FRAGMENT_ARGS, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(act, intent);
    }

    public static void switchToLoginActivity(Activity act) {
    	SmartHomeApp app = SmartHomeApp.getInstance();
        Intent intent = new Intent(app, LoginActivity.class);
        startActivity(act, intent);
    }

    public static void startActivity(Activity act, Intent intent) {
        if (act == null) {
            act = MainActivity.getMainActivity();
            if (act == null) {
                SmartHomeApp app = SmartHomeApp.getInstance();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                app.startActivity(intent);
            } else {
                act.startActivity(intent);
            }
        } else {
            act.startActivity(intent);
        }
    }

}
