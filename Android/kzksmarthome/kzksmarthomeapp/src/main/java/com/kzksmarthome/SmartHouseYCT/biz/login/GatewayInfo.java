package com.kzksmarthome.SmartHouseYCT.biz.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * Created by jack on 2016/10/9.
 */
public class GatewayInfo extends BaseModel implements Parcelable {

    /**
     * gateway_id : 1
     * mac_address : f4:5c:89:c4:12:8d
     * member_type : 1
     * gateway_name : 我爱我家
     */

    private int gateway_id;
    private String mac_address;
    private int member_type;
    private String gateway_name;



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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gateway_id);
        dest.writeString(this.mac_address);
        dest.writeInt(this.member_type);
        dest.writeString(this.gateway_name);
    }

    public GatewayInfo() {
    }

    protected GatewayInfo(Parcel in) {
        this.gateway_id = in.readInt();
        this.mac_address = in.readString();
        this.member_type = in.readInt();
        this.gateway_name = in.readString();
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

    @Override
    public String toString() {
        return "GatewayInfo{" +
                "gateway_id=" + gateway_id +
                ", mac_address='" + mac_address + '\'' +
                ", member_type=" + member_type +
                ", gateway_name='" + gateway_name + '\'' +
                '}';
    }
}
