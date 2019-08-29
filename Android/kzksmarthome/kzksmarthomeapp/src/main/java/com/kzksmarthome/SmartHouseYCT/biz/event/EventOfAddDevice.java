package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 添加蓝牙设备
 */
public class EventOfAddDevice implements Parcelable {

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public EventOfAddDevice() {
	}

	private EventOfAddDevice(Parcel in) {
	}

	public static final Creator<EventOfAddDevice> CREATOR = new Creator<EventOfAddDevice>() {
		public EventOfAddDevice createFromParcel(Parcel source) {
			return new EventOfAddDevice(source);
		}

		public EventOfAddDevice[] newArray(int size) {
			return new EventOfAddDevice[size];
		}
	};
}