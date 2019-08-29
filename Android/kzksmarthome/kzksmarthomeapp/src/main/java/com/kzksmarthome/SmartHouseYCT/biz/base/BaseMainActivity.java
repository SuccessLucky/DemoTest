package com.kzksmarthome.SmartHouseYCT.biz.base;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kzksmarthome.SmartHouseYCT.R;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 * 
 */
public class BaseMainActivity extends BaseFragmentActivity {

    private ArrayList<Fragment> mList = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public <T extends BaseFragment> Fragment replaceFragment(Class<T> clazz) {
        return replaceFragment(clazz, null);
    }

    /**
     * @param clazz
     * @param args
     */
    public <T extends BaseFragment> Fragment replaceFragment(Class<T> clazz, Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fT = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(clazz.getName());
        if (fragment == null) {
            fragment = getFragmentFactory().getFragment(clazz, false);
            fragment.setArguments(args);
            fT.add(R.id.content, fragment, clazz.getName());
        } else {
            fT.show(fragment);
        }
        if (!mList.contains(fragment)) {
            mList.add(fragment);
        }

        for (Fragment other : mList) {
            if (other != fragment) {
                fT.hide(other);
            }
        }
        fT.commitAllowingStateLoss();

        return fragment;
    }

    protected <T extends BaseFragment> BaseFragment findFragment(Class<T> clazz) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (BaseFragment) fragmentManager.findFragmentByTag(clazz.getName());
    }

    public BaseFragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (BaseFragment) fragmentManager.findFragmentById(R.id.content);
    }

    protected void init() {
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = getCurrentFragment();
        if (fragment == null || !fragment.goBack()) {
            super.onBackPressed();
        }
    }

}
