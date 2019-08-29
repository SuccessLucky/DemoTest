package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.RegisterSmsResponse;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class RegisterInfoFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.next_step)
    Button mNextStepBtn;
    @BindView(R.id.register_name)
    EditText mRegisterNameET;
    @BindView(R.id.register_nickname)
    EditText mRegisterNicknameET;
    @BindView(R.id.register_psw)
    EditText mRegisterPwdET;

    private String mobile = null;

    @OnClick(R.id.next_step)
    void onNextStep() {

        mobile = mRegisterNameET.getText().toString();
        if (!Util.isMobileNO(mobile)) {
            showToast(R.string.enter_mobile_error);
            return;
        }
        if (TextUtils.isEmpty(mRegisterNicknameET.getText().toString())) {
            showToast(R.string.enter_nickname);
            return;
        }
        if (TextUtils.isEmpty(mRegisterPwdET.getText().toString())) {
            showToast(R.string.enter_register_pwd);
            return;
        }
        int pwdLen = mRegisterPwdET.getText().toString().length();
        if (pwdLen < 6 || pwdLen > 16) {
            showToast(R.string.enter_correct_pwd);
            return;
        }
        sendGetSmsReq(mobile);
    }

    /**
     * 获取短信验证码请求
     */
    private void sendGetSmsReq(String mobile) {
        RestRequestApi.registerSms(getActivity(), mobile, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_register_info, container, false);
        ButterKnife.bind(this, mRootView);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mRegisterPwdET.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onNextStep();
                return false;
            }
        });
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
        if (getActivity() == null) {
            return;
        }
        if (ApiHost.REGISTER_SMS_URL.equals(url)) {
            if (response != null) {
                RegisterSmsResponse param = (RegisterSmsResponse) response.body;
                if (null != param) {
                    if (param.isSuccess()) {
                        Bundle infoBundle = new Bundle();
                        infoBundle.putString(BundleKeyConstant.KEY_REGISTER_NAME, mobile);
                        infoBundle.putString(BundleKeyConstant.KEY_REGISTER_NICKNAME, mRegisterNicknameET.getText().toString());
                        infoBundle.putString(BundleKeyConstant.KEY_REGISTER_PWD, mRegisterPwdET.getText().toString());
                        PageSwitcher.switchToTopNavPage(getActivity(), RegisterSubmitFragment.class, infoBundle, R.string.back_btn, R.string.register, 0);
                    }else if(null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())){
                        showToast(param.getError().getMessage());
                    }else{
                        showToast("注册验证码发送失败");
                    }
                }else{
                    showToast("注册验证码发送失败");
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
        /*if (response != null && response.header != null && response.header.prompt != null) {
            SmartHomeApp.showToast(response.header.prompt);
        }*/
    }
}
