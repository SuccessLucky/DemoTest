package com.kzksmarthome.SmartHouseYCT.util;

/**
 * @author laixj
 * @version V1.0
 * @Title: DeviceTypeEnums
 * @Description: 设备类型枚举
 * @date 2016/9/8 22:07
 */
public enum DeviceTypeEnums {
    DROPLIGHT((short) 0, DeviceCtrTypeEnums.NORMAL.getCode(), "三路设备"),
    TABLELAMP((short) 1, DeviceCtrTypeEnums.NORMAL.getCode(), "二路设备"),
    CURTAIN((short) 2, DeviceCtrTypeEnums.NORMAL.getCode(), "普通窗帘"),
    DOOR((short) 3, DeviceCtrTypeEnums.NORMAL.getCode(), "门"),
    WINDOWOPENER((short) 4, DeviceCtrTypeEnums.NORMAL.getCode(), "协议转发开窗器"),
    ELECTRICCURTAIN((short) 5, DeviceCtrTypeEnums.NORMAL.getCode(), "电动窗帘"),
    LIGHT((short) 6, DeviceCtrTypeEnums.NORMAL.getCode(), "一路设备"),
    SMOKESENSOR((short) 7, DeviceCtrTypeEnums.NORMAL.getCode(), "烟雾感应器"),
    GASSENSOR((short) 8, DeviceCtrTypeEnums.NORMAL.getCode(), "燃气感应器"),
    WARTERCONTROLER((short) 9, DeviceCtrTypeEnums.NORMAL.getCode(), "水浸控制器"),
    MOBILEOUTLET((short) 10, DeviceCtrTypeEnums.NORMAL.getCode(), "移动插座"),
    DOORANDWINDOW((short) 11, DeviceCtrTypeEnums.NORMAL.getCode(), "门窗磁"),
    WINDRAIN((short) 12, DeviceCtrTypeEnums.NORMAL.getCode(), "风雨感应器"),
    TRANSLATWINDOW((short) 14, DeviceCtrTypeEnums.NORMAL.getCode(), "平移开窗器"),
    SOUNDLIGHT((short) 15, DeviceCtrTypeEnums.NORMAL.getCode(), "声光报警"),
    LOCK((short) 16, DeviceCtrTypeEnums.NORMAL.getCode(), "指纹锁"),
    PUSHWINDOW((short) 17, DeviceCtrTypeEnums.NORMAL.getCode(), "推拉窗"),
    TEMPANDHUMIDITY((short) 18, DeviceCtrTypeEnums.NORMAL.getCode(), "温湿度"),
    OUTLET((short) 19, DeviceCtrTypeEnums.NORMAL.getCode(), "86插座"),
    JLOUTLET((short) 20, DeviceCtrTypeEnums.NORMAL.getCode(), "计量插座"),
    CONTROLBOX((short) 21, DeviceCtrTypeEnums.NORMAL.getCode(), "控制盒"),
    THECURTAIN((short) 22, DeviceCtrTypeEnums.NORMAL.getCode(), "幕布"),
    PROJECTIONFRAME((short) 23, DeviceCtrTypeEnums.NORMAL.getCode(), "投影架"),
    MANIPULATOR((short) 24, DeviceCtrTypeEnums.NORMAL.getCode(), "机械手控制器"),
    ELECTRICGLASS((short) 25, DeviceCtrTypeEnums.NORMAL.getCode(), "电动玻璃"),
    LIGHTMODULATIONLAMP((short) 26, DeviceCtrTypeEnums.NORMAL.getCode(), "1路调光灯"),
    SOUNDANDLIGHTALARM((short) 27, DeviceCtrTypeEnums.NORMAL.getCode(), "声光报警器"),
    SCENECONTROL((short) 28, DeviceCtrTypeEnums.NORMAL.getCode(), "场景控制器"),
    LUMINANCESENSOR((short) 29, DeviceCtrTypeEnums.NORMAL.getCode(), "亮度传感器"),
    REMOTECONTROL((short) 30, DeviceCtrTypeEnums.NORMAL.getCode(), "遥控设备"),
    ELECTRICBED((short) 31, DeviceCtrTypeEnums.NORMAL.getCode(), "电动床"),
    FRESHAIR((short) 32, DeviceCtrTypeEnums.NORMAL.getCode(), "新风"),
    NFRAREDINDUCTION((short) 33, DeviceCtrTypeEnums.NORMAL.getCode(), "红外感应设备"),
    SIXWAYPANEL((short) 34, DeviceCtrTypeEnums.NORMAL.getCode(), "六路面板"),
    COLORFULBULB((short) 35, DeviceCtrTypeEnums.NORMAL.getCode(), "多彩球泡灯"),
    COLORFULLAMP((short) 36, DeviceCtrTypeEnums.NORMAL.getCode(), "多彩冷暖灯"),
    PYROELECTRICINFRARED((short) 37, DeviceCtrTypeEnums.NORMAL.getCode(), "人体热释电红外"),
    MAGNETICWINDOW((short) 38, DeviceCtrTypeEnums.NORMAL.getCode(), "窗磁"),
    WATERIMMERSIONSENSOR((short) 39, DeviceCtrTypeEnums.NORMAL.getCode(), "水浸传感器"),
    SINGLEMETER((short) 40, DeviceCtrTypeEnums.NORMAL.getCode(), "单相电表"),
    METERINGSWITCH((short) 41, DeviceCtrTypeEnums.NORMAL.getCode(), "计量控制盒"),
    THREECOMPARTMENTAMMETER((short) 42, DeviceCtrTypeEnums.NORMAL.getCode(), "三厢电表"),
    METERINGSOCKET10((short) 43, DeviceCtrTypeEnums.NORMAL.getCode(), "计量插座（10A）"),
    METERINGSOCKET16((short) 44, DeviceCtrTypeEnums.NORMAL.getCode(), "计量插座（16A）"),
    SOSBUTTON((short) 45, DeviceCtrTypeEnums.NORMAL.getCode(), "SOS开关"),

    FORWARD((short) 100, DeviceCtrTypeEnums.NORMAL.getCode(), "协议转发"),
    AIRCONDITION((short) 101, DeviceCtrTypeEnums.AIRCONDITION.getCode(), "空调"),
    INFRARED((short) 102, DeviceCtrTypeEnums.NORMAL.getCode(), "红外转发设备");



    private short code;
    private String ctrtype;
    private String name;

    private DeviceTypeEnums(short code, String ctrype, String name) {
        this.code = code;
        this.ctrtype = ctrype;
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public String getCtrtype() {
        return ctrtype;
    }

    public void setCtrtype(String ctrtype) {
        this.ctrtype = ctrtype;
    }

    public String getName() {
        return name;
    }
}
