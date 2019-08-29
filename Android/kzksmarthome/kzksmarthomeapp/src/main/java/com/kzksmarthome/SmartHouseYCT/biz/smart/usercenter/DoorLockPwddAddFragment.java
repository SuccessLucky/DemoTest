package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddDoorPwdResponse;
import com.kzksmarthome.common.biz.widget.EditTextWithDel;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: DoorLockPwddAddFragment
 * @Description: 添加门锁密码界面
 * @date 2016/11/9 23:06
 */
public class DoorLockPwddAddFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.doorlock_pwd)
    EditTextWithDel doorlockPwd;
    @BindView(R.id.doorlock_pwd_confirm)
    EditTextWithDel doorlockPwdConfirm;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;

    private int deviceId = -1;

    @Override
    public void onRightBtnClick() {
        if (TextUtils.isEmpty(doorlockPwd.getText().toString())) {
            showToast(R.string.enter_doorlock_pwd);
            return;
        }
        int pwdLen = doorlockPwd.getText().toString().length();
        if (pwdLen < 6 || pwdLen > 16) {
            showToast(R.string.enter_correct_pwd);
            return;
        }
        if (TextUtils.isEmpty(doorlockPwdConfirm.getText().toString())) {
            showToast(R.string.confirm_doorlock_pwd);
            return;
        }
        int pwdLen1 = doorlockPwdConfirm.getText().toString().length();
        if (pwdLen1 < 6 || pwdLen1 > 16) {
            showToast(R.string.enter_correct_pwd);
            return;
        }
        if (deviceId < 0){
            return;
        }
        showLoadingDialog(R.string.loading_str, false);
        RestRequestApi.addDoorPwd(getActivity(), deviceId, doorlockPwd.getText().toString(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_add_doorlock_pwd, container, false);
        ButterKnife.bind(this, mRootView);
        Bundle bundle = getArguments();
        if(bundle != null){
            deviceId = bundle.getInt("deviceid",-1);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
    }

    @Override
    public boolean goBack() {
        return super.goBack();
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        L.d("loginFail");
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        SmartHomeApp.showToast("请检查网络");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        dismissLoadingDialog();
        if (getActivity() == null) {
            return;
        }
        if (ApiHost.ADD_DOOR_PWD_URL.equals(url)) {
            if (response != null) {
                AddDoorPwdResponse param = (AddDoorPwdResponse) response.body;
                if (null != param) {
                    if (param.isSuccess()) {
                        // TODO: 2016/11/9
                        showToast("开锁密码设置成功");
                        super.onBackPressed();
                    } else if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                        showToast(param.getError().getMessage());
                    } else {
                        showToast("开锁密码设置失败");
                    }
                } else {
                    showToast("开锁密码设置失败");
                }
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        L.d("loginBizFail");
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
