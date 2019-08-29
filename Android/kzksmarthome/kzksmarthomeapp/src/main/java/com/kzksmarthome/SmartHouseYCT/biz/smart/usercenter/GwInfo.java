package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * @Title: GwInfo
 * @Description: 网关信息Bean
 * @author laixj
 * @date 2016/9/14 7:01
 * @version V1.0
 */
public class GwInfo extends BaseModel implements  Parcelable {

    /**
     * 网关编号
     */
    private Integer gwId;
    /**
     * 网关名称
     */
    private String gwName;
    /**
     * wifi名称
     */
    private String wifiName;
    /**
     * wifi密码
     */
    private String wifiPwd;

    public GwInfo(){}


    public Integer getGwId() {
        return gwId;
    }

    public void setGwId(Integer gwId) {
        this.gwId = gwId;
    }

    public String getGwName() {
        return gwName;
    }

    public void setGwName(String gwName) {
        this.gwName = gwName;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiPwd() {
        return wifiPwd;
    }

    public void setWifiPwd(String wifiPwd) {
        this.wifiPwd = wifiPwd;
    }

    @Override
    public String toString() {
        return "GwInfo{" +
                "gwId=" + gwId +
                ", gwName='" + gwName + '\'' +
                ", wifiName='" + wifiName + '\'' +
                ", wifiPwd='" + wifiPwd + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return gwId.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GwInfo gwInfo = (GwInfo) o;
        if (gwId.compareTo(gwInfo.getGwId()) != 0 || gwName.compareTo(gwInfo.getGwName()) != 0) {
            return false;
        }
        return true;
    }

    public GwInfo(Integer gwId, String gwName, String wifiName, String wifiPwd) {
        this.gwId = gwId;
        this.gwName = gwName;
        this.wifiName = wifiName;
        this.wifiPwd = wifiPwd;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.gwId);
        dest.writeString(this.gwName);
        dest.writeString(this.wifiName);
        dest.writeString(this.wifiPwd);
    }

    protected GwInfo(Parcel in) {
        this.gwId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.gwName = in.readString();
        this.wifiName = in.readString();
        this.wifiPwd = in.readString();
    }

    public static final Creator<GwInfo> CREATOR = new Creator<GwInfo>() {
        @Override
        public GwInfo createFromParcel(Parcel source) {
            return new GwInfo(source);
        }

        @Override
        public GwInfo[] newArray(int size) {
            return new GwInfo[size];
        }
    };
}
