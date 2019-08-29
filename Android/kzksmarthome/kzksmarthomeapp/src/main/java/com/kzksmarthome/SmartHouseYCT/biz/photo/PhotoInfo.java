package com.kzksmarthome.SmartHouseYCT.biz.photo;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

public class PhotoInfo extends BaseModel implements Parcelable {

    public static final int MAX_NUM = 12; // 最多12张
    public static final int CHECKED = 0; // 已选
    public static final int UNCHECK = 1; // 没选
    public String mUrl;
    public int mIsChecked;

    public PhotoInfo() {
    }

    public PhotoInfo(Parcel source) {
        mUrl = source.readString();
        mIsChecked = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeInt(mIsChecked);
    }

    public static final Creator<PhotoInfo> CREATOR = new Creator<PhotoInfo>() {
        public PhotoInfo createFromParcel(Parcel source) {
            return new PhotoInfo(source);
        }

        public PhotoInfo[] newArray(int size) {
            return new PhotoInfo[size];
        }
    };

    @Override
    public String toString() {
        return "PhotoInfo [mUrl=" + mUrl + ", mIsChecked=" + mIsChecked + "]";
    }

}
