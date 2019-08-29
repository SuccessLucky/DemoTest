package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

public class EventOfActivityStatus implements Parcelable {

	public static final int STATUS_RESUME = 0;
	public static final int STATUS_PAUSE = 1;

	private int status = -1;

	public EventOfActivityStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(status);
	}

	public EventOfActivityStatus() {
	}

	private EventOfActivityStatus(Parcel in) {
		this.status = in.readInt();
	}

	public static final Creator<EventOfActivityStatus> CREATOR = new Creator<EventOfActivityStatus>() {
		public EventOfActivityStatus createFromParcel(Parcel source) {
			return new EventOfActivityStatus(source);
		}

		public EventOfActivityStatus[] newArray(int size) {
			return new EventOfActivityStatus[size];
		}
	};
}