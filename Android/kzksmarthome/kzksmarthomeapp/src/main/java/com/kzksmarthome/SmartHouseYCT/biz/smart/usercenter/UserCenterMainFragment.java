package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSelectGw;
import com.kzksmarthome.SmartHouseYCT.biz.login.LoginActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetGwListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.LogoutResponse;
import com.kzksmarthome.SmartHouseYCT.util.UserRoleEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.tcp.TCPMgr;
import com.kzksmarthome.common.lib.util.AndroidUtil;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.user.UserInfo;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 我的
 * Created by jack on 2016/8/27.
 */
public class UserCenterMainFragment extends BaseRequestFragment implements RequestCallback, OperateConfirmDialog.OnEnsureClick {
    @BindView(R.id.setting_iv_user)
    ImageView settingIvUser;
    @BindView(R.id.setting_rl_gw_switch)
    RelativeLayout settingRlGwSwitch;
    @BindView(R.id.setting_rl_rights_manage)
    RelativeLayout settingRlRightsManage;
    @BindView(R.id.setting_rl_security_manage)
    RelativeLayout settingRlSecurityManage;
    @BindView(R.id.setting_rl_smart_manage)
    RelativeLayout settingRlSmartManage;
    @BindView(R.id.setting_rl_setting)
    RelativeLayout settingRlSetting;
    @BindView(R.id.setting_rl_logout)
    RelativeLayout settingRlLogout;
    @BindView(R.id.setting_user_tv)
    TextView settingUserTv;
    @BindView(R.id.setting_gw_tv)
    TextView settingGwTv;
    @BindView(R.id.security_manage_top_line)
    View securityManageTopLine;
    @BindView(R.id.security_manage_bottom_line)
    View securityManageBottomLine;
    @BindView(R.id.version_tv)
    TextView versionTv;

    private GatewayInfo originGw;

    private List<GatewayInfo> gwList;

    private OperateConfirmDialog logoutConfirmDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_usercenter_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        Log.d("laixj", "我页请求数据-onCreateView->");
        judgeGw();
        GjjEventBus.getInstance().register(mUserCenterEvent);
        getData();
        versionTv.setText("Version："+ Util.getVersion(SmartHomeApp.getInstance()));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 请求回调
     */
    public Object mUserCenterEvent = new Object() {
        public void onEventMainThread(EventOfResultSelectGw data) {
            if (null == data.gwInfo) {
                return;
            }
            originGw = data.gwInfo;
            UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
            if (!data.isAddGW && originGw.getMac_address().equals(userInfo.gateway)) {
                return;
            }
            userInfo.gateway = originGw.getMac_address();
            userInfo.role = originGw.getMember_type();
            userInfo.uuid = AndroidUtil.getDeviceIMEI(getActivity().getApplication());
            SmartHomeAppLib.getUserMgr().saveUser(userInfo);
            judgeGw();
            gwList = null;
            getData();
        }
    };

    private void getData() {
        RestRequestApi.getGwList(getActivity(), this);
    }

