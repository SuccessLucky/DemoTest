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
 * @Title: RestApiTestFragment
 * @Description: 登陆请求测试界面
 * @date 2016/9/18 16:17
 */
public class LoginTestFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.login_nickname_et)
    EditText loginNicknameEt;
    @BindView(R.id.login_pwd_et)
    EditText loginPwdEt;
    @BindView(R.id.login_cancel)
    TextView loginCancel;
    @BindView(R.id.login_ensure)
    TextView loginEnsure;
    @BindView(R.id.login_btn_ll)
    LinearLayout loginBtnLl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.test_layout_login, container, false);
        ButterKnife.bind(this, mRootView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        dismissLoadingDialog();
        Log.d("restapi", "登陆--onFailure");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        Log.d("restapi", "登陆--onBizSuccess--response == null-->"+(response == null));
        if (ApiHost.LOGIN_URL.equals(url)) {
            dismissLoadingDialog();
            if (response != null) {
                LoginResponse param = (LoginResponse) response.body;
                Log.d("restapi", "请求-->"+(param == null));
                Log.d("restapi", "请求-->"+(param.toString()));
                Log.d("restapi", "请求-->" + param.isSuccess());
                Log.d("restapi", param.toString());
            }
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        dismissLoadingDialog();
        Log.d("restapi", "登陆--onBizFail");
        //Log.d("restapi", response.body.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.login_cancel, R.id.login_ensure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_cancel:
                break;
            case R.id.login_ensure:
                String password = loginPwdEt.getText().toString();
                String nickname = loginNicknameEt.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showToast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(nickname)) {
                    showToast("请输入昵称");
                    return;
                }
                showLoadingDialog(R.string.loading_str, false);
                RestRequestApi.login(getActivity(), nickname, password, this);
                break;
        }
    }
}
