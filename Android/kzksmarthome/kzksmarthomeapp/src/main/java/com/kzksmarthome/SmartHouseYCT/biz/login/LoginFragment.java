package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfLoginSuccess;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.LoginResponse;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.user.UserInfo;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.util.Encrypt;

/**
 * @author jack
 * @version V1.0
 * @Title:
 * @Description: 登陆界面
 * @date 2015年7月22日 上午12:11:51
 */
public class LoginFragment extends BaseRequestFragment implements RequestCallback {


    @BindView(R.id.login_phone_tv)
    TextView loginPhoneTv;
    @BindView(R.id.login_phone_number_edit)
    EditText loginPhoneNumberEdit;
    @BindView(R.id.login_pwd_tv)
    TextView loginPwdTv;
    @BindView(R.id.login_pwd_number_edit)
    EditText loginPwdNumberEdit;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.login_forget_pae)
    TextView loginForgetPae;
    @BindView(R.id.login_goto_register)
    TextView loginGotoRegister;

    /**
     * @param
     * @return void
     * @throws
     * @Description:登陆
     */
    @OnClick(R.id.login_btn)
    void onClickLogin() {
        doLogin();
    }

    /**
     * 短信验证有效时间ms
     */
    private static final int SMS_VALIDITY = 60000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView(){
        loginGotoRegister.setText(Html.fromHtml("没有账号？<font color='#0090FF'>去注册></font>"));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.login_forget_pae, R.id.login_goto_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_forget_pae:
                PageSwitcher.switchToTopNavPage(getActivity(), ResetPassword.class, null, "", getString(R.string.reset_pwd_str),null);
                break;
            case R.id.login_goto_register:
                PageSwitcher.switchToTopNavPage(getActivity(), RegisterInfoFragment.class, null, "", getString(R.string.register_title),null);
                break;
        }
    }

    /**
     * 登陆
     */
    private void doLogin() {
        String phone = loginPhoneNumberEdit.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(phone)) {
            SmartHomeApp.showToast(R.string.phone_is_error);
            return;
        }
        String pwd = loginPwdNumberEdit.getText().toString().trim()
                .replaceAll(" ", "");
        if (TextUtils.isEmpty(pwd)) {
            SmartHomeApp.showToast(R.string.pwd_is_null);
            return;
        }
        showLoadingDialog(R.string.ptr_refreshing, true);
        pwd = Encrypt.md5(pwd);
        RestRequestApi.login(getActivity(), phone, pwd, this);
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
        dismissLoadingDialog();
        if (ApiHost.LOGIN_URL.equals(url)) {
            if (response != null) {
                LoginResponse param = (LoginResponse) response.body;
                if (param != null) {
                    //BusinessStat.getInstance().addStat("login", userInfo.uuid, userInfo.mobile, "1");
                    UserInfo userInfo = new UserInfo();
                    userInfo.token = param.getResult().getToken();
                    SmartHomeAppLib.getUserMgr().saveUser(userInfo);
                    GjjEventBus.getInstance().post(new EventOfLoginSuccess());
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
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
    public void onDestroy() {
        super.onDestroy();
    }
}
