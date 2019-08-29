package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @Title: GatewayInfo
 * @Description: 网关信息Bean
 * @author laixj
 * @date 2016/10/10 20:19
 * @version V1.0
 */
public class GatewayInfo extends BaseModel implements Parcelable,Serializable {
    /**
     * gateway_id : 2
     * mac_address : f4:5c:89:c4:12:9d
     * member_type : 0
     * gateway_name : 我家
     */

    private int gateway_id;
    private String mac_address;
    private String wifi_mac_address;
    private int member_type;
    private String gateway_name;
    private int security_status;
    /**
     * 视图类型
     */
    private int viewType;

    public GatewayInfo(int gateway_id, String mac_address, String gateway_name) {
        this.gateway_id = gateway_id;
        this.mac_address = mac_address;
        this.gateway_name = gateway_name;
    }

    @Override
    public int hashCode() {
        return gateway_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GatewayInfo that = (GatewayInfo) o;

        if (gateway_id != that.gateway_id) return false;
        return mac_address.equals(that.mac_address);

    }

    @Override
    public String toString() {
        return "GatewayInfo{" +
                "gateway_id=" + gateway_id +
                ", mac_address='" + mac_address + '\'' +
                ", wifi_mac_address='" + wifi_mac_address + '\'' +
                ", member_type=" + member_type +
                ", gateway_name='" + gateway_name + '\'' +
                ", security_status=" + security_status +
                '}';
    }

    public int getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(int gateway_id) {
        this.gateway_id = gateway_id;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public int getMember_type() {
        return member_type;
    }

    public void setMember_type(int member_type) {
        this.member_type = member_type;
    }

    public String getGateway_name() {
        return gateway_name;
    }

    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
    }

    public String getWifi_mac_address() {
        return wifi_mac_address;
    }

    public void setWifi_mac_address(String wifi_mac_address) {
        this.wifi_mac_address = wifi_mac_address;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getSecurity_status() {
        return security_status;
    }

    public void setSecurity_status(int security_status) {
        this.security_status = security_status;
    }

    public GatewayInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gateway_id);
        dest.writeString(this.mac_address);
        dest.writeString(this.wifi_mac_address);
        dest.writeInt(this.member_type);
        dest.writeString(this.gateway_name);
        dest.writeInt(this.security_status);
        dest.writeInt(this.viewType);
    }

    protected GatewayInfo(Parcel in) {
        this.gateway_id = in.readInt();
        this.mac_address = in.readString();
        this.wifi_mac_address = in.readString();
        this.member_type = in.readInt();
        this.gateway_name = in.readString();
        this.security_status = in.readInt();
        this.viewType = in.readInt();
    }

    public static final Creator<GatewayInfo> CREATOR = new Creator<GatewayInfo>() {
        @Override
        public GatewayInfo createFromParcel(Parcel source) {
            return new GatewayInfo(source);
        }

        @Override
        public GatewayInfo[] newArray(int size) {
            return new GatewayInfo[size];
        }
    };
}
