package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.IOTSelectInfo;

import java.util.ArrayList;

/**
 * 选择网关
 * Created by jack on 2016/11/1.
 */

public class EventOfSelectIOT implements Parcelable {
    private ArrayList<IOTSelectInfo> iotSelectInfoArrayList;

    public ArrayList<IOTSelectInfo> getIotSelectInfoArrayList() {
        return iotSelectInfoArrayList;
    }

    public void setIotSelectInfoArrayList(ArrayList<IOTSelectInfo> iotSelectInfoArrayList) {
        this.iotSelectInfoArrayList = iotSelectInfoArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.iotSelectInfoArrayList);
    }

    public EventOfSelectIOT() {
    }

    protected EventOfSelectIOT(Parcel in) {
        this.iotSelectInfoArrayList = new ArrayList<IOTSelectInfo>();
        in.readList(this.iotSelectInfoArrayList, IOTSelectInfo.class.getClassLoader());
    }

    public static final Creator<EventOfSelectIOT> CREATOR = new Creator<EventOfSelectIOT>() {
        @Override
        public EventOfSelectIOT createFromParcel(Parcel source) {
            return new EventOfSelectIOT(source);
        }

        @Override
        public EventOfSelectIOT[] newArray(int size) {
            return new EventOfSelectIOT[size];
        }
    };
}
