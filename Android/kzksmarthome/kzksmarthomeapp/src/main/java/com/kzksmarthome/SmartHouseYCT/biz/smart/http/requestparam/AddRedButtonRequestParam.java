package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceButtonInfo;

import java.util.List;

/**
 * @Title: AddRedButtonRequestParam
 * @Description: 增加红外按钮
 * @author jack
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class AddRedButtonRequestParam {

    /**
     * device_id : 1
     * device_buttons : [{"button_id":1,"name":"音量加","instruction_code":"0001x11"},{"button_id":2,"name":"音量减","instruction_code":"0001x12"}]
     */

    private int device_id;
    /**
     * button_id : 1
     * name : 音量加
     * instruction_code : 0001x11
     */

    private List<DeviceButtonInfo> device_buttons;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public List<DeviceButtonInfo> getDevice_buttons() {
        return device_buttons;
    }

    public void setDevice_buttons(List<DeviceButtonInfo> device_buttons) {
        this.device_buttons = device_buttons;
    }
}
