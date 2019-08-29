package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;

/**
 * @author laixj
 * @version V1.0
 * @Title: DeviceButtonInfo
 * @Description: 设备按钮Bean
 * @date 2016/9/8 22:04
 */
public class DeviceButtonInfo extends BaseModel implements Parcelable, Serializable {

    /**
     * device_buttons : [{"button_id":1,"name":"温度加","instruction_code":"0001x11"},{"button_id":2,"name":"温度减","instruction_code":"0001x12"}]
     */
    private int button_id;
    private String name;
    private String instruction_code;

    public DeviceButtonInfo() {
    }

    public DeviceButtonInfo(int button_id) {
        this.button_id = button_id;
    }

    public DeviceButtonInfo(int button_id, String name, String instruction_code) {
        this.button_id = button_id;
        this.name = name;
        this.instruction_code = instruction_code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceButtonInfo that = (DeviceButtonInfo) o;

        if (button_id != that.button_id) return false;
        //if (name != null ? !name.equals(that.name) : that.name != null) return false;
        //return instruction_code != null ? instruction_code.equals(that.instruction_code) : that.instruction_code == null;
        return true;
    }

    @Override
    public int hashCode() {
        int result = button_id;
        return result;
    }

    @Override
    public String toString() {
        return "DeviceButtonInfo{" +
                "button_id=" + button_id +
                ", name='" + name + '\'' +
                ", instruction_code='" + instruction_code + '\'' +
                '}';
    }

    public int getButton_id() {
        return button_id;
    }

    public void setButton_id(int button_id) {
        this.button_id = button_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstruction_code() {
        return instruction_code;
    }

    public void setInstruction_code(String instruction_code) {
        this.instruction_code = instruction_code;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.button_id);
        dest.writeString(this.name);
        dest.writeString(this.instruction_code);
    }

    protected DeviceButtonInfo(Parcel in) {
        this.button_id = in.readInt();
        this.name = in.readString();
        this.instruction_code = in.readString();
    }

    public static final Creator<DeviceButtonInfo> CREATOR = new Creator<DeviceButtonInfo>() {
        @Override
        public DeviceButtonInfo createFromParcel(Parcel source) {
            return new DeviceButtonInfo(source);
        }

        @Override
        public DeviceButtonInfo[] newArray(int size) {
            return new DeviceButtonInfo[size];
        }
    };
}
