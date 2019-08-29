package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

public class EventOfResultAddress implements Parcelable {
    /**
     * 地址
     */
    public String AddressStr;
    /**
     * 地址编号
     */
    public String AddressId; 
    /**
     * 小区
     */
    public String AddressXQ; 

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AddressStr);
        dest.writeString(this.AddressId);
        dest.writeString(this.AddressXQ);
    }

    public EventOfResultAddress() {
    }

    private EventOfResultAddress(Parcel in) {
        this.AddressStr = in.readString();
        this.AddressId = in.readString();
        this.AddressXQ = in.readString();
    }

    public static final Creator<EventOfResultAddress> CREATOR = new Creator<EventOfResultAddress>() {
        public EventOfResultAddress createFromParcel(Parcel source) {
            return new EventOfResultAddress(source);
        }

        public EventOfResultAddress[] newArray(int size) {
            return new EventOfResultAddress[size];
        }
    };
}
