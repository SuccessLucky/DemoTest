package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultAddScene;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSelectGw;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultUpdateScene;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfSceneUpdate;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfSpeech;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceButtonInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceTools;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DeleteSceneResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDeviceByIdResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetSceneListResponse;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;
import com.kzksmarthome.SmartHouseYCT.util.UserRoleEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tools.IOTConfig;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.user.UserInfo;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 场景界面
 * Created by jack on 2016/8/27.
 */
public class SceneMainFragment extends BaseRequestFragment implements RequestCallback, SceneMainAdapter.OnSceneItemClick, SceneDelConfirmDialog.OnEnsureClick {

    @BindView(R.id.smart_scene_main_recycle)
    RecyclerView smartSceneMainRecycle;
    @BindView(R.id.title_right_tv)
    TextView titleRightTv;
    @BindView(R.id.title_left_tv)
    TextView titleLeftTv;


    private SceneMainAdapter mAdapter;

    private List<SceneInfo> sceneList = new ArrayList<SceneInfo>();

    private int delPosition = -1;
    private SceneDelConfirmDialog delConfirmDialog;
    /**
     * 网关
     */
    private String iotMac;
    private String originGwMac = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_scene_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initView();
        GjjEventBus.getInstance().register(mSceneMainEvent);
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (null != userInfo) {
            originGwMac = userInfo.gateway;
        }
        iotMac = userInfo.gateway;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            initData();
        }
    }

    private void initData() {
        RestRequestApi.getSceneList(getActivity(), -1, this);
    }

    private void initView() {
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (UserRoleEnums.ADMIN.getCode() == userInfo.role) {
            titleRightTv.setVisibility(View.VISIBLE);
        } else if (UserRoleEnums.USER.getCode() == userInfo.role) {
            titleRightTv.setVisibility(View.GONE);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        smartSceneMainRecycle.setLayoutManager(gridLayoutManager);
        mAdapter = new SceneMainAdapter(getActivity());
        mAdapter.setSceneList(sceneList);
        mAdapter.setOnSceneItemClickListener(this);
        smartSceneMainRecycle.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(mSceneMainEvent);
    }

    @OnClick(R.id.title_right_tv)
    public void onClick() {
        if (mAdapter.isEdit()) {
            titleRightTv.setText(R.string.edit_str);
            mAdapter.setEdit(false);
            sceneList.remove(sceneList.size() - 1);
            GjjEventBus.getInstance().post(new EventOfSceneUpdate());//通知首页场景更新了
        } else {
            titleRightTv.setText(R.string.finish_str);
            mAdapter.setEdit(true);
            sceneList.add(new SceneInfo(-1));
            smartSceneMainRecycle.scrollToPosition((sceneList.size() - 1));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        SmartHomeApp.showToast("请检查网络");
        if (url.startsWith(ApiHost.GET_SCENE_LIST_URL)) {
            mAdapter.setEdit(false);
            mAdapter.setSceneList(sceneList);
            titleRightTv.setText(R.string.edit_str);
        }
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_SCENE_LIST_URL)) {
            if (response != null && mAdapter != null) {
                GetSceneListResponse param = (GetSceneListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            sceneList = param.getResult();
                            mAdapter.setEdit(false);
                            mAdapter.setSceneList(sceneList);
                            titleRightTv.setText(R.string.edit_str);
                        } else {
                            mAdapter.setEdit(false);
                            mAdapter.setSceneList(sceneList);
                            titleRightTv.setText(R.string.edit_str);
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                        mAdapter.setEdit(false);
                        mAdapter.setSceneList(sceneList);
                        titleRightTv.setText(R.string.edit_str);
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                    mAdapter.setEdit(false);
                    mAdapter.setSceneList(sceneList);
                    titleRightTv.setText(R.string.edit_str);
                }
            }
        } else if (url.equals(ApiHost.DELETE_SCENE_URL)) {
            dismissLoadingDialog();
            if (response != null) {
                DeleteSceneResponse param = (DeleteSceneResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        SmartHomeApp.showToast("场景删除成功");
                        sceneList.remove(delPosition);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                }
            }
        } else if (url.startsWith(ApiHost.GET_DEVICE_BY_ID_URL.substring(0, ApiHost.GET_DEVICE_BY_ID_URL.lastIndexOf("/")))) {
            if (response != null) {
                GetDeviceByIdResponse param = (GetDeviceByIdResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            DeviceInfo deviceInfo = param.getResult();
                            if (null == deviceInfo.getDevice_buttons()) {
                                for (DeviceButtonInfo button : deviceInfo.getDevice_buttons()) {
                                    if (getString(R.string.device_switch).equals(button)) {
                                        try {
                                            RestRequestApi.sendRedOrder(iotMac, deviceInfo.getMac_address(), Tools.hexStr2Byte(button.getInstruction_code()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_SCENE_LIST_URL)) {
            mAdapter.setEdit(false);
            mAdapter.setSceneList(sceneList);
            titleRightTv.setText(R.string.edit_str);
        }
    }

    /**
     * 请求回调
     */
    public Object mSceneMainEvent = new Object() {
        public void onEventMainThread(EventOfResultAddScene data) {
            SceneInfo sceneInfo = data.sceneInfo;
            if (null != sceneInfo) {
                showToast(R.string.scene_add_succ_str);
                if (mAdapter.getSceneList().size() <= 1) {
                    sceneList.add(0, sceneInfo);
                } else {
                    sceneList.add(mAdapter.getSceneList().size() - 1, sceneInfo);
                }
                mAdapter.notifyDataSetChanged();
            }
        }

        public void onEventMainThread(EventOfResultUpdateScene data) {
            SceneInfo sceneInfo = data.sceneInfo;
            if (null != sceneInfo) {
                showToast(R.string.scene_update_succ_str);
                sceneList.set(data.position, sceneInfo);
                mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * 语音识别发送
         * @param eventOfSpeech
         */
        public void onEventMainThread(EventOfSpeech eventOfSpeech) {
            if (eventOfSpeech != null && !TextUtils.isEmpty(eventOfSpeech.getSpeechStr()) && !Util.isListEmpty(sceneList)) {
                for (SceneInfo sceneInfo : sceneList) {
                    try {
                        if (eventOfSpeech.getSpeechStr().contains(sceneInfo.getName())) {
                            RestRequestApi.sendSendSceneOrder(iotMac, Tools.hexStr2Byte(sceneInfo.getSerial_number()));
                            showToast("启动场景：" + sceneInfo.getName());
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 切换网关之后需重新请求数据
         * @param data
         */
        public void onEventMainThread(EventOfResultSelectGw data) {
            if (null == data.gwInfo) {
                return;
            }
            Log.d("switchgw", "情景切换网关-data.gwInfo->" + data.gwInfo.getGateway_name());
            GatewayInfo newGw = data.gwInfo;
            if (!data.isAddGW && newGw.getMac_address().equals(originGwMac)) {
                return;
            }
            Log.d("switchgw", "情景切换网关-originGwMac->" + originGwMac);
            originGwMac = newGw.getMac_address();
            Log.d("laixj", "切换网关重新请求数据->");
            sceneList = new ArrayList<SceneInfo>();
            mAdapter.setEdit(false);
            mAdapter.setSceneList(sceneList);
            if (UserRoleEnums.ADMIN.getCode() == newGw.getMember_type()) {
                titleRightTv.setVisibility(View.VISIBLE);
            } else if (UserRoleEnums.USER.getCode() == newGw.getMember_type()) {
                titleRightTv.setVisibility(View.GONE);
            }
            initData();
        }
    };

    @Override
    public void onSceneDeleteClick(int position) {
        if (position >= 0) {
            delPosition = position;
            SceneInfo sceneInfo = sceneList.get(position);
            if (null == sceneInfo) {
                return;
            }
            if (null == delConfirmDialog) {
                delConfirmDialog = new SceneDelConfirmDialog(getActivity(), position, sceneInfo);
                delConfirmDialog.setOnEnsureClickListener(this);
                delConfirmDialog.setCancelable(true);
                delConfirmDialog.setCanceledOnTouchOutside(true);
            } else {
                delConfirmDialog.setPosition(position);
                delConfirmDialog.setSceneInfo(sceneInfo);
            }
            delConfirmDialog.show();
        }
    }

    @OnClick(R.id.title_left_tv)
    public void onViewClicked() {
        showToast(R.string.close_warring_hint_str);
        RestRequestApi.liftAlarm(SmartHomeApp.getInstance(),iotMac);
    }

    class ControlThread implements Runnable {
        private List<SceneDetailInfo> sceneDetail;

        public ControlThread() {
        }

        public ControlThread(List<SceneDetailInfo> sceneDetail) {
            this.sceneDetail = sceneDetail;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingDialog(R.string.send_loading_str, true);
                }
            });
            for (int i = 0; i < sceneDetail.size(); i++) {
                int sleepTime = 300;
                SceneDetailInfo info = sceneDetail.get(i);
                Log.d("laixj", "情景模式控制设备--");
                DeviceInfo deviceInfo = new DeviceInfo(info);
                if (DeviceTypeEnums.DROPLIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo)
                        || DeviceTypeEnums.TABLELAMP.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    String way = deviceInfo.getOther_status();
                    if (TextUtils.isEmpty(way)) {
                        return;
                    }
                    byte way_byte = Byte.valueOf(way);
                    int state1 = deviceInfo.getDevice_state1();
                    int state2 = deviceInfo.getDevice_state2();
                    int state3 = deviceInfo.getDevice_state3();
                    if (way_byte == 1) {
                        if (1 == state1) {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
                            deviceInfo.setDevice_state1(1);
                        } else {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                            deviceInfo.setDevice_state1(2);
                        }
                    } else if (way_byte == 2) {
                        if (1 == state2) {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x1, (byte) 0x00);
                            deviceInfo.setDevice_state2(1);
                        } else {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                            deviceInfo.setDevice_state2(2);
                        }
                    } else if (way_byte == 3) {
                        if (1 == state3) {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
                            deviceInfo.setDevice_state3(1);
                        } else {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                            deviceInfo.setDevice_state3(2);
                        }
                    }
                } else if (DeviceTypeEnums.CURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.DOOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.AIRCONDITION.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    sleepTime = 500;
                    RestRequestApi.setAirConditionerType(iotMac, deviceInfo.getMac_address(), Integer.valueOf(deviceInfo.getOther_status()));//发送空调型号适配
                    int flag = deviceInfo.getDevice_state1();
                    if (flag == DeviceStatusEnums.OFF.getCode()) {
                        RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, 0x00});
                    } else if (flag == DeviceStatusEnums.ON.getCode()) {
                        RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, (byte) 0xff});
                    }
                } else if (DeviceTypeEnums.INFRARED.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    sleepTime = 500;
                    RestRequestApi.sendRedOrder(iotMac, deviceInfo.getMac_address(), deviceInfo.getDevice_state1());//发送红外命令
                } else if (DeviceTypeEnums.LIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo) || DeviceTypeEnums.SOUNDANDLIGHTALARM.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    int flag1 = deviceInfo.getDevice_state1();
                    if (flag1 == DeviceStatusEnums.OFF.getCode()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
                    } else if (flag1 == DeviceStatusEnums.ON.getCode()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
                    }
                } else if (DeviceTypeEnums.MOBILEOUTLET.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    int flag1 = deviceInfo.getDevice_state1();
                    if (flag1 == DeviceStatusEnums.OFF.getCode()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
                    } else if (flag1 == DeviceStatusEnums.ON.getCode()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
                    }
                } else if (DeviceTypeEnums.OUTLET.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.DOORANDWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.TRANSLATWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    int flag = deviceInfo.getDevice_state1();
                    if (flag == DeviceStatusEnums.OFF.getCode()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
                    } else if (flag == DeviceStatusEnums.ON.getCode()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
                    }
                } else if (DeviceTypeEnums.LOCK.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.PUSHWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    int flag = deviceInfo.getDevice_state1();
                    if (flag == DeviceStatusEnums.OFF.getCode()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
                    } else if (flag == DeviceStatusEnums.ON.getCode()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
                    }
                } else if (DeviceTypeEnums.WINDOWOPENER.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//协议转发开窗器
                    if (deviceInfo.getDevice_state1() == 2) {
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.WINDOW_OPENER_FZ);
                    } else {
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.WINDOW_OPENER_ZZ);
                    }
                } else if (DeviceTypeEnums.ELECTRICCURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//电动窗帘
                    if (deviceInfo.getDevice_state1() == 2) {
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.ELECTRIC_CURTAIN_ZZ);
                    } else {
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.ELECTRIC_CURTAIN_FZ);
                    }
                } else if (DeviceTypeEnums.CONTROLBOX.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) deviceInfo.getDevice_state1());
                } else if (DeviceTypeEnums.COLORFULBULB.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//多彩灯泡
                    if (deviceInfo.getDevice_state1() == 2) {
                        RestRequestApi.sendColorLightLight(iotMac, deviceInfo.getMac_address(), (byte) 240, (byte) 1);
                    } else {
                        RestRequestApi.sendColorLightLight(iotMac, deviceInfo.getMac_address(), (byte) 0, (byte) 2);
                    }
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == (sceneDetail.size() - 1)) {//发送完关闭loading
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onSceneItemClick(int position) {
       /* List<SceneDetailInfo> sceneDetail = mAdapter.getSceneList().get(position).getScene_details();
        if (null != sceneDetail && sceneDetail.size() > 0) {
          ControlThread ctrThread = new ControlThread(sceneDetail);
            BackgroundTaskExecutor.executeTask(ctrThread);
            UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
            iotMac = userInfo.gateway;
            RestRequestApi.sendSendSceneOrder(iotMac, (byte) 0x01);
        }*/
        try {
            SceneInfo sceneInfo = mAdapter.getSceneList().get(position);
            if (sceneInfo != null && sceneInfo.getSerial_number() != null) {
                doLoading();
                RestRequestApi.sendSendSceneOrder(iotMac, Tools.hexStr2Byte(sceneInfo.getSerial_number()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示loading
     */
    private void doLoading() {
        showLoadingDialog(R.string.send_str, true);
        MainTaskExecutor.scheduleTaskOnUiThread(2000, new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
            }
        });
    }


    @Override
    public void onEnsureClick(int position, SceneInfo sceneInfo) {
        delPosition = position;
        if (null == sceneInfo) {
            return;
        }
        showLoadingDialog(R.string.loading_str, false);
        RestRequestApi.deleteScene(getActivity(), sceneInfo.getScene_id(), this);
    }
}
