package com.kzksmarthome.common.lib.easylink.bean;


/**
 * Created by Administrator on 2016/9/12 0012.
 */

public class Modle {
    protected int sqlflag;

    protected int id;
    protected String fatherMac;  //wifi下的zb的mac地址
    protected String flag;  //设备总类别
    protected String modle_name;  //显示名字
    protected String custom_name;  //显示名字
    protected String mac;  //节点mac
    protected String time;

    protected String ucDeviceType; //设备类型
    protected String ucSensorType; //产品类别
    protected String ucWorkMode; //工作模式
    protected String ucWorkState; //测试字状态
    protected String ucOD_Upload_Interval_0; //定时上报时间间隔
    protected String ucOD_Upload_Interval_1; //定时上报偏移时间
    protected String ucOD_Area; //区域信息
    protected String ucOD_Func; //功能信息
    protected String uiBATVoltage; //设备电压值0V
    protected String ucRSSI; //信号强度
    protected String ucLQI; //信号连接质量
    protected String others; //信号连接质量

    public Modle() {
    }

    public Modle(String flag, String modle_name, String mac, String time, String ucDeviceType, String ucSensorType, String ucWorkMode, String ucWorkState, String ucOD_Upload_Interval_0, String ucOD_Upload_Interval_1, String ucOD_Area, String ucOD_Func, String uiBATVoltage, String ucRSSI, String ucLQI, String others) {
        this.flag = flag;
        this.modle_name = modle_name;
        this.mac = mac;
        this.time = time;
        this.ucDeviceType = ucDeviceType;
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
        this.custom_name="";
        this.others=others;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }


    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getFatherMac() {
        return fatherMac;
    }

    public int getSqlflag() {
        return sqlflag;
    }

    public void setSqlflag(int sqlflag) {
        this.sqlflag = sqlflag;
    }

    public void setFatherMac(String fatherMac) {
        this.fatherMac = fatherMac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getModle_name() {
        return modle_name;
    }

    public void setModle_name(String modle_name) {
        this.modle_name = modle_name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUcDeviceType() {
        return ucDeviceType;
    }

    public void setUcDeviceType(String ucDeviceType) {
        this.ucDeviceType = ucDeviceType;
    }

    public String getUcSensorType() {
        return ucSensorType;
    }

    public void setUcSensorType(String ucSensorType) {
        this.ucSensorType = ucSensorType;
    }

    public String getUcWorkMode() {
        return ucWorkMode;
    }

    public void setUcWorkMode(String ucWorkMode) {
        this.ucWorkMode = ucWorkMode;
    }

    public String getUcWorkState() {
        return ucWorkState;
    }

    public void setUcWorkState(String ucWorkState) {
        this.ucWorkState = ucWorkState;
    }

    public String getUcOD_Upload_Interval_0() {
        return ucOD_Upload_Interval_0;
    }

    public void setUcOD_Upload_Interval_0(String ucOD_Upload_Interval_0) {
        this.ucOD_Upload_Interval_0 = ucOD_Upload_Interval_0;
    }

    public String getUcOD_Upload_Interval_1() {
        return ucOD_Upload_Interval_1;
    }

    public void setUcOD_Upload_Interval_1(String ucOD_Upload_Interval_1) {
        this.ucOD_Upload_Interval_1 = ucOD_Upload_Interval_1;
    }

    public String getUcOD_Area() {
        return ucOD_Area;
    }

    public void setUcOD_Area(String ucOD_Area) {
        this.ucOD_Area = ucOD_Area;
    }

    public String getUcOD_Func() {
        return ucOD_Func;
    }

    public void setUcOD_Func(String ucOD_Func) {
        this.ucOD_Func = ucOD_Func;
    }

    public String getUiBATVoltage() {
        return uiBATVoltage;
    }

    public void setUiBATVoltage(String uiBATVoltage) {
        this.uiBATVoltage = uiBATVoltage;
    }

    public String getUcRSSI() {
        return ucRSSI;
    }

    public void setUcRSSI(String ucRSSI) {
        this.ucRSSI = ucRSSI;
    }

    public String getUcLQI() {
        return ucLQI;
    }

    public void setUcLQI(String ucLQI) {
        this.ucLQI = ucLQI;
    }
}
