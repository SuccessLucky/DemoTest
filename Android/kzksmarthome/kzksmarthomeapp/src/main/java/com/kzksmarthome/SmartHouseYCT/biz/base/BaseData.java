package com.kzksmarthome.SmartHouseYCT.biz.base;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseData<T extends Serializable> extends BaseRecyclerItemData implements Parcelable {

    public T mData;

    public BaseData() {

    }

    @SuppressWarnings("unchecked")
    public BaseData(Parcel source) {
        mViewType = source.readInt();
        mData = (T) source.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mViewType);
        dest.writeSerializable(mData);
    }

    @SuppressWarnings("rawtypes")
    public static final Creator<BaseData> CREATOR = new Creator<BaseData>() {
        public BaseData createFromParcel(Parcel source) {
            return new BaseData(source);
        }

        public BaseData[] newArray(int size) {
            return new BaseData[size];
        }
    };

    @Override
    public String toString() {
        return "BaseData [mData=" + mData + "]";
    }

}
