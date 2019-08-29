package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @author laixj
 * @version V1.0
 * @Title: DoorAccessInfo
 * @Description: 门禁信息Bean
 * @date 2016/10/31 20:28
 */
public class DoorAccessInfo extends BaseModel implements Parcelable, Serializable {
    /**
     * device_id : 1
     * lock_id : 1
     * user_name : 阿毛
     * unlock_times : 0
     */

    private int device_id;
    private int lock_id;
    private String user_name;
    private int unlock_times;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getLock_id() {
        return lock_id;
    }

    public void setLock_id(int lock_id) {
        this.lock_id = lock_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUnlock_times() {
        return unlock_times;
    }

    public void setUnlock_times(int unlock_times) {
        this.unlock_times = unlock_times;
    }

    @Override
    public String toString() {
        return "DoorAccessInfo{" +
                "device_id=" + device_id +
                ", lock_id=" + lock_id +
                ", user_name='" + user_name + '\'' +
                ", unlock_times=" + unlock_times +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoorAccessInfo that = (DoorAccessInfo) o;

        if (device_id != that.device_id) return false;
        return lock_id == that.lock_id;

    }

    @Override
    public int hashCode() {
        int result = device_id;
        result = 31 * result + lock_id;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.device_id);
        dest.writeInt(this.lock_id);
        dest.writeString(this.user_name);
        dest.writeInt(this.unlock_times);
    }

    public DoorAccessInfo() {
    }

    protected DoorAccessInfo(Parcel in) {
        this.device_id = in.readInt();
        this.lock_id = in.readInt();
        this.user_name = in.readString();
        this.unlock_times = in.readInt();
    }

    public static final Creator<DoorAccessInfo> CREATOR = new Creator<DoorAccessInfo>() {
        @Override
        public DoorAccessInfo createFromParcel(Parcel source) {
            return new DoorAccessInfo(source);
        }

        @Override
        public DoorAccessInfo[] newArray(int size) {
            return new DoorAccessInfo[size];
        }
    };
}
