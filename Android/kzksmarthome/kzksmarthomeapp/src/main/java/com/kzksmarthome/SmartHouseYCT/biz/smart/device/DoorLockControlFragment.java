package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceCtrl;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.module.user.UserInfo;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.Stack;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: DoorLockControlFragment
 * @Description: 门锁控制界面
 * @date 2016/9/11 22:03
 */
public class DoorLockControlFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.lock_ctr_iv)
    ImageView lockCtrIv;
    @BindView(R.id.lock_ctr_pwd_hint_tv)
    TextView lockCtrPwdHintTv;
    @BindView(R.id.lock_ctr_pwd1)
    ImageView lockCtrPwd1;
    @BindView(R.id.lock_ctr_pwd2)
    ImageView lockCtrPwd2;
    @BindView(R.id.lock_ctr_pwd3)
    ImageView lockCtrPwd3;
    @BindView(R.id.lock_ctr_pwd4)
    ImageView lockCtrPwd4;
    @BindView(R.id.lock_ctr_pwd5)
    ImageView lockCtrPwd5;
    @BindView(R.id.lock_ctr_pwd6)
    ImageView lockCtrPwd6;
    @BindView(R.id.lock_ctr_pwd_number1_tv)
    TextView lockCtrPwdNumber1Tv;
    @BindView(R.id.lock_ctr_pwd_number2_tv)
    TextView lockCtrPwdNumber2Tv;
    @BindView(R.id.lock_ctr_pwd_number3_tv)
    TextView lockCtrPwdNumber3Tv;
    @BindView(R.id.lock_ctr_pwd_number4_iv)
    TextView lockCtrPwdNumber4Iv;
    @BindView(R.id.lock_ctr_pwd_number5_iv)
    TextView lockCtrPwdNumber5Iv;
    @BindView(R.id.lock_ctr_pwd_number6_iv)
    TextView lockCtrPwdNumber6Iv;
    @BindView(R.id.lock_ctr_pwd_number7_tv)
    TextView lockCtrPwdNumber7Tv;
    @BindView(R.id.lock_ctr_pwd_number8_tv)
    TextView lockCtrPwdNumber8Tv;
    @BindView(R.id.lock_ctr_pwd_number9_tv)
    TextView lockCtrPwdNumber9Tv;
    @BindView(R.id.lock_ctr_pwd_del_tv)
    TextView lockCtrPwdDelTv;
    @BindView(R.id.lock_ctr_pwd_number0_tv)
    TextView lockCtrPwdNumber0Tv;
    @BindView(R.id.lock_ctr_open_tv)
    TextView lockCtrOpenTv;
    @BindView(R.id.lock_ctr_pwd_number_line4_ll)
    LinearLayout lockCtrPwdNumberLine4Ll;
    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;
    private int flag = 0;

    private Stack pwdStack = new Stack();

    private ImageView[] pwdImg = new ImageView[6];
    /**
     * 设备地址
     */
    private  String iotMac;
    /**
     * 是否启动开始
     */
    private boolean isLock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_doorlock_control, container, false);
        ButterKnife.bind(this, mRootView);

        Bundle bundle = getArguments();
        deviceInfo = bundle.getParcelable("deviceInfo");
        if (null != deviceInfo) {
            flag = deviceInfo.getDevice_state1();
            if(flag == DeviceStatusEnums.ON.getCode()){
                lockCtrIv.setImageResource(R.drawable.lock_ctr_unlock);
            }else if(flag == DeviceStatusEnums.OFF.getCode()){
                lockCtrIv.setImageResource(R.drawable.lock_ctr_lock);
            }
        }
        pwdImg[0] = lockCtrPwd1;
        pwdImg[1] = lockCtrPwd2;
        pwdImg[2] = lockCtrPwd3;
        pwdImg[3] = lockCtrPwd4;
        pwdImg[4] = lockCtrPwd5;
        pwdImg[5] = lockCtrPwd6;
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if(userInfo != null){
            iotMac = userInfo.gateway;
        }
        GjjEventBus.getInstance().register(deviceEvent);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onRightBtnClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("deviceinfo", (Serializable) deviceInfo);
        PageSwitcher.switchToTopNavPage(getActivity(), DoorContactManageFragment.class, bundle, "", "门锁联系人", "", -1, null, R.drawable.icon_add);
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
        GjjEventBus.getInstance().unregister(deviceEvent);
    }

    @OnClick({R.id.lock_ctr_pwd_number1_tv, R.id.lock_ctr_pwd_number2_tv, R.id.lock_ctr_pwd_number3_tv,
            R.id.lock_ctr_pwd_number4_iv, R.id.lock_ctr_pwd_number5_iv, R.id.lock_ctr_pwd_number6_iv, R.id.lock_ctr_pwd_number7_tv, R.id.lock_ctr_pwd_number8_tv, R.id.lock_ctr_pwd_number9_tv, R.id.lock_ctr_pwd_del_tv, R.id.lock_ctr_pwd_number0_tv, R.id.lock_ctr_open_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lock_ctr_pwd_number1_tv:
                fillinPwd(1);
                break;
            case R.id.lock_ctr_pwd_number2_tv:
                fillinPwd(2);
                break;
            case R.id.lock_ctr_pwd_number3_tv:
                fillinPwd(3);
                break;
            case R.id.lock_ctr_pwd_number4_iv:
                fillinPwd(4);
                break;
            case R.id.lock_ctr_pwd_number5_iv:
                fillinPwd(5);
                break;
            case R.id.lock_ctr_pwd_number6_iv:
                fillinPwd(6);
                break;
            case R.id.lock_ctr_pwd_number7_tv:
                fillinPwd(7);
                break;
            case R.id.lock_ctr_pwd_number8_tv:
                fillinPwd(8);
                break;
            case R.id.lock_ctr_pwd_number9_tv:
                fillinPwd(9);
                break;
            case R.id.lock_ctr_pwd_del_tv:
                if(!pwdStack.empty()){
                    int size = pwdStack.size();
                    pwdImg[size - 1].setImageResource(R.drawable.shape_lock_pwd_empty);
                    pwdStack.pop();
                }
                break;
            case R.id.lock_ctr_pwd_number0_tv:
                fillinPwd(0);
                break;
            case R.id.lock_ctr_open_tv:
                // TODO: 2016/9/11
                if(!pwdStack.empty() && pwdStack.size() == 6){
                    //showToast("尝试开门");
                    //发送开锁命令
                    senLockOrder();
                    isLock = true;
                    openLock();
                    showLoadingDialog(R.string.open_lock_str,true);
                    MainTaskExecutor.scheduleTaskOnUiThread(20000, new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                            if(isLock) {
                                showToast("开锁失败！");
                            }
                        }
                    });
                }
                break;
        }
    }


    /**
     * 重复发送开锁命令
     */
    int sendCount = 5;

    /**
     * 开锁
     */
    public void openLock(){
        if(isLock && sendCount > 0){
            MainTaskExecutor.scheduleTaskOnUiThread(3000, new Runnable() {
                @Override
                public void run() {
                    senLockOrder();
                    sendCount --;
                    openLock();
                }
            });
        }else{
            sendCount = 5;
        }
    }

    /**
     * 设备回调
     */
    public Object deviceEvent = new Object() {
        public void onEventMainThread(EventOfTcpResult eventOfTcpResult) {
            if (getActivity() == null || !isLock) {
                return;
            }
            try {
                if (eventOfTcpResult != null) {
                    final DeviceState deviceState = eventOfTcpResult.deviceState;
                    byte[] od = deviceState.deviceOD;
                    if (deviceState.dstAddr == null) {
                        return;
                    }
                    if (od[0] == 0x0f && od[1] == (byte) 0xbe) {//判断是否为计量设备
                       if(deviceState.deviceType == 0x02 ){
                           if(deviceState.deviceProduct == 0x02) {
                               if (deviceState.cmdId == 0x01) {
                                   dismissLoadingDialog();
                                   isLock = false;
                                   senLockOrder();
                               } else if (deviceState.cmdId == 0x07 && (deviceState.lockOperateType == 0x00 || deviceState.lockOperateType == 0x53)) {
                                   dismissLoadingDialog();
                                   isLock = false;
                                   showToast("远程开锁成功！");
                               }
                           }else if(deviceState.deviceProduct == 0x03){
                                  dismissLoadingDialog();
                               if(deviceState.lockData[1] == 0x0a && deviceState.lockData[2] == 0x01){
                                   isLock = false;
                                   showToast("远程开锁成功！");
                               }
                           }
                       }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    private void fillinPwd(int number){
        if(pwdStack.size() < 6){
            pwdStack.push(number);
            int size = pwdStack.size();
            pwdImg[size - 1].setImageResource(R.drawable.shape_lock_pwd_filled);
        }
    }

    @Override
    public void onBackPressed() {
        EventOfResultDeviceCtrl event = new EventOfResultDeviceCtrl();
        event.deviceInfo = deviceInfo;
        GjjEventBus.getInstance().post(event);
        getActivity().onBackPressed();
    }

    /**
     * 发送开锁命令
     */
    public void senLockOrder()  {
        if(deviceInfo != null){
            byte product = 0x00;
            try {
                int productSrc_int = Integer.decode("0x"+deviceInfo.getCategory());
                 product = (byte)productSrc_int;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(product == 0x02){
                sendCount = 5;
                RestRequestApi.senLockOrder(iotMac,deviceInfo.getMac_address());
            }else if(product == 0x03){
                sendCount = 0;
                byte[] passWord = new byte[6];
             for(int i = 0; i< pwdStack.size(); i++){
                 int number = (Integer) pwdStack.get(i);
                 passWord[i] = (byte) number;
             }
             RestRequestApi.senLoeveaLockOrder(iotMac,deviceInfo.getMac_address(),passWord);
            }
        }
    }
}
