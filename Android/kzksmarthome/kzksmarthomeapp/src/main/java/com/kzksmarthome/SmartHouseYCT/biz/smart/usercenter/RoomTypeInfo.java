package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @author laixj
 * @version V1.0
 * @Title: RoomTypeInfo
 * @Description: 房间类型信息Bean
 * @date 2016/9/17 15:52
 */
public class RoomTypeInfo extends BaseModel implements Parcelable, Serializable {
    /**
     * type
     */
    private short type;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型图片
     */
    private int drawable;

    public RoomTypeInfo() {
    }

    public RoomTypeInfo(short type, String typeName, int drawable) {
        this.type = type;
        this.typeName = typeName;
        this.drawable = drawable;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    @Override
    public String toString() {
        return "RoomTypeInfo{" +
                "type=" + type +
                ", typeName='" + typeName + '\'' +
                ", drawable=" + drawable +
                '}';
    }

    @Override
    public int hashCode() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomTypeInfo typeInfo = (RoomTypeInfo) o;
        if (type != typeInfo.getType() || typeName.compareTo(typeInfo.getTypeName()) != 0) {
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
        dest.writeInt(this.type);
        dest.writeString(this.typeName);
        dest.writeInt(this.drawable);
    }

    protected RoomTypeInfo(Parcel in) {
        this.type = (short) in.readInt();
        this.typeName = in.readString();
        this.drawable = in.readInt();
    }

    public static final Creator<RoomTypeInfo> CREATOR = new Creator<RoomTypeInfo>() {
        @Override
        public RoomTypeInfo createFromParcel(Parcel source) {
            return new RoomTypeInfo(source);
        }

        @Override
        public RoomTypeInfo[] newArray(int size) {
            return new RoomTypeInfo[size];
        }
    };
}
