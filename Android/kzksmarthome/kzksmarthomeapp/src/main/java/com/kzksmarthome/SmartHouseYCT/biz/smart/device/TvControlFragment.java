package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceCtrl;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfUpdateDeviceList;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddRedButtonResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDeviceByIdResponse;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: SmartCurtainControlFragment
 * @Description: 电视控制界面
 * @date 2016/9/11 8:39
 */
public class TvControlFragment extends BaseRequestFragment implements RequestCallback, View.OnClickListener,
        TvCtrOperationSelectDialog.OnOperationSelectItemClick, TvCmdOperationDialog.OnEnsureClick {


    @BindView(R.id.tv_button_grid)
    GridView tvButtonGrid;

    private TvControlButtonAdapter cmdAdapter;
    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;

    private int flag = 0;
    /**
     * 按钮信息对象
     */
    private DeviceButtonInfo mDeviceButtonInfo;

    private TvCtrOperationSelectDialog dialog = null;

    private TvCmdOperationDialog editDialog = null;

    private TvCmdOperationDialog addDialog = null;
    /**
     * 是否为添加红外编码
     */
    private boolean mIsAddRedFlag = true;
    /**
     * 网关mac地址
     */
    private String mIotMac;
    /**
     * 红外编码列表
     */
    private ArrayList<Integer> mTvRedOrderNumList;
    private boolean opened;
    /**
     * 红外按键编码
     */
    private int mKeyNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_tv_control, container, false);
        ButterKnife.bind(this, mRootView);
        Bundle bundle = getArguments();
        deviceInfo = bundle.getParcelable("deviceInfo");
        if (null == deviceInfo) {
            getActivity().onBackPressed();
        }
        RestRequestApi.getAllRedOrderNumber(getActivity(), this);
        if (null == deviceInfo.getDevice_buttons()) {
            RestRequestApi.getDeviceById(getActivity(), deviceInfo.getDevice_id(), this);
        }
        if (null != deviceInfo) {
            flag = deviceInfo.getDevice_state1();
        }
        com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        mIotMac = userInfo.gateway;
        cmdAdapter = new TvControlButtonAdapter(getActivity());
        cmdAdapter.setCmdList(deviceInfo.getDevice_buttons(), deviceInfo.getDevice_id());
        tvButtonGrid.setAdapter(cmdAdapter);
        tvButtonGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DeviceButtonInfo cmdInfo = cmdAdapter.getCmdList().get(position);
                if (null != cmdInfo) {
                    try {
                        mIsAddRedFlag = false;
                        if (getString(R.string.device_switch).equals(cmdInfo.getName())) {
                            if (opened) {
                                deviceInfo.setDevice_state1(DeviceStatusEnums.OFF.getCode());
                                opened = !opened;
                            } else {
                                deviceInfo.setDevice_state1(DeviceStatusEnums.ON.getCode());
                                opened = !opened;
                            }
                        }
                        RestRequestApi.sendRedOrder(mIotMac, deviceInfo.getMac_address(), Tools.hexStr2Byte(cmdInfo.getInstruction_code()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tvButtonGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO: 2016/9/11
                DeviceButtonInfo cmdInfo = cmdAdapter.getCmdList().get(position);
                if (null != cmdInfo) {
                    if (null == dialog) {
                        dialog = new TvCtrOperationSelectDialog(getActivity(), position, deviceInfo, cmdInfo);
                    } else {
                        dialog.setPosition(position);
                        dialog.setCmdInfo(cmdInfo);
                        dialog.setDeviceInfo(deviceInfo);
                    }
                    dialog.setOnOperationSelectClickListener(TvControlFragment.this);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
                return true;
            }
        });
        tvButtonGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        GjjEventBus.getInstance().register(deviceEvent);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 请求回调
     */
    public Object deviceEvent = new Object() {
        public void onEventMainThread(EventOfTcpResult eventOfTcpResult) {
            try {
                if (eventOfTcpResult != null) {
                    DeviceState deviceState = eventOfTcpResult.deviceState;
                    if (deviceState != null && deviceState.dstAddr != null && deviceInfo.getMac_address().equals(Tools.byte2HexStr(deviceState.dstAddr))) {
                        if (deviceState.redSendState != 0) {
                            dismissLoadingDialog();
                        }
                        if (deviceState.redSendState == 1) {//红外学习成功或红外控制成功
                            if (mIsAddRedFlag) {//学习
                                SmartHomeApp.showToast("红外学习成功！");
                                if (mDeviceButtonInfo != null) {
                                    mDeviceButtonInfo.setInstruction_code(Tools.byte2HexStr((byte) (mKeyNumber)));
                                    mTvRedOrderNumList.add(mKeyNumber);
                                }
                            } else {//控制
                                SmartHomeApp.showToast("控制命令发送成功！");
                            }
                        } else if (deviceState.redSendState == 2) {//红外学习失败或红外控制失败
                            if (mIsAddRedFlag) {//学习
                                SmartHomeApp.showToast("红外学习失败！");
                            } else {//控制
                                SmartHomeApp.showToast("控制命令发送失败！");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onFailure(Request request, String url, Exception e) {
        dismissLoadingDialog();
        if (ApiHost.ADD_DEVICE_BUTTON_URL.equals(url)) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onBizSuccess(final ResponseParam response, final String url, int from) {

        if (url.startsWith(ApiHost.GET_ALL_READ_ORDER_NUMBER)) {
            BackgroundTaskExecutor.executeTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (response != null) {
                            mTvRedOrderNumList = new ArrayList<Integer>(100);
                            TvRedOrderNumberResponse tvRedOrderNumberResponse = (TvRedOrderNumberResponse) response.body;
                            List<TvRedOrderNumberResponse.ResultBean> resultBeanList = tvRedOrderNumberResponse.getResult();
                            if (resultBeanList != null) {
                                for (TvRedOrderNumberResponse.ResultBean resultBean : resultBeanList) {
                                    if (!TextUtils.isEmpty(resultBean.getInstruction_code())&&!"-1".equals(resultBean.getInstruction_code())) {
                                        mTvRedOrderNumList.add(Tools.hexStr2Int(resultBean.getInstruction_code()));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (ApiHost.ADD_DEVICE_BUTTON_URL.equals(url)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissLoadingDialog();
                    AddRedButtonResponse addRedButtonResponse = (AddRedButtonResponse) response.body;
                    if(addRedButtonResponse != null && addRedButtonResponse.getResult() != null) {
                        Log.d("REQ", addRedButtonResponse.getResult().getDevice_buttons().toString());
                    }
                    GjjEventBus.getInstance().post(new EventOfUpdateDeviceList());
                    Activity activity = getActivity();
                    if(activity != null) {
                        activity.onBackPressed();
                    }
                }
            });
        } else if (url.startsWith(ApiHost.GET_DEVICE_BY_ID_URL.substring(0, ApiHost.GET_DEVICE_BY_ID_URL.lastIndexOf("/")))) {
            if (response != null) {
                GetDeviceByIdResponse param = (GetDeviceByIdResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            deviceInfo = param.getResult();
                            if (null == deviceInfo.getDevice_buttons()) {
                                cmdAdapter.setCmdList(new ArrayList<DeviceButtonInfo>(), deviceInfo.getDevice_id());
                            } else {
                                Log.d("laixj", "设备按键：" + deviceInfo.getDevice_buttons().toString());
                                cmdAdapter.setCmdList(deviceInfo.getDevice_buttons(), deviceInfo.getDevice_id());
                            }
                            cmdAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }

    }

    /**
     * 获取按键编号
     *
     * @return
     */
    private int getKeyNumber() {
        try {
            if (!Util.isListEmpty(mTvRedOrderNumList)) {
                for (int i = 0; i < 100; i++) {
                    if (mTvRedOrderNumList.indexOf(i) == -1) {
                        return i;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        dismissLoadingDialog();
        if (ApiHost.ADD_DEVICE_BUTTON_URL.equals(url)) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(deviceEvent);
        try {
            if (null != addDialog) {
                addDialog.dismiss();
            }
            if (null != editDialog) {
                editDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_right_tv:
                //showToast("添加命令");
                addDialog = new TvCmdOperationDialog(getActivity(), 0, -1, deviceInfo, new DeviceButtonInfo());
                addDialog.setOnEnsureClickListener(TvControlFragment.this);
                addDialog.setCancelable(true);
                addDialog.setCanceledOnTouchOutside(true);
                addDialog.show();
                break;
        }
    }

    @Override
    public void onRightBtnClick() {
        addDialog = new TvCmdOperationDialog(getActivity(), 0, -1, deviceInfo, new DeviceButtonInfo());
        addDialog.setOnEnsureClickListener(TvControlFragment.this);
        addDialog.setCancelable(true);
        addDialog.setCanceledOnTouchOutside(true);
        addDialog.show();
    }

    @Override
    public void onDeleteClick(int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo) {
        if (!Util.isListEmpty(cmdAdapter.getCmdList()) && cmdAdapter.getCmdList().size() > position) {
            if (getString(R.string.device_switch).equals(cmdAdapter.getCmdList().get(position).getName())) {
                showToast(R.string.device_switch_nodelete);
                return;
            }
            cmdAdapter.getCmdList().remove(position);
            cmdAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditClick(int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo) {
        //showToast("编辑" + cmdInfo.toString());
        if (getString(R.string.device_switch).equals(cmdAdapter.getCmdList().get(position).getName())) {
            showToast(R.string.device_switch_noedit);
            return;
        }
        editDialog = new TvCmdOperationDialog(getActivity(), 1, position, deviceInfo, cmdInfo);
        editDialog.setOnEnsureClickListener(TvControlFragment.this);
        editDialog.setCancelable(true);
        editDialog.setCanceledOnTouchOutside(true);
        editDialog.show();
    }

    @Override
    public void onLearnClick(int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo) {
        try {
            mKeyNumber = getKeyNumber();
            RestRequestApi.redLeaner(mIotMac, deviceInfo.getMac_address(), mKeyNumber);
            mIsAddRedFlag = true;
            mDeviceButtonInfo = cmdInfo;
            showToast("学习" + cmdInfo.toString() + "---" + mKeyNumber);
            showLoadingDialog(R.string.loading, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnsureClick(int flag, int position, DeviceInfo deviceInfo, DeviceButtonInfo cmdInfo) {
        Log.d("laixj", "添加/编辑：flag=" + flag + ";position=" + position);
        Log.d("laixj", "添加/编辑：cmdInfo=" + cmdInfo.toString());
        if (flag == 0) {//添加
            if (getString(R.string.device_switch).equals(cmdInfo.getName())) {
                showToast(R.string.device_switch_exist);
                return;
            }
            // TODO: 2016/9/11
            cmdInfo.setButton_id(-1);
            cmdInfo.setInstruction_code("-1");
            cmdAdapter.getCmdList().add(cmdInfo);
            cmdAdapter.notifyDataSetChanged();
        } else if (flag == 1) {//编辑
            if (getString(R.string.device_switch).equals(cmdInfo.getName())) {
                showToast(R.string.device_switch_exist);
                return;
            }
            // TODO: 2016/9/11
            cmdAdapter.getCmdList().set(position, cmdInfo);
            cmdAdapter.notifyDataSetChanged();
        } else if (flag == 3) {

        }
    }

    /*@Override
    public boolean goBack() {
        if (deviceInfo != null && !Util.isListEmpty(cmdAdapter.getCmdList())) {
            RestRequestApi.addRedButton(getActivity(), deviceInfo.getDevice_id(), cmdAdapter.getCmdList(), this);
        }
        return super.goBack();
    }*/

    @Override
    public void onBackPressed() {
        try {
            if (deviceInfo != null && cmdAdapter != null && !Util.isListEmpty(cmdAdapter.getCmdList())) {
                Log.d("laixj", "红外按键保存-->" + cmdAdapter.getCmdList().toString());
                showLoadingDialog(R.string.loading, false);
                RestRequestApi.addRedButton(getActivity(), deviceInfo.getDevice_id(), cmdAdapter.getCmdList(), this);
                DeviceButtonInfo deviceButtonInfo = cmdAdapter.getCmdList().get(0);
                if (deviceButtonInfo != null) {
                    EventOfResultDeviceCtrl event = new EventOfResultDeviceCtrl();
                    deviceInfo.setDevice_state1(Tools.hexStr2Byte(deviceButtonInfo.getInstruction_code()));
                    event.deviceInfo = deviceInfo;
                    GjjEventBus.getInstance().post(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
