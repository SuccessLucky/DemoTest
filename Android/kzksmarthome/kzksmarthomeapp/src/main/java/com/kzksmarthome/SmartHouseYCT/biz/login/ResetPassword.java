package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.ResetPwdResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.ResetPwdSmsResponse;
import com.kzksmarthome.common.biz.widget.EditTextWithDel;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: ResetPassword
 * @Description: 找回密码
 * @date 2016/9/19 22:12
 */
public class ResetPassword extends BaseRequestFragment implements RequestCallback {

    private static final int SMS_VALIDITY = 60000;// 短信验证有效时间ms

    @BindView(R.id.reset_pwd_phone_et)
    EditTextWithDel resetPwdPhoneEt;
    @BindView(R.id.send_again_btn)
    Button resetPwdSendAgainBtn;
    @BindView(R.id.sms_edit)
    EditTextWithDel resetPwdCodeEt;
    @BindView(R.id.reset_pwd_newpwd_et)
    EditTextWithDel resetPwdNewpwdEt;
    @BindView(R.id.commit_psw)
    Button commitPsw;

    private String mPhone;// mobile
    private String mCode;// mobile
    private String mPwd;
    private Counter mCounter;

    @OnClick(R.id.send_again_btn)
    void onGetSms() {
        mPhone = resetPwdPhoneEt.getText().toString();
        if (TextUtils.isEmpty(mPhone)) {
            SmartHomeApp.showToast(R.string.enter_mobile_error);
            return;
        }
        if (!Util.isMobileNO(mPhone)) {
            SmartHomeApp.showToast(R.string.enter_mobile);
            return;
        }
        TextView smsET = resetPwdCodeEt;
        smsET.setText("");
        smsET.setEnabled(true);
        smsET.requestFocus();
        sendResetPwdSms(mPhone);
    }

    @OnClick(R.id.commit_psw)
    void onResetPwd() {
        mPhone = resetPwdPhoneEt.getText().toString();
        mCode = resetPwdCodeEt.getText().toString();
        mPwd = resetPwdNewpwdEt.getText().toString();
        if (TextUtils.isEmpty(mPhone)) {
            SmartHomeApp.showToast(R.string.enter_mobile_error);
            return;
        }
        if (TextUtils.isEmpty(mCode)) {
            SmartHomeApp.showToast(R.string.enter_sms);
            return;
        }
        if (TextUtils.isEmpty(mPwd)) {
            SmartHomeApp.showToast(R.string.enter_new_pwd);
            return;
        }
        int pwdLen = mPwd.length();
        if (pwdLen < 6 || pwdLen > 16) {
            showToast(R.string.enter_correct_pwd);
            return;
        }
        resetPwd(mPhone, mCode, mPwd);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_reset_psw, container, false);
        ButterKnife.bind(this, mRootView);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        /*Bundle arguments = getArguments();
        if (null == arguments) {
            getActivity().onBackPressed();
            return;
        }*/
    }

    private void initView() {
        enableGetSmsBtn(null);
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
        Log.d("restapi", "重置密码--onBizSuccess--response == null-->" + (response == null));
        if (ApiHost.RESET_PWD_URL.equals(url)) {
            dismissLoadingDialog();
            if (response != null) {
                ResetPwdResponse param = (ResetPwdResponse) response.body;
                Log.d("restapi", param.toString());
                if (null != param) {
                    if (param.isSuccess()) {
                        showToast(R.string.reset_pwd_ok_tip);
                        // TODO: 2016/9/22
                        //PageSwitcher.switchToTopNavPage(getActivity(), LoginActivity.class, null, "", getString(R.string.start_login_str), null);
                        getActivity().finish();
                    }else if(null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())){
                        showToast(param.getError().getMessage());
                    }else{
                        showToast("密码重置失败");
                    }
                }else{
                    showToast("密码重置失败");
                }
            }
        } else if (ApiHost.RESET_PWD_SMS_URL.equals(url)) {
            dismissLoadingDialog();
            if (response != null) {
                ResetPwdSmsResponse param = (ResetPwdSmsResponse) response.body;
                Log.d("restapi", param.toString());
                if (null != param) {
                    if (param.isSuccess()) {
                        showToast("验证码已发送至您手机");
                    }else if(null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())){
                        showToast(param.getError().getMessage());
                    }else{
                        showToast("验证码发送失败");
                    }
                }else{
                    showToast("验证码发送失败");
                }
            }
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        dismissLoadingDialog();
        Log.d("restapi", "重置密码--onBizFail");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /* 定义一个倒计时的内部类 */
    class Counter extends CountDownTimer {
        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if (getActivity() == null) {
                return;
            }
            enableGetSmsBtn(SmartHomeApp.getInstance().getString(R.string.send_sms_again));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (getActivity() == null) {
                return;
            }
            resetPwdSendAgainBtn.setText(sendSmsCountDown(millisUntilFinished / 1000l));
        }
    }

    /**
     * 获取回密码验证码请求
     */
    private void sendResetPwdSms(String phone) {
        RestRequestApi.resetPwdSms(getActivity(), phone, this);
    }

    /**
     * 找回密码
     */
    private void resetPwd(String phone, String code, String password) {
        showLoadingDialog(R.string.reset_pwd_tip, false);
        RestRequestApi.resetPwd(getActivity(), phone, code, password, this);
    }

    /**
     * 注册成功
     */
    private void registerSucceed() {
        Intent i = new Intent();
        // if (GjjApp.getInstance().getLockPatternUtils().savedPatternExists()) {
        // i.setClass(getActivity(), UnlockGesturePasswordActivity.class);
        // } else {
        // i.setClass(getActivity(), GuideGesturePasswordActivity.class);
        // }
        i.setClass(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();

    }

    /**
     * 短信按钮不可点击
     *
     * @param text
     */
    private void disableGetSmsBtn(String text) {
        Button getSmsBtn = this.resetPwdSendAgainBtn;
        getSmsBtn.setEnabled(false);
        if (null != text) {
            getSmsBtn.setText(text);
        }
        getSmsBtn.setTextColor(getResources().getColor(R.color.secondary_gray));
    }

    /**
     * 短信按钮可点击
     *
     * @param text
     */
    private void enableGetSmsBtn(String text) {
        Button getSmsBtn = this.resetPwdSendAgainBtn;
        getSmsBtn.setEnabled(true);
        if (null != text) {
            getSmsBtn.setText(text);
        }
        getSmsBtn.setTextColor(getResources().getColor(R.color.orange));
    }

    /**
     * 短信重新发送倒计时
     */
    private void countDownSms() {
        disableGetSmsBtn(null);
        resetPwdCodeEt.setEnabled(true);
        Counter counter = mCounter;
        if (counter != null) {
            counter.cancel();
        }
        counter = new Counter(SMS_VALIDITY, 1000l);
        mCounter = counter;
        counter.start();
    }

    private String sendSmsCountDown(long ms) {
        return String.format(getResources().getString(R.string.send_sms_again) + "(%s)", ms);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCounter != null) {
            mCounter.cancel();
        }
    }
}
