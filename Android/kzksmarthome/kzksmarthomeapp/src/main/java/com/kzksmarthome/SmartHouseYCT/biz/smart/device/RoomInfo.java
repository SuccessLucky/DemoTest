package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @Title: RoomInfo
 * @Description: 房间信息Bean
 * @author laixj
 * @date 2016/9/8 22:25
 * @version V1.0
 */
public class RoomInfo extends BaseModel implements Parcelable,Serializable {

    private int id;
    private String name;
    private String image;
    private int floor_id;

    public RoomInfo(){}

    public RoomInfo(int floor_id) {
        this.floor_id = floor_id;
    }

    public RoomInfo(int id, int floor_id) {
        this.id = id;
        this.floor_id = floor_id;
    }

    public RoomInfo(int id, String name, String image, int floor_id) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.floor_id = floor_id;
    }

    public RoomInfo(RoomInfo info) {
        this.id = info.getId();
        this.name = info.getName();
        this.image = info.getImage();
        this.floor_id = info.getFloor_id();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", floor_id=" + floor_id +
                '}';
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomInfo roomInfo = (RoomInfo) o;
        if (id != roomInfo.getId() || floor_id != roomInfo.getFloor_id()) {
            return false;
        }
        return true;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeInt(this.floor_id);
    }

    protected RoomInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.floor_id = in.readInt();
    }

    public static final Creator<RoomInfo> CREATOR = new Creator<RoomInfo>() {
        @Override
        public RoomInfo createFromParcel(Parcel source) {
            return new RoomInfo(source);
        }

        @Override
        public RoomInfo[] newArray(int size) {
            return new RoomInfo[size];
        }
    };
}
