package com.kzksmarthome.SmartHouseYCT.biz.base;

import in.srain.cube.app.FragmentParam;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.common.module.log.L;

public class BaseCubeMainActivity extends BaseFragmentActivity {

    private final static String LOG_TAG = "cube-fragment";

    protected BaseFragment currentFragment;
    private boolean mCloseWarned;
    
    /**
     * return the string id of close warning
     * <p/>
     * return value which lower than 1 will exit instantly when press back key
     * 
     * @return
     */
    protected String getCloseWarning() {
        return "";
    }

    protected int getFragmentContainerId() {
        return R.id.content;
    }

    public void replaceFragment(Class<?> cls) {
        FragmentParam param = new FragmentParam();
        param.cls = cls;
        goToThisFragment(param);
    }

    public void replaceFragment(Class<?> cls, Object data) {
        FragmentParam param = new FragmentParam();
        param.cls = cls;
        param.data = data;
        goToThisFragment(param);
    }

    protected String getFragmentTag(FragmentParam param) {
        StringBuilder sb = new StringBuilder(param.cls.toString());
        return sb.toString();
    }

    private void goToThisFragment(FragmentParam param) {
        int containerId = getFragmentContainerId();
        Class<?> cls = param.cls;
        if (cls == null) {
            return;
        }
        String fragmentTag = getFragmentTag(param);
        FragmentManager fm = getSupportFragmentManager();
        if (L.isDebugMode()) {
            L.d(LOG_TAG + "before operate, stack entry count: %s", fm.getBackStackEntryCount());
        }
        BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            fragment = getFragmentFactory().getBaseFragment(cls, true);
        }
        if (currentFragment != null && currentFragment != fragment) {
            currentFragment.onLeave();
        }
        fragment.onEnter(param.data);

        FragmentTransaction fT = fm.beginTransaction();
        fT.replace(containerId, fragment, fragment.getClass().getName());
        currentFragment = fragment;
        fT.commitAllowingStateLoss();
        mCloseWarned = false;
    }

    /**
     * process the return back logic return true if back pressed event has been processed and should stay in current view
     * 
     * @return
     */
    protected boolean processBackPressed() {
        return false;
    }

    /**
     * process back pressed
     */
    @Override
    public void onBackPressed() {

        // process back for fragment
        boolean enableBackPressed = true;
        if (currentFragment != null) {
            enableBackPressed = !currentFragment.processBackPressed();
        }
        if (enableBackPressed) {
            processBackPressed();
        }
    }

    protected <T extends BaseFragment> BaseFragment findFragment(Class<T> clazz) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (BaseFragment) fragmentManager.findFragmentByTag(clazz.getName());
    }

    public BaseFragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (BaseFragment) fragmentManager.findFragmentById(R.id.content);
    }

    public void hideKeyboardForCurrentFocus() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showKeyboardAtView(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    protected void exitFullScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
}
