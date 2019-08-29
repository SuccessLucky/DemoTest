package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @Description: 日志上传结果通知
 * @Copyright: Copyright (c) 2015
 * @version: 1.0.0.0
 * @author: jack
 * @createDate 2015-5-20
 *
 */
public class EventOfUpLoadLog implements Parcelable {
    
    public boolean upLoadState;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(upLoadState ? (byte) 1 : (byte) 0);
    }

    public EventOfUpLoadLog() {
    }

    private EventOfUpLoadLog(Parcel in) {
        this.upLoadState = in.readByte() != 0;
    }

    public static final Parcelable.Creator<EventOfUpLoadLog> CREATOR = new Parcelable.Creator<EventOfUpLoadLog>() {
        public EventOfUpLoadLog createFromParcel(Parcel source) {
            return new EventOfUpLoadLog(source);
        }

        public EventOfUpLoadLog[] newArray(int size) {
            return new EventOfUpLoadLog[size];
        }
    };
}
