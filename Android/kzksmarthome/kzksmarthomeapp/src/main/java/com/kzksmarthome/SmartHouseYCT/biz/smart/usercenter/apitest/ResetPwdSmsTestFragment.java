package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.apitest;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.ResetPwdSmsResponse;
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
 * @Title: ResetPwdSmsTestFragment
 * @Description: 重置密码验证码短信请求测试界面
 * @date 2016/9/18 16:17
 */
public class ResetPwdSmsTestFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.reset_pwd_sms_et)
    EditText reset_pwdSmsEt;
    @BindView(R.id.reset_pwd_sms_cancel)
    TextView reset_pwdSmsCancel;
    @BindView(R.id.reset_pwd_sms_ensure)
    TextView reset_pwdSmsEnsure;
    @BindView(R.id.reset_pwd_sms_btn_ll)
    LinearLayout reset_pwdSmsBtnLl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.test_layout_reset_pwd_sms, container, false);
        ButterKnife.bind(this, mRootView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        dismissLoadingDialog();
        Log.d("restapi", "密码重置短信--onFailure");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if(getActivity() == null ){
            return;
        }
        Log.d("restapi", "密码重置短信--onBizSuccess");
        if (ApiHost.RESET_PWD_SMS_URL.equals(url)) {
            dismissLoadingDialog();
            if (response != null) {
                ResetPwdSmsResponse param = (ResetPwdSmsResponse) response.body;
                SmartHomeApp.showToast("请求-->"+param.isSuccess());
                Log.d("restapi", param.toString());
            }
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        dismissLoadingDialog();
        Log.d("restapi", "密码重置短信--onBizFail");
        //Log.d("restapi", response.body.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.reset_pwd_sms_cancel, R.id.reset_pwd_sms_ensure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_pwd_sms_cancel:
                break;
            case R.id.reset_pwd_sms_ensure:
                String phone = reset_pwdSmsEt.getText().toString();
                if(TextUtils.isEmpty(phone) || phone.length() != 11){
                    showToast("请输入11位的手机号");
                    return;
                }
                showLoadingDialog(R.string.loading_str, false);
                RestRequestApi.resetPwdSms(getActivity(), phone, this);
                break;
        }
    }
}
