package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneDetailInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author laixj
 * @version V1.0
 * @Title: DeviceInfo
 * @Description: 设备信息Bean
 * @date 2016/9/8 22:04
 */
public class DeviceInfo extends BaseModel implements Parcelable,Serializable {

    private int device_id;
    private int room_id;
    private String device_name;
    private String image;
    private String device_OD;
    private String device_type;
    private String category;
    private String sindex;
    private String sindex_length;
    private int device_state1 = 2;
    private int device_state2;
    private int device_state3;
    private int alarm_status;
    private String other_status;
    private String mac_address;
    private String floor_name;
    private String room_name;
    private String cmd_id;
    private int floor_id;




    /**
     * 是否选中
     */
    private boolean selected;

    public DeviceInfo(int device_id) {
        this.device_id = device_id;
    }

    public DeviceInfo(int room_id, String device_OD) {
        this.room_id = room_id;
        this.device_OD = device_OD;
    }

    public DeviceInfo(DeviceInfo deviceInfo){
        this.device_id = deviceInfo.getDevice_id();
        this.room_id = deviceInfo.getRoom_id();
        this.device_name = deviceInfo.getDevice_name();
        this.image = deviceInfo.getImage();
        this.device_OD = deviceInfo.getDevice_OD();
        this.device_type = deviceInfo.getDevice_type();
        this.cmd_id = deviceInfo.getCmdId();
        this.category = deviceInfo.getCategory();
        this.sindex = deviceInfo.getSindex();
        this.device_state1 = deviceInfo.getDevice_state1();
        this.device_state2 = deviceInfo.getDevice_state2();
        this.device_state3 = deviceInfo.getDevice_state3();
        this.alarm_status = deviceInfo.getAlarm_status();
        this.other_status = deviceInfo.getOther_status();
        this.mac_address = deviceInfo.getMac_address();
        this.floor_name = deviceInfo.getFloor_name();
        this.room_name = deviceInfo.getRoom_name();
        this.sindex_length = deviceInfo.getSindex_length();
        this.floor_id = deviceInfo.getFloor_id();
        this.device_buttons = deviceInfo.getDevice_buttons();
    }

    public DeviceInfo(SceneDetailInfo deviceInfo){
        this.device_id = deviceInfo.getDevice_id();
        this.room_id = deviceInfo.getRoom_id();
        this.device_name = deviceInfo.getDevice_name();
        this.image = deviceInfo.getImage();
        this.device_OD = deviceInfo.getDevice_OD();
        this.device_type = deviceInfo.getDevice_type();
        this.cmd_id = deviceInfo.getCmdId();
        this.category = deviceInfo.getCategory();
        this.sindex = deviceInfo.getSindex();
        this.device_state1 = deviceInfo.getDevice_state1();
        this.device_state2 = deviceInfo.getDevice_state2();
        this.device_state3 = deviceInfo.getDevice_state3();
        this.alarm_status = deviceInfo.getAlarm_status();
        this.other_status = deviceInfo.getOther_status();
        this.mac_address = deviceInfo.getMac_address();
        this.floor_name = deviceInfo.getFloor_name();
        this.room_name = deviceInfo.getRoom_name();
        this.sindex_length = deviceInfo.getSindex_length();
        this.device_buttons = deviceInfo.getDevice_buttons();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceInfo that = (DeviceInfo) o;

        return device_id == that.device_id;

    }

    @Override
    public int hashCode() {
        return device_id;
    }

    private List<DeviceButtonInfo> device_buttons;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDevice_OD() {
        return device_OD;
    }

