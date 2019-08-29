package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfLoginSuccess;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSelectGw;
import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddGwResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.LoginResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.SettingIOTRadeyActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.GwSwitchFragment;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.biz.widget.AndroidBug5497Workaround;
import com.kzksmarthome.common.biz.widget.CustomProgressDialog;
import com.kzksmarthome.common.biz.widget.YScrollLinearLayout;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.tcp.TCPMgr;
import com.kzksmarthome.common.lib.util.AndroidUtil;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.net.HttpCache;
import com.kzksmarthome.common.module.user.UserInfo;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.util.Encrypt;

public class LoginActivity extends Activity implements RequestCallback, AndroidBug5497Workaround.OnKeyboardListener {

    @BindView(R.id.login_name)
    EditText mAccountET;

    @BindView(R.id.login_identify_code)
    EditText mPwdET;

    @BindView(R.id.top_layout)
    YScrollLinearLayout mContentLayout;

    @BindView(R.id.imageView1)
    ImageView mIconImg;

  /*  @BindView(R.id.close)
    ImageView mCloseIV;*/
    /**
     * 去注册
     */
    @BindView(R.id.go_register)
    TextView mGoRegisterTV;


    @BindView(R.id.forget_psw)
    TextView mForgetPswTV;

    private CustomProgressDialog mLoginDialog;
    private InputMethodManager mInputMethodManager;
//    private boolean mIsBackPressed = false;
    private int[] mIconImgLocation;

    private String pwd;
    private String nickname;

   /* @OnClick(R.id.close)
    void close() {
        ReportStat.getInstance().addClickReport(302);
        finish();
    }*/

    @OnClick(R.id.forget_psw)
    void forgetPsw() {
        hideKeyboardForCurrentFocus();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = new Bundle();
        String un = mAccountET.getText().toString();
        if (Util.isMobileNO(un) || Util.isEmail(un)) {
            bundle.putString(BundleKeyConstant.KEY_REGISTER_NAME, un);
        }
        PageSwitcher.switchToTopNavPage(this, ResetPassword.class, bundle, getString(R.string.back_btn), getString(R.string.find_psw),
                null);
    }

