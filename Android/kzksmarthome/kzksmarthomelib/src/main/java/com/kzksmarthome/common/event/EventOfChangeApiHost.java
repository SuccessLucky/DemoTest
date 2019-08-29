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
public class EventOfChangeApiHost implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfChangeApiHost() {
    }

    private EventOfChangeApiHost(Parcel in) {
    }

    public static final Parcelable.Creator<EventOfChangeApiHost> CREATOR = new Parcelable.Creator<EventOfChangeApiHost>() {
        public EventOfChangeApiHost createFromParcel(Parcel source) {
            return new EventOfChangeApiHost(source);
        }

        public EventOfChangeApiHost[] newArray(int size) {
            return new EventOfChangeApiHost[size];
        }
    };
}
