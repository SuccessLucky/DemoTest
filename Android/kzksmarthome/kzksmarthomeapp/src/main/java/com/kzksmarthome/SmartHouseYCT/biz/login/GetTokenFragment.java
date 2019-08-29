package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.util.Util;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Chuck on 2015/12/22.
 */
public class GetTokenFragment extends BaseRequestFragment implements RequestCallback {

    private static final int SMS_VALIDITY = 60000;// 短信验证有效时间ms

    @BindView(R.id.sms_edit)
    EditText mSmsET;
    @BindView(R.id.send_again_btn)
    Button getSmsBtn;


    @BindView(R.id.sms_tip)
    TextView mSmsTipTV;

    @OnClick(R.id.send_again_btn)
    void onGetSms() {
        if (!Util.isMobileNO(mRegisterName)) {
            SmartHomeApp.showToast(R.string.mobile_error);
            return;
        }
        TextView smsET = mSmsET;
        smsET.setText("");
        smsET.setEnabled(true);
        smsET.requestFocus();
        sendGetSmsReq();
    }

    @OnClick(R.id.btn_next_step)
    void onCheckToken() {
        if (TextUtils.isEmpty(mSmsET.getText().toString())) {
            SmartHomeApp.showToast(R.string.enter_sms);
            return;
        }

        verifyRetrieve();
        showLoadingDialog(R.string.check_token_tip, false);
        //FindPswSucceed();
    }

    private String mRegisterName;// mobile
    private Counter mCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_get_token, container, false);
        ButterKnife.bind(this, mRootView);


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
        countDownSms();
        mSmsTipTV.setText(getString(R.string.send_sms_tip, mRegisterName));
    }

    private void initView() {
        enableGetSmsBtn(null);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {

    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {

    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {

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
       /* Request req = GjjRequestFactory.applyRetrieve(IdType.ID_TYPE_MOBILE.getValue(), mRegisterName);
        GjjRequestManager.getInstance().execute(req, this);
        disableGetSmsBtn(null);
        mSmsTipTV.setText(getString(R.string.send_sms_ing_tip, mRegisterName));*/
    }

    /**
     * 注册
     */
    private void verifyRetrieve() {
        /*StringBuilder stringBuilder = Util.getThreadSafeStringBuilder();
        stringBuilder.append(mRegisterName).append(":").append(mSmsET.getText().toString());
        Request req = GjjRequestFactory.verifyRetrieve(IdType.ID_TYPE_MOBILE.getValue(), stringBuilder.toString());
        GjjRequestManager.getInstance().execute(req, this);*/
    }

   /* @Override
    public void onRequestFinished(Request request, Bundle resultData) {
        if (getActivity() == null) {
            return;
        }
        String requestType = request.getRequestType();
        if (UserApiConstants.CMD_VERIFY_RETRIEVE.equals(requestType)) {
            dismissLoadingDialog();
            final VerifyRetrieveRsp rsp = (VerifyRetrieveRsp) resultData
                    .getSerializable(GjjOperationFactory.RSP_BODY);
            if (rsp != null) {
                FindPswSucceed(rsp.str_mobile_new_token);
            }
        } else if(UserApiConstants.CMD_APPLY_RETRIEVE.equals(requestType)) {
            countDownSms();
            mSmsTipTV.setText(getString(R.string.send_sms_tip, mRegisterName));
        }
    }

    @Override
    public void onRequestError(Request request, Bundle resultData, int statusCode, int errorType) {
        L.d("onRequestError: statusCode-%s, errorType-%s", statusCode, errorType);
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        String requestType = request.getRequestType();
        final Header header = (Header) resultData.getSerializable(GjjOperationFactory.RSP_HEADER);
        if (UserApiConstants.CMD_VERIFY_RETRIEVE.equals(requestType)) {
            // 验证失败处理
            if (header != null && !TextUtils.isEmpty(header.str_prompt)) {
                showToast(header.str_prompt);
            } else if (statusCode == ApiConstants.ClientErrorCode.ERROR_NETWORK_UNAVAILABLE.getCode()) {
                showToast(getString(R.string.network_unavailable));
            } else {
                showToast(R.string.token_error);
            }
        } else if(UserApiConstants.CMD_APPLY_RETRIEVE.equals(requestType)) {
            // 发送短信验证失败处理
            if (header != null && !TextUtils.isEmpty(header.str_prompt)) {
                showToast(header.str_prompt);
            } else if (statusCode == ApiConstants.ClientErrorCode.ERROR_NETWORK_UNAVAILABLE.getCode()) {
                showToast(getString(R.string.network_unavailable));
            } else {
                showToast(R.string.get_sms_failed);
            }
        }
    }
*/
    /**
     * 验证成功
     */
    private void FindPswSucceed(String token) {
     /*   Bundle bundle = new Bundle();
        bundle.putString(BundleKeyConstant.KEY_REGISTER_NAME, mRegisterName);
        bundle.putString(BundleKeyConstant.KEY_FINE_PSW_TOKEN, token);
        PageSwitcher.switchToTopNavPage(getActivity(), ResetPassword.class, bundle, R.string.back_btn, R.string.find_psw, 0);*/
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
        return String.format(SmartHomeApp.getInstance().getString(R.string.send_sms_again) + "(%s)", ms);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCounter != null) {
            mCounter.cancel();
        }
    }

}
