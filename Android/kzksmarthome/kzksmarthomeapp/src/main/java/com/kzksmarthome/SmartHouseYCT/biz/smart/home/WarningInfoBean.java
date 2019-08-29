package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * 告警信息
 * Created by jack on 2016/10/29.
 */

public class WarningInfoBean extends BaseModel implements Parcelable {
    private int alarm_id;
    private String create_date;
    private String alarm_msg;

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getAlarm_msg() {
        return alarm_msg;
    }

    public void setAlarm_msg(String alarm_msg) {
        this.alarm_msg = alarm_msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.alarm_id);
        dest.writeString(this.create_date);
        dest.writeString(this.alarm_msg);
    }

    public WarningInfoBean() {
    }

    protected WarningInfoBean(Parcel in) {
        this.alarm_id = in.readInt();
        this.create_date = in.readString();
        this.alarm_msg = in.readString();
    }

    public static final Creator<WarningInfoBean> CREATOR = new Creator<WarningInfoBean>() {
        @Override
        public WarningInfoBean createFromParcel(Parcel source) {
            return new WarningInfoBean(source);
        }

        @Override
        public WarningInfoBean[] newArray(int size) {
            return new WarningInfoBean[size];
        }
    };
}
