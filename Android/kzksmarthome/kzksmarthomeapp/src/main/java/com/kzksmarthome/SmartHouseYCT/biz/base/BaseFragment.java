package com.kzksmarthome.SmartHouseYCT.biz.base;

import in.srain.cube.app.CubeFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.module.log.L;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 * 
 */
public class BaseFragment extends CubeFragment implements OnTouchListener {

    /**
     * 保存本Fragment是否是hidden状态
     */
    private static final String SAVE_INSTANCE_STATE_KEY_IS_HIDDEN = "isHidden";

    /**
     * 清除所有fragment回退栈
     */
    public static final String FLAT_CLEAR_TOP_FRAGMENT = "clear_top_fragment";
    protected View mRootView;

    protected String getName() {
        return ((Object) this).getClass().getSimpleName();
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        L.d("BaseFragment# onInflate %s", getName());
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        L.d("BaseFragment# onActivityCreated %s", getName());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.d("BaseFragment# onActivityResult %s", getName());
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        L.d("BaseFragment# onAttach %s", getName());
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        L.d("BaseFragment# onCreate %s", getName());
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isHidden = savedInstanceState.getBoolean(SAVE_INSTANCE_STATE_KEY_IS_HIDDEN);
            if (isHidden) {
                FragmentTransaction fT = this.getFragmentManager().beginTransaction();
                fT.hide(this);
                fT.commitAllowingStateLoss();
            }
        }
    }


    @Override
    public void onDestroy() {
        L.d("BaseFragment# onDestroy %s", getName());
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        L.d("BaseFragment# onDestroyView %s", getName());
        unRegisterMessage();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        L.d("BaseFragment# onDetach %s", getName());
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        L.d("BaseFragment# onHiddenChanged %s, hidden: %s", getName(), hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onLowMemory() {
        L.d("BaseFragment# onLowMemory %s", getName());
        super.onLowMemory();
    }

    @Override
    public void onPause() {
        L.d("BaseFragment# onPause %s", getName());
        super.onPause();
    }

    @Override
    public void onResume() {
        L.d("BaseFragment# onResume %s", getName());
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        L.d("BaseFragment# onSaveInstanceState %s", getName());
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_INSTANCE_STATE_KEY_IS_HIDDEN, isHidden());
    }

    @Override
    public void onStart() {
        L.d("BaseFragment# onStart %s", getName());
        super.onStart();
    }

    @Override
    public void onStop() {
        L.d("BaseFragment# onStop %s", getName());
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        L.d("BaseFragment# onViewCreated %s", getName());
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
        registerMessage();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        L.d("BaseFragment# onContextItemSelected %s", getName());
        return super.onContextItemSelected(item);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        L.d("BaseFragment# onCreateAnimation %s", getName());
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        L.d("BaseFragment# onCreateContextMenu %s", getName());
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        L.d("BaseFragment# onCreateOptionsMenu %s", getName());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
        L.d("BaseFragment# onDestroyOptionsMenu %s", getName());
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        L.d("BaseFragment# onOptionsItemSelected %s", getName());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        L.d("BaseFragment# onOptionsMenuClosed %s", getName());
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        L.d("BaseFragment# onPrepareOptionsMenu %s", getName());
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        L.d("BaseFragment# onPrepareOptionsMenu %s", getName());
        super.onViewStateRestored(savedInstanceState);
    }

    public void onBackPressed() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    protected void registerMessage() {
        if (L.isDebugMode()) {
            L.d("BaseFragment# registerMessage %s", getName());
        }
    }

    protected void unRegisterMessage() {
        L.d("BaseFragment# unRegisterMessage %s", getName());
        try {
            if (GjjEventBus.getInstance().isRegistered(this)) {
                GjjEventBus.getInstance().unregister(this);
            }
        } catch (Exception e) {
        }
    }

    public void runOnUiThread(Runnable action) {
        runOnUiThread(action, 0);
    }

    public void runOnUiThread(Runnable action, long delay) {
        if (delay > 0f) {
            MainTaskExecutor.scheduleTaskOnUiThread(delay, action);
        } else {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                MainTaskExecutor.runTaskOnUiThread(action);
            } else {
                action.run();
            }
        }

    }

    public boolean goBack() {
        return false;
    }

    @Override
    protected View createView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.d("BaseFragment# onCreateView %s", getName());
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        // mRootView.setOnTouchListener(this);
        return mRootView;
    }

    public View findViewById(int id) {
        return mRootView == null ? null : mRootView.findViewById(id);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideKeyboardForCurrentFocus();
        }
        return true;
    }

    public void onRightBtnClick() {

    }

    public void onTitleBtnClick() {

    }

    protected void handleArgs(Bundle bundle) {

    }

    public void hideKeyboardForCurrentFocus() {
        try {
            FragmentActivity activity = getActivity();
            if (activity instanceof BaseSubActivity) {
                ((BaseSubActivity) activity).hideKeyboardForCurrentFocus();
            }
        } catch (Exception e) {
            L.e(e);
        }
    }

    public void onSoftKeyboardStateChange(boolean showKeyboard, int height) {

    }

    public void showToast(int textResId) {
        if (isVisible()) {
            SmartHomeApp.showToast(textResId);
        }
    }

    /**
     * toast 文字带图片
     * 
     * @param textResId
     * @param iconResId
     */
    public void showToast(int textResId, int iconResId) {
        if (isVisible()) {
        	SmartHomeApp.showToast(textResId, iconResId);
        }
    }

    /**
     * toast 文字
     * 
     * @param text
     */
    public void showToast(String text) {
        if (isVisible()) {
        	SmartHomeApp.showToast(text);
        }
    }

    /**
     * toast 文字带图片
     * 
     * @param text
     * @param iconResId
     */
    public void showToast(String text, int iconResId) {
        if (isVisible()) {
        	SmartHomeApp.showToast(text, iconResId);
        }
    }

    /**
     * 取消toast
     */
    public void cancelToast() {
    	SmartHomeApp.cancelToast();
    }
}