    public void setDevice_OD(String device_OD) {
        this.device_OD = device_OD;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSindex() {
        return sindex;
    }

    public void setSindex(String sindex) {
        this.sindex = sindex;
    }

    public int getDevice_state1() {
        return device_state1 <= 0?2:device_state1;
    }

    public void setDevice_state1(int device_state1) {
        this.device_state1 = device_state1;
    }

    public int getDevice_state2() {
        return device_state2;
    }

    public void setDevice_state2(int device_state2) {
        this.device_state2 = device_state2;
    }

    public int getDevice_state3() {
        return device_state3;
    }

    public void setDevice_state3(int device_state3) {
        this.device_state3 = device_state3;
    }

    public int getAlarm_status() {
        return alarm_status;
    }

    public void setAlarm_status(int alarm_status) {
        this.alarm_status = alarm_status;
    }

    public String getOther_status() {
        return other_status;
    }

    public void setOther_status(String other_status) {
        this.other_status = other_status;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public List<DeviceButtonInfo> getDevice_buttons() {
        return device_buttons;
    }

    public void setDevice_buttons(List<DeviceButtonInfo> device_buttons) {
        this.device_buttons = device_buttons;
    }

    public String getFloor_name() {
        return floor_name;
    }

    public void setFloor_name(String floor_name) {
        this.floor_name = floor_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCmdId() {
        return cmd_id;
    }

    public void setCmdId(String cmdId) {
        this.cmd_id = cmdId;
    }

    public String getSindex_length() {
        return sindex_length;
    }

    public void setSindex_length(String sindex_length) {
        this.sindex_length = sindex_length;
    }

    public int getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public DeviceInfo() {
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "device_id=" + device_id +
                ", room_id=" + room_id +
                ", device_name='" + device_name + '\'' +
                ", image='" + image + '\'' +
                ", device_OD='" + device_OD + '\'' +
                ", device_type='" + device_type + '\'' +
                ", category='" + category + '\'' +
                ", sindex='" + sindex + '\'' +
                ", sindex_length='" + sindex_length + '\'' +
                ", floor_id='" + floor_id + '\'' +
                ", device_state1=" + device_state1 +
                ", device_state2=" + device_state2 +
                ", device_state3=" + device_state3 +
                ", alarm_status=" + alarm_status +
                ", other_status='" + other_status + '\'' +
                ", mac_address='" + mac_address + '\'' +
                ", floor_name='" + floor_name + '\'' +
                ", room_name='" + room_name + '\'' +
                ", cmdId='" + cmd_id + '\'' +
                ", selected=" + selected +
                ", device_buttons=" + device_buttons +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.device_id);
        dest.writeInt(this.room_id);
        dest.writeString(this.device_name);
        dest.writeString(this.image);
        dest.writeString(this.device_OD);
        dest.writeString(this.device_type);
        dest.writeString(this.category);
        dest.writeString(this.sindex);
        dest.writeInt(this.device_state1);
        dest.writeInt(this.device_state2);
        dest.writeInt(this.device_state3);
        dest.writeInt(this.alarm_status);
        dest.writeString(this.other_status);
        dest.writeString(this.mac_address);
        dest.writeString(this.floor_name);
        dest.writeString(this.room_name);
        dest.writeString(this.cmd_id);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.device_buttons);
        dest.writeString(this.sindex_length);
        dest.writeInt(this.floor_id);
    }

    protected DeviceInfo(Parcel in) {
        this.device_id = in.readInt();
        this.room_id = in.readInt();
        this.device_name = in.readString();
        this.image = in.readString();
        this.device_OD = in.readString();
        this.device_type = in.readString();
        this.category = in.readString();
        this.sindex = in.readString();
        this.device_state1 = in.readInt();
        this.device_state2 = in.readInt();
        this.device_state3 = in.readInt();
        this.alarm_status = in.readInt();
        this.other_status = in.readString();
        this.mac_address = in.readString();
        this.floor_name = in.readString();
        this.room_name = in.readString();
        this.cmd_id = in.readString();
        this.selected = in.readByte() != 0;
        this.device_buttons = in.createTypedArrayList(DeviceButtonInfo.CREATOR);
        this.sindex_length = in.readString();
        this.floor_id = in.readInt();
    }

    public static final Creator<DeviceInfo> CREATOR = new Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel source) {
            return new DeviceInfo(source);
        }

        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };
}
