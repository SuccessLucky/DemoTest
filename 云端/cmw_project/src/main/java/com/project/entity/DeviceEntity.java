package com.project.entity;

import com.project.common.entity.DomainObject;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Entity
@Table(name = "device_tbl")
public class DeviceEntity extends DomainObject {

    private Integer roomId;
    private String name;
    private String image;
    private String deviceOD; // Object Dictionary
    private String deviceType; // 设备类型
    private String category; // 设备类别
    private String sindex; // 子索引
    private String sIndexLength; // 子索引长度
    private String cmdId;
    private int sequence; // 排序
    private int deviceState1; // 状态 默认0:关闭,1:打开
    private int deviceState2; // 状态 默认0:关闭,1:打开
    private int deviceState3; // 状态 默认0:关闭,1:打开
    private int alarmStatus; // 警报状态 默认0:正常
    private String otherStatus;
    private String otherInfo;
    private String macAddress;
    private String regional;
    private int controlType;
    private List<DeviceButtonEntity> deviceButtons = new ArrayList<>(); // 设备按键

    @Column(name = "room_id", columnDefinition = "int(11)")
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "device_OD")
    public String getDeviceOD() {
        return deviceOD;
    }

    public void setDeviceOD(String deviceOD) {
        this.deviceOD = deviceOD;
    }

    @Column(name = "device_type")
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "sindex")
    public String getSindex() {
        return sindex;
    }

    public void setSindex(String sindex) {
        this.sindex = sindex;
    }

    @Column(name = "sIndexLength")
    public String getsIndexLength() {
        return sIndexLength;
    }

    public void setsIndexLength(String sIndexLength) {
        this.sIndexLength = sIndexLength;
    }

    @Column(name = "cmdId")
    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    @Column(name = "sequence", columnDefinition = "int default 0")
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Column(name = "device_state1", columnDefinition = "tinyint default 0")
    public int getDeviceState1() {
        return deviceState1;
    }

    public void setDeviceState1(int deviceState1) {
        this.deviceState1 = deviceState1;
    }

    @Column(name = "device_state2", columnDefinition = "tinyint default 0")
    public int getDeviceState2() {
        return deviceState2;
    }

    public void setDeviceState2(int deviceState2) {
        this.deviceState2 = deviceState2;
    }

    @Column(name = "device_state3", columnDefinition = "tinyint default 0")
    public int getDeviceState3() {
        return deviceState3;
    }

    public void setDeviceState3(int deviceState3) {
        this.deviceState3 = deviceState3;
    }

    @Column(name = "other_state")
    public String getOtherStatus() {
        return otherStatus;
    }

    public void setOtherStatus(String otherStatus) {
        this.otherStatus = otherStatus;
    }

    @Column(name = "other_info", length=2000)
    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    @Column(name = "alarm_status", nullable = false, columnDefinition = "tinyint default 0")
    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    @Column(name = "mac_address")
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Column(name = "regional")
    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    @Column(name = "control_type", columnDefinition = "tinyint default 0")
    public int getControlType() {
        return controlType;
    }

    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    @OneToMany(mappedBy="device",orphanRemoval=true)
    @Cascade({CascadeType.ALL})
    @OrderBy("id")
    public List<DeviceButtonEntity> getDeviceButtons() {
        return deviceButtons;
    }

    public void setDeviceButtons(List<DeviceButtonEntity> deviceButtons) {
        this.deviceButtons = deviceButtons;
    }
}
