package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

public class EventOfDesignPhotoRead implements Parcelable {

    public int mIndex;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mIndex);
    }

    public EventOfDesignPhotoRead() {
    }

    private EventOfDesignPhotoRead(Parcel in) {
        this.mIndex = in.readInt();
    }

    public static final Creator<EventOfDesignPhotoRead> CREATOR = new Creator<EventOfDesignPhotoRead>() {
        public EventOfDesignPhotoRead createFromParcel(Parcel source) {
            return new EventOfDesignPhotoRead(source);
        }

        public EventOfDesignPhotoRead[] newArray(int size) {
            return new EventOfDesignPhotoRead[size];
        }
    };
}
