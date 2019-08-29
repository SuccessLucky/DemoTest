package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

public class EventOfMarkMsgRead implements Parcelable {

    public int[] mMsgIds;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMsgIds.length); //先将数组长度写入
        dest.writeIntArray(mMsgIds);
    }

    public EventOfMarkMsgRead() {
    }

    private EventOfMarkMsgRead(Parcel in) {
        int length = in.readInt();
        mMsgIds = new int[length];
        in.readIntArray(mMsgIds);
    }

    public static final Creator<EventOfMarkMsgRead> CREATOR = new Creator<EventOfMarkMsgRead>() {
        public EventOfMarkMsgRead createFromParcel(Parcel source) {
            return new EventOfMarkMsgRead(source);
        }

        public EventOfMarkMsgRead[] newArray(int size) {
            return new EventOfMarkMsgRead[size];
        }
    };
}
