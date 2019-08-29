package com.kzksmarthome.SmartHouseYCT.biz.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.kzksmarthome.common.biz.widget.CustomProgressDialog;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.network.NetworkState;
import com.kzksmarthome.common.lib.network.NetworkStateMgr;
import com.kzksmarthome.common.lib.okhttp.CacheControlMode;

public class BaseRequestFragment extends BaseFragment {
    protected boolean mMarkResponseFromServer = false;
    private NetworkState mLastNetworkState = NetworkStateMgr.getInstance().getNetworkState();
    
    private boolean isViewCreated;
    /**
     * 加载中对话框
     */
    private CustomProgressDialog mLoadingDialog;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }

    private Object mNetworkStateEventReceiver = new Object() {
        @SuppressWarnings("unused")
        public void onEventMainThread(NetworkState networkState) {
            if (mLastNetworkState.ordinal() == NetworkState.UNAVAILABLE.ordinal() && NetworkStateMgr.isThisNetworkAvailable(networkState)) {
                onNetworkAvailableFromUnavailable();
                
                if (!mMarkResponseFromServer && isViewCreated) {
                    doRequest(CacheControlMode.CACHE_REMOTE_AND_CACHE);
                }
            }
            
            mLastNetworkState = networkState;
        }
    };
    
    @Override
    protected void registerMessage() {
        super.registerMessage();
        GjjEventBus.getInstance().register(mNetworkStateEventReceiver);
    }

    @Override
    protected void unRegisterMessage() {
        super.unRegisterMessage();
        GjjEventBus.getInstance().unregister(mNetworkStateEventReceiver);
    }
    
    public void doRequest(int cacheMode) {

    }


    public void onNetworkAvailableFromUnavailable() {

    }

    /**
     * 展示加载中对话框
     */
    protected void showLoadingDialog(int tipResId, boolean cancelable) {
        if (null == mLoadingDialog) {
            mLoadingDialog = new CustomProgressDialog(getActivity());
            mLoadingDialog.setTipText(tipResId);
            mLoadingDialog.setCancelable(cancelable);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    onBackPressed();
                }
            });
        }
        mLoadingDialog.show();
    }

    /**
     * 关闭对话框
     */
    protected void dismissLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
