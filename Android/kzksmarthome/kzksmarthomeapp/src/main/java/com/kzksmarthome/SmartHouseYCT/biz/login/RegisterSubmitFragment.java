package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfLoginSuccess;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSelectGw;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.LoginResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.RegisterResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.RegisterSmsResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.SettingIOTRadeyActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.GwSwitchFragment;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.util.AndroidUtil;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.user.UserInfo;
import com.squareup.okhttp.Request;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.util.Encrypt;

public class RegisterSubmitFragment extends BaseRequestFragment implements RequestCallback {

    private static final int SMS_VALIDITY = 60000;// 短信验证有效时间ms

    @BindView(R.id.sms_edit)
    EditText mSmsET;
    @BindView(R.id.send_again_btn)
    Button getSmsBtn;
    @BindView(R.id.btn_register)
    Button registerBtn;
    @BindView(R.id.agree_tv)
    TextView mAgreeTV;

    @BindView(R.id.sms_tip)
    TextView mSmsTipTV;

    @OnClick(R.id.send_again_btn)
    void onGetSms() {
        if (!Util.isMobileNO(mRegisterName)) {
            SmartHomeApp.showToast(R.string.enter_mobile_error);
            return;
        }
        TextView smsET = mSmsET;
        smsET.setText("");
        smsET.setEnabled(true);
        smsET.requestFocus();
        //sendGetSmsReq();
    }

    @OnClick(R.id.btn_register)
    void onRegister() {
        if (TextUtils.isEmpty(mSmsET.getText().toString())) {
            SmartHomeApp.showToast(R.string.enter_sms);
            return;
        }
        sendRegisterReq();
        showLoadingDialog(R.string.login_ing_tip, false);

    }

