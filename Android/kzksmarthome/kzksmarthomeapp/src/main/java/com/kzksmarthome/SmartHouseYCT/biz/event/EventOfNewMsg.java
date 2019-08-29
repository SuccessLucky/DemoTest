package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 添加蓝牙设备
 */
public class EventOfNewMsg implements Parcelable {
    

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public EventOfNewMsg() {
	}

	private EventOfNewMsg(Parcel in) {
	}

	public static final Creator<EventOfNewMsg> CREATOR = new Creator<EventOfNewMsg>() {
		public EventOfNewMsg createFromParcel(Parcel source) {
			return new EventOfNewMsg(source);
		}

		public EventOfNewMsg[] newArray(int size) {
			return new EventOfNewMsg[size];
		}
	};
}