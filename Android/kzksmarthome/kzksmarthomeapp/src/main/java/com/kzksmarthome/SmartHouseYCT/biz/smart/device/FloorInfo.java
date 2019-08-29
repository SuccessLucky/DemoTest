package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @Title: RoomInfo
 * @Description: 楼层信息Bean
 * @author laixj
 * @date 2016/9/8 22:25
 * @version V1.0
 */
public class FloorInfo extends BaseModel implements Parcelable,Serializable {

    /**
     * 楼层号
     */
    private int id;
    /**
     * 楼层名
     */
    private String name;
    /**
     * 楼层图片
     */
    private String image;

    public FloorInfo(){}

    public FloorInfo(FloorInfo info){
        this.id = info.getId();
        this.name = info.getName();
        this.image = info.getImage();
    }

    public FloorInfo(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloorInfo floorInfo = (FloorInfo) o;
        if (id != floorInfo.getId()) {
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
    }

    protected FloorInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
    }

    public static final Creator<FloorInfo> CREATOR = new Creator<FloorInfo>() {
        @Override
        public FloorInfo createFromParcel(Parcel source) {
            return new FloorInfo(source);
        }

        @Override
        public FloorInfo[] newArray(int size) {
            return new FloorInfo[size];
        }
    };

    @Override
    public String toString() {
        return "FloorInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
