package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.apitest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: RestApiTestFragment
 * @Description: rest请求测试界面
 * @date 2016/9/18 16:17
 */
public class RestApiTestFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.register_sms_rl)
    RelativeLayout registerSmsRl;
    @BindView(R.id.register_rl)
    RelativeLayout registerRl;
    @BindView(R.id.login_rl)
    RelativeLayout loginRl;
    @BindView(R.id.reset_pwd_sms_rl)
    RelativeLayout resetPwdSmsRl;
    @BindView(R.id.reset_pwd_rl)
    RelativeLayout resetPwdRl;
    @BindView(R.id.add_family_user_rl)
    RelativeLayout addFamilyUserRl;
    @BindView(R.id.get_floor_info_rl)
    RelativeLayout getFloorInfoRl;
    @BindView(R.id.get_floor_list_rl)
    RelativeLayout getFloorListRl;
    @BindView(R.id.add_floor_rl)
    RelativeLayout addFloorRl;
    @BindView(R.id.update_floor_rl)
    RelativeLayout updateFloorRl;
    @BindView(R.id.delete_floor_rl)
    RelativeLayout deleteFloorRl;
    @BindView(R.id.get_room_list_rl)
    RelativeLayout getRoomListRl;
    @BindView(R.id.add_room_rl)
    RelativeLayout addRoomRl;
    @BindView(R.id.update_room_rl)
    RelativeLayout updateRoomRl;
    @BindView(R.id.delete_room_rl)
    RelativeLayout deleteRoomRl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_api_test_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        return super.onCreateView(inflater, container, savedInstanceState);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.register_sms_rl, R.id.register_rl, R.id.login_rl, R.id.reset_pwd_sms_rl, R.id.reset_pwd_rl,
            R.id.add_family_user_rl, R.id.get_floor_info_rl, R.id.get_floor_list_rl, R.id.add_floor_rl,
            R.id.update_floor_rl, R.id.delete_floor_rl, R.id.get_room_list_rl, R.id.add_room_rl, R.id.update_room_rl, R.id.delete_room_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_sms_rl:
                //Bundle bundle = new Bundle();
                //bundle.putParcelable("deviceInfo", deviceInfo);
                PageSwitcher.switchToTopNavPage(getActivity(), RegisterSmsTestFragment.class, null, "", "注册短信发送", "");
                break;
            case R.id.register_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), RegisterTestFragment.class, null, "", "注册", "");
                break;
            case R.id.login_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "登陆", "");
                break;
            case R.id.reset_pwd_sms_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), ResetPwdSmsTestFragment.class, null, "", "重置密码短信", "");
                break;
            case R.id.reset_pwd_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), ResetPwdTestFragment.class, null, "", "重置密码", "");
                break;
            case R.id.add_family_user_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "添加家庭用户", "");
                break;
            case R.id.get_floor_info_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "获取楼层信息", "");
                break;
            case R.id.get_floor_list_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "获取楼层列表", "");
                break;
            case R.id.add_floor_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "添加楼层", "");
                break;
            case R.id.update_floor_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "修改楼层", "");
                break;
            case R.id.delete_floor_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "删除楼层", "");
                break;
            case R.id.get_room_list_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "获取房间列表", "");
                break;
            case R.id.add_room_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "添加房间", "");
                break;
            case R.id.update_room_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "修改房间", "");
                break;
            case R.id.delete_room_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), LoginTestFragment.class, null, "", "删除房间", "");
                break;
        }
    }
}
