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
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.RegisterSmsResponse;
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
 * @Title: RestApiTestFragment
 * @Description: 发送注册验证码请求测试界面
 * @date 2016/9/18 16:17
 */
public class RegisterSmsTestFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.register_sms_et)
    EditText registerSmsEt;
    @BindView(R.id.register_sms_cancel)
    TextView registerSmsCancel;
    @BindView(R.id.register_sms_ensure)
    TextView registerSmsEnsure;
    @BindView(R.id.register_sms_btn_ll)
    LinearLayout registerSmsBtnLl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.test_layout_register_sms, container, false);
        ButterKnife.bind(this, mRootView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        dismissLoadingDialog();
        Log.d("restapi", "注册短信--onFailure");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if(getActivity() == null ){
            return;
        }
        Log.d("restapi", "注册短信--onBizSuccess");
        if (ApiHost.REGISTER_SMS_URL.equals(url)) {
            dismissLoadingDialog();
            if (response != null) {
                RegisterSmsResponse param = (RegisterSmsResponse) response.body;
                SmartHomeApp.showToast("请求-->"+(param == null));
                SmartHomeApp.showToast("请求-->"+(param.toString()));
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
        Log.d("restapi", "注册短信--onBizFail");
        //Log.d("restapi", response.body.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.register_sms_cancel, R.id.register_sms_ensure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_sms_cancel:
                break;
            case R.id.register_sms_ensure:
                String phone = registerSmsEt.getText().toString();
                if(TextUtils.isEmpty(phone) || phone.length() != 11){
                    showToast("请输入11位的注册手机号");
                    return;
                }
                showLoadingDialog(R.string.loading_str, false);
                RestRequestApi.registerSms(getActivity(), phone, this);
                break;
        }
    }
}
