package com.kzksmarthome.common.lib.easylink.modle;


import com.kzksmarthome.common.lib.easylink.bean.Modle;

/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class Modle_Node_Zb_Unknow extends Modle {
    public Modle_Node_Zb_Unknow(String mac, String ucDeviceType, String ucSensorType, String ucWorkMode, String ucWorkState, String ucOD_Upload_Interval_0, String ucOD_Upload_Interval_1, String ucOD_Area, String ucOD_Func, String uiBATVoltage, String ucRSSI, String ucLQI, String others) {
        this.mac=mac;
        this.ucDeviceType=ucDeviceType;
        this.ucSensorType = ucSensorType;
        this.ucWorkMode = ucWorkMode;
        this.ucWorkState = ucWorkState;
        this.ucOD_Upload_Interval_0 = ucOD_Upload_Interval_0;
        this.ucOD_Upload_Interval_1 = ucOD_Upload_Interval_1;
        this.ucOD_Area = ucOD_Area;
        this.ucOD_Func = ucOD_Func;
        this.uiBATVoltage = uiBATVoltage;
        this.ucRSSI = ucRSSI;
        this.ucLQI = ucLQI;
        this.others=others;
    }
}
