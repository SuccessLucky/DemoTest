package com.kzksmarthome.common.lib.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public enum NetworkState implements Parcelable {
    // actually it is conbination of network type and network state
    WIFI("wifi"), NET_2G("2g"), NET_2G_WAP("2g"), NET_3G("3g"), NET_4G("4g"), UNAVAILABLE("unavailable");
    
    private String name;
    private String operator;
    private String extra;

    NetworkState(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    public String getOperator() {
        return operator;
    }
    public String getExtra() {
        return extra;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean is234G() {
        boolean ret = false;

        if (NetworkState.NET_4G == this || NetworkState.NET_3G == this || NetworkState.NET_2G == this || NetworkState.NET_2G_WAP == this) {
            ret = true;
        }

        return ret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
        dest.writeString(name);
        dest.writeString(operator);
        dest.writeString(extra);
    }
    
    public static final Parcelable.Creator<NetworkState> CREATOR = new Parcelable.Creator<NetworkState>() {
        public NetworkState createFromParcel(Parcel source) {
            NetworkState s = values()[source.readInt()];
            s.name = source.readString();
            s.operator = source.readString();
            s.extra = source.readString();
            return s;
        }

        public NetworkState[] newArray(int size) {
            return new NetworkState[size];
        }
    };
}
