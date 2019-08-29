package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 修改了服务器地址事件
 * 
 * @author panrq
 * @createDate 2015-4-29
 *
 */
public class EventOfChangeDebugMode implements Parcelable {
    public boolean mOpen;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mOpen ? 1 : 0);
    }

    public EventOfChangeDebugMode(boolean open) {
        mOpen = open;
    }

    private EventOfChangeDebugMode(Parcel in) {
        mOpen = in.readInt() == 1 ? true : false;
    }

    public static final Creator<EventOfChangeDebugMode> CREATOR = new Creator<EventOfChangeDebugMode>() {
        public EventOfChangeDebugMode createFromParcel(Parcel source) {
            return new EventOfChangeDebugMode(source);
        }

        public EventOfChangeDebugMode[] newArray(int size) {
            return new EventOfChangeDebugMode[size];
        }
    };
}
