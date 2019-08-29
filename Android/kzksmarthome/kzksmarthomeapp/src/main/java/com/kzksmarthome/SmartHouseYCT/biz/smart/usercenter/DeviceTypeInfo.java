package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * @author laixj
 * @version V1.0
 * @Title: DeviceTypeInfo
 * @Description: 设备类型信息Bean
 * @date 2016/9/17 15:52
 */
public class DeviceTypeInfo extends BaseModel implements Parcelable {
    /**
     * 控制类型
     */
    private short ctrType;
    /**
     * 设备类型
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

    public DeviceTypeInfo() {
    }

    public DeviceTypeInfo(short ctrType, short type, String typeName, int drawable) {
        this.ctrType = ctrType;
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

    public short getCtrType() {
        return ctrType;
    }

    public void setCtrType(short ctrType) {
        this.ctrType = ctrType;
    }

    @Override
    public String toString() {
        return "DeviceTypeInfo{" +
                "ctrType=" + ctrType +
                ", type=" + type +
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

        DeviceTypeInfo typeInfo = (DeviceTypeInfo) o;
        if (ctrType != typeInfo.getCtrType() || type != typeInfo.getType() || typeName.compareTo(typeInfo.getTypeName()) != 0) {
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
        dest.writeInt(this.ctrType);
        dest.writeInt(this.type);
        dest.writeString(this.typeName);
        dest.writeInt(this.drawable);
    }

    protected DeviceTypeInfo(Parcel in) {
        this.ctrType = (short) in.readInt();
        this.type = (short) in.readInt();
        this.typeName = in.readString();
        this.drawable = in.readInt();
    }

    public static final Creator<DeviceTypeInfo> CREATOR = new Creator<DeviceTypeInfo>() {
        @Override
        public DeviceTypeInfo createFromParcel(Parcel source) {
            return new DeviceTypeInfo(source);
        }

        @Override
        public DeviceTypeInfo[] newArray(int size) {
            return new DeviceTypeInfo[size];
        }
    };
}
