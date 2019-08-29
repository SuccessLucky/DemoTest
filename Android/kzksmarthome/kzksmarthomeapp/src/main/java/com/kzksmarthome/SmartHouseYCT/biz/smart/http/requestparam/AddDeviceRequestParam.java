package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.util.List;

/**
 * 添加设备 AddDeviceRequestParam
 * @author lizhid
 * @version V1.0
 * @description:
 * @date 2016/10/31
 */
public class AddDeviceRequestParam extends BaseModel{
    private List<AddDeviceBean> devices;

    public List<AddDeviceBean> getDevices() {
        return devices;
    }

    public void setDevices(List<AddDeviceBean> devices) {
        this.devices = devices;
    }


}