    @OnClick(R.id.btn_login)
    void login() {
        nickname = mAccountET.getText().toString();
        if (!Util.isMobileNO(nickname) && !Util.isEmail(nickname)) {
            Toast.makeText(this, R.string.enter_mobile_error, Toast.LENGTH_LONG).show();
            SmartHomeApp.showToast(R.string.enter_mobile_error);
            return;
        }

        pwd = mPwdET.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            SmartHomeApp.showToast(R.string.enter_register_pwd);
            return;
        }
        hideKeyboardForCurrentFocus();
        //登录
        pwd = Encrypt.md5(pwd);
        RestRequestApi.login(this, nickname, pwd, this);
        SharedPreferences sharedPreferences = SmartHomeAppLib.getInstance().getPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cmw_userName",nickname);
        editor.commit();
        CustomProgressDialog loginDialog = mLoginDialog;
        if (null == loginDialog) {
            loginDialog = new CustomProgressDialog(this);
            mLoginDialog = loginDialog;
            loginDialog.setTipText(R.string.login_ing_tip);
            loginDialog.setCancelable(false);
            loginDialog.setCanceledOnTouchOutside(false);
            loginDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    onBackPressed();
                }
            });
        }
        loginDialog.show();
    }

    @OnClick(R.id.root_rl)
    void onRootClick() {
        hideKeyboardForCurrentFocus();
    }

    @OnClick(R.id.go_register)
    void goRegister() {
        ApiHost.NETWORK_ISREMOTE = false;
        TCPMgr.getInstance().closeConnect();
        hideKeyboardForCurrentFocus();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PageSwitcher.switchToTopNavPage(this, RegisterInfoFragment.class, null, getString(R.string.back_btn), getString(R.string.register), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        AndroidBug5497Workaround.assistActivity(this);
        ButterKnife.bind(this);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        //viewReserve();
        initUser();
        GjjEventBus.getInstance().register(this);
       // GjjEventBus.getInstance().register(this);
        L.d("LoginActivity TaskId %s", this.getTaskId());
    }

    public void onEventMainThread(EventOfResultSelectGw data) {
        GatewayInfo gwInfo = data.gwInfo;
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        userInfo.gateway = gwInfo.getMac_address();
        userInfo.role = gwInfo.getMember_type();
        userInfo.uuid = AndroidUtil.getDeviceIMEI(getApplication());
        SmartHomeAppLib.getUserMgr().saveUser(userInfo);
        GjjEventBus.getInstance().post(new EventOfLoginSuccess());
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 初始化去注册
     */
    public void viewReserve() {
        SpannableString spannableInfo = new SpannableString(getString(R.string.goto_register));
        int totalLen = spannableInfo.length();
        int tipLen = totalLen - 5;
        ForegroundColorSpan blue = new ForegroundColorSpan(getResources().getColor(R.color.gold_color));
        spannableInfo.setSpan(blue, tipLen, totalLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableInfo.setSpan(new UnderlineSpan(), tipLen, totalLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView goRegisterTV = mGoRegisterTV;
        goRegisterTV.setText(spannableInfo);
        goRegisterTV.setMovementMethod(LinkMovementMethod.getInstance());
        goRegisterTV.setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    private void initView() {
        String userName = SmartHomeAppLib.getInstance().getPreferences().getString("cmw_userName","");
        mAccountET.setText(userName);
        mPwdET.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                login();
                return false;
            }
        });

    }



    private void initUser() {
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (userInfo != null) {
                if (!TextUtils.isEmpty(userInfo.email)) {
                    mAccountET.setText(userInfo.email);
                }
                if (!TextUtils.isEmpty(userInfo.mobile)) {
                    mAccountET.setText(userInfo.mobile);
                }
            Selection.setSelection(mAccountET.getText(), mAccountET.length());
        }
    }

    /**
     * 登录成功
     */
    private void loginSucceed() {

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setAttributes(params);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void hideKeyboardForCurrentFocus() {
        if (getCurrentFocus() != null) {
            mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onShow(boolean showKeyboard, int heightDifference) {
        int[] iconImgLocation = mIconImgLocation;
        if (iconImgLocation == null) {
            iconImgLocation = new int[2];
            mIconImgLocation = iconImgLocation;
            mIconImg.getLocationOnScreen(iconImgLocation);
        }
        int tm = iconImgLocation[1] + mIconImg.getHeight();
        if (tm > heightDifference) {
            tm = heightDifference;
        }
        if (showKeyboard) {
            mContentLayout.yScrollTo(tm, 300);
        } else {
            mContentLayout.yScrollTo(0, 300);
        }
    }

    /**
     * 关闭登录提示框
     */
    private void dismissProgressDialog() {
        if (null != mLoginDialog) {
            mLoginDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        GjjEventBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        L.d("loginFail");
        if (this.isFinishing()) {
            return;
        }
        dismissProgressDialog();
        SmartHomeApp.showToast("请检查网络");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (this.isFinishing()) {
            return;
        }
        dismissProgressDialog();
        if (ApiHost.LOGIN_URL.equals(url)) {
            if (response != null) {
                LoginResponse param = (LoginResponse) response.body;
                if (param != null) {
                    if(param.isSuccess()){
                        System.out.println("--------->>"+param.toString());
                        ApiHost.NETWORK_ISREMOTE = false;
                        TCPMgr.getInstance().closeConnect();//登录成功断掉原来的连接
                        SmartHomeAppLib.getInstance().getPreferences().edit()
                                .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, null)
                                .commit();
                        SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC,null).commit();
                        //BusinessStat.getInstance().addStat("login", userInfo.uuid, userInfo.mobile, "1");
                        UserInfo userInfo = new UserInfo();
                        userInfo.token = param.getResult().getToken();
                        userInfo.password = pwd;
                        userInfo.nickname = nickname;
                        userInfo.uuid = AndroidUtil.getDeviceIMEI(getApplication());
                        SmartHomeAppLib.getUserMgr().saveUser(userInfo);
                        HttpCache.clearCache();
                        if(null != param.getResult().getUser_gateways() && param.getResult().getUser_gateways().size() > 0){
                            if(param.getResult().getUser_gateways().size() == 1){
                                GatewayInfo gwInfo = param.getResult().getUser_gateways().get(0);
                                userInfo = SmartHomeAppLib.getUserMgr().getUser();
                                userInfo.gateway = gwInfo.getMac_address();
                                userInfo.uuid = AndroidUtil.getDeviceIMEI(getApplication());
                                if(userInfo.gateway != null) {
                                    SmartHomeAppLib.getInstance().getPreferences().edit()
                                            .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, userInfo.gateway)
                                            .commit();
                                    SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC,gwInfo.getWifi_mac_address()).commit();
                                }
                                userInfo.role = gwInfo.getMember_type();
                                SmartHomeAppLib.getUserMgr().saveUser(userInfo);
                                GjjEventBus.getInstance().post(new EventOfLoginSuccess());
                                SmartHomeApp.getInstance().getIOTInfoNew(false,5);//连接网关
                                Intent intent = new Intent();
                                intent.setClass(this, MainActivity.class);
                                startActivity(intent);
                                this.finish();
                            }else{
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("gwlist", (Serializable) param.getResult().getUser_gateways());
                                PageSwitcher.switchToTopNavPage(LoginActivity.this, GwSwitchFragment.class, bundle, "", "选择网关", "确定", -1, "");
                            }
                        }else{
                           Intent intent = new Intent(LoginActivity.this, SettingIOTRadeyActivity.class);
                                startActivity(intent);
                            this.finish();
                        }
                        GjjEventBus.getInstance().post(new EventOfLoginSuccess());
                    }else{
                        if(null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())){
                            SmartHomeApp.showToast(param.getError().getMessage());
                        }else{
                            SmartHomeApp.showToast("登陆失败");
                        }
                    }
                }else{
                    SmartHomeApp.showToast("登陆失败");
                }
            }
        }else if (ApiHost.ADD_GW_URL.equals(url)) {
            if (response != null) {
                AddGwResponse param = (AddGwResponse) response.body;
                if (param != null) {
                    if(param.isSuccess()){
                        if(null != param.getResult().getUser_gateways() && param.getResult().getUser_gateways().size() > 0){
                            GatewayInfo gwInfo = param.getResult().getUser_gateways().get(0);
                            UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
                            userInfo.gateway = gwInfo.getMac_address();
                            userInfo.role = gwInfo.getMember_type();
                            userInfo.uuid = AndroidUtil.getDeviceIMEI(getApplication());
                            SmartHomeAppLib.getUserMgr().saveUser(userInfo);
                            Intent intent = new Intent();
                            intent.setClass(this, MainActivity.class);
                            startActivity(intent);
                            this.finish();
                        }
                    }else{
                        if(null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())){
                            SmartHomeApp.showToast(param.getError().getMessage());
                        }else{
                            SmartHomeApp.showToast("网关添加失败");
                        }
                    }
                }else{
                    SmartHomeApp.showToast("网关添加失败");
                }
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        L.d("loginBizFail");
        if (this.isFinishing()) {
            return;
        }
        dismissProgressDialog();
    }
}