    private void judgeGw() {
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (null == userInfo) {
            return;
        }
        if (UserRoleEnums.ADMIN.getCode() == userInfo.role) {
            settingUserTv.setText(R.string.user_admin_str);
            securityManageTopLine.setVisibility(View.GONE);
            settingRlRightsManage.setVisibility(View.VISIBLE);
            securityManageBottomLine.setVisibility(View.GONE);
            settingRlSecurityManage.setVisibility(View.GONE);

            settingRlSmartManage.setVisibility(View.VISIBLE);
            settingRlSetting.setVisibility(View.GONE);

        } else if (UserRoleEnums.USER.getCode() == userInfo.role) {
            settingUserTv.setText(R.string.user_user_str);
            securityManageTopLine.setVisibility(View.GONE);

            settingRlRightsManage.setVisibility(View.GONE);
            securityManageBottomLine.setVisibility(View.GONE);
            settingRlSecurityManage.setVisibility(View.GONE);

            settingRlSmartManage.setVisibility(View.GONE);
            settingRlSetting.setVisibility(View.GONE);

        }
        settingGwTv.setText(String.format(getString(R.string.setting_gw_id_str), userInfo.gateway));
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        L.d("loginFail");
        SmartHomeApp.showToast("请检查网络");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (url.startsWith(ApiHost.GET_GW_LIST_URL)) {
            if (response != null) {
                GetGwListResponse param = (GetGwListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult().getUser_gateways() && param.getResult().getUser_gateways().size() > 0) {
                            gwList = param.getResult().getUser_gateways();
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("网关列表获取失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("网关列表获取失败");
                }
            }
        } else if (ApiHost.LOGOUT_URL.equals(url)) {
            if (response != null) {
                LogoutResponse param = (LogoutResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        Log.d("doExecute", "成功退出登录");
                        SmartHomeAppLib.getUserMgr().logOut();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        SmartHomeAppLib.getInstance().getPreferences().edit()
                                .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, null)
                                .commit();
                        SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC,null).commit();
                        ApiHost.NETWORK_ISREMOTE = false;
                        //退出登录断开网络连接
                        TCPMgr.getInstance().closeConnect();
                    }
                } else {
                    // TODO: 2016/12/31
                }
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(mUserCenterEvent);
    }

    @OnClick({R.id.setting_iv_user, R.id.setting_rl_gw_switch, R.id.setting_rl_rights_manage, R.id.setting_rl_security_manage, R.id.setting_rl_smart_manage, R.id.setting_rl_setting, R.id.setting_rl_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_iv_user:
                showToast("点击头像");
                break;
            case R.id.setting_rl_gw_switch: {
                Bundle bundle = new Bundle();
                bundle.putSerializable("gwlist", (Serializable) gwList);
                bundle.putSerializable("selectedgw", (Serializable) originGw);
                PageSwitcher.switchToTopNavPage(getActivity(), GwSwitchFragment.class, bundle, "", "切换网关", "确定", -1, "");
                break;
            }
            case R.id.setting_rl_rights_manage:
                //showToast("权限管理");
                PageSwitcher.switchToTopNavPage(getActivity(), RightsManageFragment.class, null, "", getString(R.string.setting_rights_manage_str), "", -1, null, R.drawable.icon_add);
                break;
            case R.id.setting_rl_security_manage:
                //showToast("安防管理");
                //Bundle bundle = new Bundle();
                //bundle.putParcelable("deviceInfo", deviceInfo);
                PageSwitcher.switchToTopNavPage(getActivity(), SecurityManageFragment.class, null, "", getString(R.string.setting_security_manage_str), "", -1, null, R.drawable.icon_add);
                break;
            case R.id.setting_rl_smart_manage:
                //showToast("智能管理");
                PageSwitcher.switchToTopNavPage(getActivity(), SmartManageFragment.class, null, "", getString(R.string.setting_smart_manage_str), "", -1, null, R.drawable.icon_add);
                break;
            case R.id.setting_rl_setting:
                //Bundle bundle = new Bundle();
                //bundle.putParcelable("deviceInfo", deviceInfo);
                PageSwitcher.switchToTopNavPage(getActivity(), SettingFragment.class, null, "", getString(R.string.setting_setting_str), "");
                break;
            case R.id.setting_rl_logout:
                showLogoutConfirmDialog(getString(R.string.dialog_title_logout_confirm));
                break;
            default:
                break;
        }
    }

    private void showLogoutConfirmDialog(String title) {
        if (null == logoutConfirmDialog) {
            logoutConfirmDialog = new OperateConfirmDialog(getActivity(), title);
        } else {
            logoutConfirmDialog.setmTitleString(title);
        }
        logoutConfirmDialog.setOnEnsureClickListener(this);
        logoutConfirmDialog.setCancelable(true);
        logoutConfirmDialog.setCanceledOnTouchOutside(true);
        logoutConfirmDialog.show();
    }

    @Override
    public void onEnsureClick() {
        RestRequestApi.logout(getActivity(), this);
    }
}
