package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 网络连接返回
 * Created by jack on 2016/11/12.
 */

public class EventOfSocket implements Parcelable {
    /**
     * 连接失败
     */
    private boolean mIsConnectFail;

    public boolean ismIsConnectFail() {
        return mIsConnectFail;
    }

    public void setmIsConnectFail(boolean mIsConnectFail) {
        this.mIsConnectFail = mIsConnectFail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mIsConnectFail ? (byte) 1 : (byte) 0);
    }

    public EventOfSocket() {
    }

    protected EventOfSocket(Parcel in) {
        this.mIsConnectFail = in.readByte() != 0;
    }

    public static final Parcelable.Creator<EventOfSocket> CREATOR = new Parcelable.Creator<EventOfSocket>() {
        @Override
        public EventOfSocket createFromParcel(Parcel source) {
            return new EventOfSocket(source);
        }

        @Override
        public EventOfSocket[] newArray(int size) {
            return new EventOfSocket[size];
        }
    };

}
