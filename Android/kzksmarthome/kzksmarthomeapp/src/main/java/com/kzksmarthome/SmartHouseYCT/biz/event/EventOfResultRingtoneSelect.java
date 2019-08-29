package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.RingtoneInfo;

public class EventOfResultRingtoneSelect implements Parcelable {
    /**
     * 选中的铃声
     */
    public RingtoneInfo ringtoneInfo;
    public String type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ringtoneInfo, flags);
        dest.writeString(this.type);
    }

    public EventOfResultRingtoneSelect() {
    }

    protected EventOfResultRingtoneSelect(Parcel in) {
        this.ringtoneInfo = in.readParcelable(RingtoneInfo.class.getClassLoader());
        this.type = in.readString();
    }

    public static final Creator<EventOfResultRingtoneSelect> CREATOR = new Creator<EventOfResultRingtoneSelect>() {
        @Override
        public EventOfResultRingtoneSelect createFromParcel(Parcel source) {
            return new EventOfResultRingtoneSelect(source);
        }

        @Override
        public EventOfResultRingtoneSelect[] newArray(int size) {
            return new EventOfResultRingtoneSelect[size];
        }
    };
}
