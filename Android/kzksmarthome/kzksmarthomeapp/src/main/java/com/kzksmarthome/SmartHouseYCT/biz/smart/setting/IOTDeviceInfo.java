package com.kzksmarthome.SmartHouseYCT.biz.smart.setting;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * 网关设备信息
 * Created by jack on 2016/9/10.
 */
public class IOTDeviceInfo extends BaseModel implements Parcelable {

    /**
     * deviceName : mytest#c65fd3
     * deviceMac : c8:93:46:c6:5f:d3
     * deviceIP : 192.168.16.107
     * deviceMacbind : 0
     * hardwareID : 0
     * fogProductID : 0
     * isEasyLinkOK : 0
     * isHaveSuperUser : 0
     * remainingUserNumber : 0
     * allInfo : Vendor=MXCHIPFirmware=31620303.023Socket1_Type=TCP ServerSocket1_Port=3000Socket2_Type=TCP ClientSocket2_Port=7002Socket2_Raddr=120.24.55.76MAC=c8:93:46:c6:5f:d3Protocol=com.mxchip.sppHardware=EMW3162Seed=10
     * devicePort : 8089
     */

    private String deviceName;
    private String deviceMac;
    private String deviceIP;
    private String deviceMacbind;
    private String hardwareID;
    private String fogProductID;
    private String isEasyLinkOK;
    private String isHaveSuperUser;
    private String remainingUserNumber;
    private String allInfo;
    private String devicePort;



    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public String getDeviceMacbind() {
        return deviceMacbind;
    }

    public void setDeviceMacbind(String deviceMacbind) {
        this.deviceMacbind = deviceMacbind;
    }

    public String getHardwareID() {
        return hardwareID;
    }

    public void setHardwareID(String hardwareID) {
        this.hardwareID = hardwareID;
    }

    public String getFogProductID() {
        return fogProductID;
    }

    public void setFogProductID(String fogProductID) {
        this.fogProductID = fogProductID;
    }

    public String getIsEasyLinkOK() {
        return isEasyLinkOK;
    }

    public void setIsEasyLinkOK(String isEasyLinkOK) {
        this.isEasyLinkOK = isEasyLinkOK;
    }

    public String getIsHaveSuperUser() {
        return isHaveSuperUser;
    }

    public void setIsHaveSuperUser(String isHaveSuperUser) {
        this.isHaveSuperUser = isHaveSuperUser;
    }

    public String getRemainingUserNumber() {
        return remainingUserNumber;
    }

    public void setRemainingUserNumber(String remainingUserNumber) {
        this.remainingUserNumber = remainingUserNumber;
    }

    public String getAllInfo() {
        return allInfo;
    }

    public void setAllInfo(String allInfo) {
        this.allInfo = allInfo;
    }

    public String getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceName);
        dest.writeString(this.deviceMac);
        dest.writeString(this.deviceIP);
        dest.writeString(this.deviceMacbind);
        dest.writeString(this.hardwareID);
        dest.writeString(this.fogProductID);
        dest.writeString(this.isEasyLinkOK);
        dest.writeString(this.isHaveSuperUser);
        dest.writeString(this.remainingUserNumber);
        dest.writeString(this.allInfo);
        dest.writeString(this.devicePort);
    }

    public IOTDeviceInfo() {
    }

    protected IOTDeviceInfo(Parcel in) {
        this.deviceName = in.readString();
        this.deviceMac = in.readString();
        this.deviceIP = in.readString();
        this.deviceMacbind = in.readString();
        this.hardwareID = in.readString();
        this.fogProductID = in.readString();
        this.isEasyLinkOK = in.readString();
        this.isHaveSuperUser = in.readString();
        this.remainingUserNumber = in.readString();
        this.allInfo = in.readString();
        this.devicePort = in.readString();
    }

    public static final Creator<IOTDeviceInfo> CREATOR = new Creator<IOTDeviceInfo>() {
        @Override
        public IOTDeviceInfo createFromParcel(Parcel source) {
            return new IOTDeviceInfo(source);
        }

        @Override
        public IOTDeviceInfo[] newArray(int size) {
            return new IOTDeviceInfo[size];
        }
    };

    @Override
    public String toString() {
        return "IOTDeviceInfo{" +
                "deviceName='" + deviceName + '\'' +
                ", deviceMac='" + deviceMac + '\'' +
                ", deviceIP='" + deviceIP + '\'' +
                ", deviceMacbind='" + deviceMacbind + '\'' +
                ", hardwareID='" + hardwareID + '\'' +
                ", fogProductID='" + fogProductID + '\'' +
                ", isEasyLinkOK='" + isEasyLinkOK + '\'' +
                ", isHaveSuperUser='" + isHaveSuperUser + '\'' +
                ", remainingUserNumber='" + remainingUserNumber + '\'' +
                ", allInfo='" + allInfo + '\'' +
                ", devicePort='" + devicePort + '\'' +
                '}';
    }
}
