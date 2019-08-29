package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @Title: DeviceCommandInfo
 * @Description: 设备命令Bean
 * @author laixj
 * @date 2016/9/11 10:10
 * @version V1.0
 */
public class DeviceCommandInfo extends BaseModel implements Parcelable,Serializable {

    /**
     * 所属网关
     */
    private Integer gwId;
    /**
     * 设备号
     */
    private Integer deviceId;
    /**
     * 设备类型
     */
    private short deviceType;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 命令编号
     */
    private Integer cmdId;
    /**
     * 命令名称
     */
    private String cmdName;

    public DeviceCommandInfo(Integer gwId, Integer deviceId, short deviceType, String deviceName, Integer cmdId, String cmdName) {
        this.gwId = gwId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.cmdId = cmdId;
        this.cmdName = cmdName;
    }

    public DeviceCommandInfo() {
    }

    @Override
    public String toString() {
        return "DeviceCommandInfo{" +
                "gwId=" + gwId +
                ", deviceId=" + deviceId +
                ", deviceType=" + deviceType +
                ", deviceName='" + deviceName + '\'' +
                ", cmdId=" + cmdId +
                ", cmdName='" + cmdName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return getCmdId().intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceCommandInfo cmdInfo = (DeviceCommandInfo) o;
        if (gwId.compareTo(cmdInfo.getGwId()) != 0 || cmdId.compareTo(cmdInfo.getCmdId()) != 0
                || !cmdName.equals(cmdInfo.getCmdName()) || deviceId.compareTo(cmdInfo.getDeviceId()) != 0
                || deviceType != cmdInfo.getDeviceType()) {
            return false;
        }
        return true;
    }

    public Integer getGwId() {
        return gwId;
    }

    public void setGwId(Integer gwId) {
        this.gwId = gwId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public short getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(short deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getCmdId() {
        return cmdId;
    }

    public void setCmdId(Integer cmdId) {
        this.cmdId = cmdId;
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.gwId);
        dest.writeValue(this.deviceId);
        dest.writeInt(this.deviceType);
        dest.writeString(this.deviceName);
        dest.writeValue(this.cmdId);
        dest.writeString(this.cmdName);
    }

    protected DeviceCommandInfo(Parcel in) {
        this.gwId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.deviceId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.deviceType = (short) in.readInt();
        this.deviceName = in.readString();
        this.cmdId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cmdName = in.readString();
    }

    public static final Creator<DeviceCommandInfo> CREATOR = new Creator<DeviceCommandInfo>() {
        @Override
        public DeviceCommandInfo createFromParcel(Parcel source) {
            return new DeviceCommandInfo(source);
        }

        @Override
        public DeviceCommandInfo[] newArray(int size) {
            return new DeviceCommandInfo[size];
        }
    };
}
