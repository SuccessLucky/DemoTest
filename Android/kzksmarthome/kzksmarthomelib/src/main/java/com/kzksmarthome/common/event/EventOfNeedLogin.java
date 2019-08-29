package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 需要重新登陆事件
 */
public class EventOfNeedLogin implements Parcelable {

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public EventOfNeedLogin() {
	}

	protected EventOfNeedLogin(Parcel in) {
	}

	public static final Creator<EventOfNeedLogin> CREATOR = new Creator<EventOfNeedLogin>() {
		@Override
		public EventOfNeedLogin createFromParcel(Parcel source) {
			return new EventOfNeedLogin(source);
		}

		@Override
		public EventOfNeedLogin[] newArray(int size) {
			return new EventOfNeedLogin[size];
		}
	};
}