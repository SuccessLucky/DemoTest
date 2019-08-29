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
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.RegisterResponse;
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
 * @Description: 注册请求测试界面
 * @date 2016/9/18 16:17
 */
public class RegisterTestFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.register_phone_et)
    EditText registerPhoneEt;
    @BindView(R.id.register_nickname_et)
    EditText registerNicknameEt;
    @BindView(R.id.register_pwd_et)
    EditText registerPwdEt;
    @BindView(R.id.register_code_et)
    EditText registerCodeEt;
    @BindView(R.id.register_cancel)
    TextView registerCancel;
    @BindView(R.id.register_ensure)
    TextView registerEnsure;
    @BindView(R.id.register_btn_ll)
    LinearLayout registerBtnLl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.test_layout_register, container, false);
        ButterKnife.bind(this, mRootView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        dismissLoadingDialog();
        Log.d("restapi", "注册--onFailure");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        Log.d("restapi", "注册--onBizSuccess");
        if (ApiHost.REGISTER_URL.equals(url)) {
            dismissLoadingDialog();
            if (response != null) {
                RegisterResponse param = (RegisterResponse) response.body;
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
        Log.d("restapi", "注册--onBizFail");
        //Log.d("restapi", response.body.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.register_cancel, R.id.register_ensure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_cancel:
                break;
            case R.id.register_ensure:
                String phone = registerPhoneEt.getText().toString();
                String password = registerPwdEt.getText().toString();
                String nickname = registerNicknameEt.getText().toString();
                String code = registerCodeEt.getText().toString();
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    showToast("请输入11位的注册手机号");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(nickname)) {
                    showToast("请输入昵称");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showToast("请输入验证码");
                    return;
                }
                showLoadingDialog(R.string.loading_str, false);
                RestRequestApi.register(getActivity(), phone, code, password, nickname, this);
                break;
        }
    }
}