    private String mRegisterName;// mobile
    private String mRegisterNickname;
    private String mRegisterPwd;
    private Counter mCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_register_submit, container, false);
        ButterKnife.bind(this, mRootView);
        GjjEventBus.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        Bundle arguments = getArguments();
        if (null == arguments) {
            getActivity().onBackPressed();
            return;
        }
        mRegisterName = arguments.getString(BundleKeyConstant.KEY_REGISTER_NAME);
        mRegisterNickname = arguments.getString(BundleKeyConstant.KEY_REGISTER_NICKNAME);
        mRegisterPwd = arguments.getString(BundleKeyConstant.KEY_REGISTER_PWD);

        mSmsTipTV.setText(String.format(getResources().getString(R.string.send_sms_tip), mRegisterName));
        //sendGetSmsReq();
    }

    private void initView() {
        enableGetSmsBtn(null);
        viewAgreement();
    }

    private void viewAgreement() {
        SpannableString spannableInfo = new SpannableString(getString(R.string.agree_text));
        ClickableSpan clickable = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // 查看协议
                PageSwitcher.switchToTopNavPage(getActivity(), UserAgreeFragment.class, null, R.string.back_btn, R.string.user_agreement, 0);
            }

        };
        int totalLen = spannableInfo.length();
        int tipLen = totalLen - 7;
        ForegroundColorSpan blue = new ForegroundColorSpan(getResources().getColor(R.color.orange));
        spannableInfo.setSpan(clickable, tipLen, totalLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableInfo.setSpan(blue, tipLen, totalLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableInfo.setSpan(new UnderlineSpan(), tipLen, totalLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView agreeTV = mAgreeTV;
        agreeTV.setText(spannableInfo);
        agreeTV.setMovementMethod(LinkMovementMethod.getInstance());
        agreeTV.setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    public void onEventMainThread(EventOfResultSelectGw data) {
        GatewayInfo gwInfo = data.gwInfo;
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        userInfo.gateway = gwInfo.getMac_address();
        userInfo.role = gwInfo.getMember_type();
        userInfo.uuid = AndroidUtil.getDeviceIMEI(getActivity().getApplication());
        SmartHomeAppLib.getUserMgr().saveUser(userInfo);
        GjjEventBus.getInstance().post(new EventOfLoginSuccess());
        Intent intent = new Intent();
        intent.setClass(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
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
            getSmsBtn.setText(sendSmsCountDown(millisUntilFinished / 1000l));
        }
    }

    /**
     * 获取短信验证码请求
     */
    private void sendGetSmsReq() {
        RestRequestApi.registerSms(getActivity(), mRegisterName, this);
    }

    /**
     * 注册
     */
    private void sendRegisterReq() {
        RestRequestApi.register(getActivity(), mRegisterName, mSmsET.getText().toString(), mRegisterPwd, mRegisterNickname, this);
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
        Button getSmsBtn = this.getSmsBtn;
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
        Button getSmsBtn = this.getSmsBtn;
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
        mSmsET.setEnabled(true);
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

    /**
     * 登陆
     */
    private void doLogin() {
        mRegisterPwd = Encrypt.md5(mRegisterPwd);
        RestRequestApi.login(getActivity(), mRegisterName, mRegisterPwd, this);
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

        if (ApiHost.LOGIN_URL.equals(url)) {
            dismissLoadingDialog();
            if (response != null) {
                LoginResponse param = (LoginResponse) response.body;
                if (null != param) {
                    if (param.isSuccess()) {
                        //BusinessStat.getInstance().addStat("login", userInfo.uuid, userInfo.mobile, "1");
                        UserInfo userInfo = new UserInfo();
                        userInfo.token = param.getResult().getToken();
                        userInfo.password = mRegisterPwd;
                        userInfo.nickname = mRegisterName;
                        userInfo.uuid = AndroidUtil.getDeviceIMEI(getActivity().getApplication());
                        SmartHomeAppLib.getUserMgr().saveUser(userInfo);
                        if(null != param.getResult().getUser_gateways() && param.getResult().getUser_gateways().size() > 0){
                            if(param.getResult().getUser_gateways().size() == 1){
                                GatewayInfo gwInfo = param.getResult().getUser_gateways().get(0);
                                userInfo = SmartHomeAppLib.getUserMgr().getUser();
                                userInfo.gateway = gwInfo.getMac_address();
                                userInfo.role = gwInfo.getMember_type();
                                userInfo.uuid = AndroidUtil.getDeviceIMEI(getActivity().getApplication());
                                SmartHomeAppLib.getUserMgr().saveUser(userInfo);
                                GjjEventBus.getInstance().post(new EventOfLoginSuccess());
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }else{
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("gwlist", (Serializable) param.getResult().getUser_gateways());
                                PageSwitcher.switchToTopNavPage(getActivity(), GwSwitchFragment.class, bundle, "", "选择网关", "确定", -1, "");
                            }
                        }else{
                            Intent intent = new Intent(getActivity(), SettingIOTRadeyActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }else if(null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())){
                        showToast(param.getError().getMessage());
                    }else{
                        showToast("注册失败");
                    }
                }else{
                    showToast("注册失败");
                }
            }
        }else if (ApiHost.REGISTER_URL.equals(url)) {
            if (response != null) {
                RegisterResponse param = (RegisterResponse) response.body;
                if (null != param) {
                    if (param.isSuccess()) {
                        doLogin();
                        //PageSwitcher.switchToTopNavPage(getActivity(), LoginFragment.class, null, 0, 0, 0);
                    }else if(null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())){
                        showToast(param.getError().getMessage());
                    }else{
                        showToast("注册失败");
                    }
                }else{
                    showToast("注册失败");
                }
            }
        }else if (ApiHost.REGISTER_SMS_URL.equals(url)) {
            if (response != null) {
                RegisterSmsResponse param = (RegisterSmsResponse) response.body;
                if (null != param) {
                    if (param.isSuccess()) {
                        mSmsTipTV.setText(String.format(getResources().getString(R.string.send_sms_tip), mRegisterName));
                        mSmsTipTV.setVisibility(View.VISIBLE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(this);
        if (mCounter != null) {
            mCounter.cancel();
        }
    }
}
