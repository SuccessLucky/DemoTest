package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.apitest;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.LoginResponse;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: ResetPwdTestFragment
 * @Description: 重置密码请求测试界面
 * @date 2016/9/18 16:17
 */
public class ResetPwdTestFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.reset_pwd_phone_et)
    EditText resetPwdPhoneEt;
    @BindView(R.id.reset_pwd_pwd_et)
    EditText resetPwdPwdEt;
    @BindView(R.id.reset_pwd_code_et)
    EditText resetPwdCodeEt;
    @BindView(R.id.reset_pwd_cancel)
    TextView resetPwdCancel;
    @BindView(R.id.reset_pwd_ensure)
    TextView resetPwdEnsure;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.test_layout_reset_pwd, container, false);
        ButterKnife.bind(this, mRootView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        dismissLoadingDialog();
        Log.d("restapi", "重置密码--onFailure");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        Log.d("restapi", "重置密码--onBizSuccess");
        if (ApiHost.LOGIN_URL.equals(url)) {
            dismissLoadingDialog();
            if (response != null) {
                LoginResponse param = (LoginResponse) response.body;
                SmartHomeApp.showToast("请求-->" + param.isSuccess());
                Log.d("restapi", param.toString());
            }
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        dismissLoadingDialog();
        Log.d("restapi", "重置密码--onBizFail");
        //Log.d("restapi", response.body.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.reset_pwd_cancel, R.id.reset_pwd_ensure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_pwd_cancel:
                break;
            case R.id.reset_pwd_ensure:
                String password = resetPwdPwdEt.getText().toString();
                String phone = resetPwdPhoneEt.getText().toString();
                String code = resetPwdCodeEt.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showToast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    showToast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showToast("请输入验证码");
                    return;
                }
                showLoadingDialog(R.string.loading_str, false);
                RestRequestApi.resetPwd(getActivity(), phone, code, password, this);
                break;
        }
    }
}
