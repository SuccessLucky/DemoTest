package com.kzksmarthome.SmartHouseYCT.biz.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.common.event.EventOfLaunchMainAct;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.module.log.L;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 * 
 */
public class BaseSubActivity extends BaseFragmentActivity {

    private InputMethodManager inputMethodManager;
    public static final String BUNDLE_KEY_BUG_POPBACKSTACK_AFTER_STATESAVED = "popbackStack_exception";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GjjEventBus.getInstance().register(mEventReceiver);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private Object mEventReceiver = new Object() {
        @SuppressWarnings("unused")
        public void onEventBackgroundThread(EventOfLaunchMainAct event) {
            FragmentManager fm = getSupportFragmentManager();
//            if (fm != null&&!fm.isDestroyed()) {
            if (fm != null) {
                int backStackCount = fm.getBackStackEntryCount();
                for (int i = 0; i < backStackCount; i++) {
                    fm.popBackStack();
                }
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        L.d("BaseFragment onNewIntent %s", intent);
        if (null == intent) {
            return;
        }
        // 修正快速点击，可能导致打开多个相同fragment问题
        String className = intent.getStringExtra(INTENT_EXTRA_FRAGMENT_CLASS_NAME);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        if (null != f && f.getClass().getName().equals(className)) {
            return;
        }
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    protected void init(Bundle savedInstanceState) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        // 避免后台销毁，重新进入时，重复加载Fragment
        if (f == null) {
            handleIntent(getIntent());
        } else {
            handleArgs(f.getArguments());
        }
    }

    @Override
    public void onBackPressed() {
        if (isFinishing()) {
            return;
        }
        hideKeyboardForCurrentFocus();
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.content);
        if (f != null && f instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) f;
            if (!fragment.goBack()) {
                if (fm.getBackStackEntryCount() <= 1) {
                    finish();
                } else {
                    try {
                        fm.popBackStackImmediate();
                    } catch (IllegalStateException e) {
                        L.e(e);
                        f.getArguments().putBoolean(BUNDLE_KEY_BUG_POPBACKSTACK_AFTER_STATESAVED, true);
                    }
                    f = fm.findFragmentById(R.id.content);
                    if (f != null) {
                        handleArgs(f.getArguments());
                    }
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        GjjEventBus.getInstance().unregister(mEventReceiver);
        super.onDestroy();
    }

    public void handleIntent(Intent intent) {
        hideKeyboardForCurrentFocus();
        if (intent == null || (intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            return;
        }
        if (isFinishing()) {
            return;
        }
        String className = intent.getStringExtra(INTENT_EXTRA_FRAGMENT_CLASS_NAME);
        Bundle args = intent.getParcelableExtra(INTENT_EXTRA_FRAGMENT_ARGS);
        try {
            @SuppressWarnings("unchecked")
            Class<BaseFragment> clazz = (Class<BaseFragment>) Class.forName(className);
            addFragment(clazz, args);
        } catch (ClassNotFoundException e) {
            L.e(e);
        }
        setIntent(intent);
    }

    /**
     * 添加fragment
     * 
     * @param clazz
     * @param bundle
     */
    private <T extends BaseFragment> void addFragment(Class<T> clazz, Bundle bundle) {

        BaseFragment fragment = getFragmentFactory().getFragment(clazz, false);
        if (fragment == null) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        // set bundle
        if (bundle != null) {
            fragment.setArguments(bundle);
            if (bundle.getBoolean(BaseFragment.FLAT_CLEAR_TOP_FRAGMENT)) {
                while (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStackImmediate();
                }
            }
        }

        FragmentTransaction fT = fm.beginTransaction();
        fT.setCustomAnimations(R.anim.right_side_in, 0, 0, R.anim.right_side_out);
//        fT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment f = fm.findFragmentById(R.id.content);
        if (null != f) {
            fT.hide(f);
        }
        fT.add(R.id.content, fragment);
        fT.addToBackStack(clazz.getName());
        fT.commitAllowingStateLoss();

        handleArgs(bundle);
    }

    protected void handleArgs(Bundle bundle) {

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (null != fragment) {
            fragment.onActivityResult(arg0, arg1, arg2);
        }
    }

    public void hideKeyboardForCurrentFocus() {
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
