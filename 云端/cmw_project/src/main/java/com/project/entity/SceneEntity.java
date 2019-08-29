package com.project.entity;

import com.project.common.entity.DomainObject;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;

/**
 * Created by xieyanhao on 16/9/26.
 */
@Entity
@Table(name = "scene_tbl")
public class SceneEntity extends DomainObject {

    private String name;
    private String image;
    private int sceneType;
    private int needLinkage; // 是否联动, 0 不联动,1 联动
    private int needTimeDelay; // 是否延时
    private int needTiming; // 是否定时
    private int needSecurityOn; // 布防时有效
    private int needSecurityOff; // 撤防时有效
    private String delayTime; // 延时时间
    private String timingTime; // 定时时间
    private String subCommandIdentifer;
    private String serialNumber;
    private String reservedProperty;
    private String forceLinkage;
    private String enabledOrDisableIdentifer;
    private String armingOrDisarmingIdentifer;
    private String linkageDeviceMacAddr;
    private String linkageDeviceRoad;
    private String linkageDeviceDataType;
    private String linkageDeviceDataRange;
    private String linkageTime;
    private String linkageDelayTime;

    private GatewayEntity gateway;
    private List<SceneDetailEntity> sceneDetails;

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

    @Column(name = "scene_type", columnDefinition = "tinyint default 0")
    public int getSceneType() {
        return sceneType;
    }

    public void setSceneType(int sceneType) {
        this.sceneType = sceneType;
    }

    @Column(name = "need_linkage", columnDefinition = "tinyint default 1")
    public int getNeedLinkage() {
        return needLinkage;
    }

    public void setNeedLinkage(int needLinkage) {
        this.needLinkage = needLinkage;
    }

    @Column(name = "need_time_delay", columnDefinition = "tinyint default 0")
    public int getNeedTimeDelay() {
        return needTimeDelay;
    }

    public void setNeedTimeDelay(int needTimeDelay) {
        this.needTimeDelay = needTimeDelay;
    }

    @Column(name = "need_timing", columnDefinition = "tinyint default 0")
    public int getNeedTiming() {
        return needTiming;
    }

    public void setNeedTiming(int needTiming) {
        this.needTiming = needTiming;
    }

    @Column(name = "need_security_on", columnDefinition = "tinyint default 1")
    public int getNeedSecurityOn() {
        return needSecurityOn;
    }

    public void setNeedSecurityOn(int needSecurityOn) {
        this.needSecurityOn = needSecurityOn;
    }

    @Column(name = "need_security_off", columnDefinition = "tinyint default 0")
    public int getNeedSecurityOff() {
        return needSecurityOff;
    }

    public void setNeedSecurityOff(int needSecurityOff) {
        this.needSecurityOff = needSecurityOff;
    }

    @Column(name = "delay_time")
    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    @Column(name = "timing_time")
    public String getTimingTime() {
        return timingTime;
    }

    public void setTimingTime(String timingTime) {
        this.timingTime = timingTime;
    }

    @Column(name = "sub_command_identifer")
    public String getSubCommandIdentifer() {
        return subCommandIdentifer;
    }

    public void setSubCommandIdentifer(String subCommandIdentifer) {
        this.subCommandIdentifer = subCommandIdentifer;
    }

    @Column(name = "serial_number")
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "reserved_property")
    public String getReservedProperty() {
        return reservedProperty;
    }

    public void setReservedProperty(String reservedProperty) {
        this.reservedProperty = reservedProperty;
    }

    @Column(name = "force_linkage")
    public String getForceLinkage() {
        return forceLinkage;
    }

    public void setForceLinkage(String forceLinkage) {
        this.forceLinkage = forceLinkage;
    }

    @Column(name = "enabled_or_disable_identifer")
    public String getEnabledOrDisableIdentifer() {
        return enabledOrDisableIdentifer;
    }

    public void setEnabledOrDisableIdentifer(String enabledOrDisableIdentifer) {
        this.enabledOrDisableIdentifer = enabledOrDisableIdentifer;
    }

    @Column(name = "arming_or_disarming_identifer")
    public String getArmingOrDisarmingIdentifer() {
        return armingOrDisarmingIdentifer;
    }

    public void setArmingOrDisarmingIdentifer(String armingOrDisarmingIdentifer) {
        this.armingOrDisarmingIdentifer = armingOrDisarmingIdentifer;
    }

    @Column(name = "linkage_device_mac_addr")
    public String getLinkageDeviceMacAddr() {
        return linkageDeviceMacAddr;
    }

    public void setLinkageDeviceMacAddr(String linkageDeviceMacAddr) {
        this.linkageDeviceMacAddr = linkageDeviceMacAddr;
    }

    @Column(name = "linkage_device_road")
    public String getLinkageDeviceRoad() {
        return linkageDeviceRoad;
    }

    public void setLinkageDeviceRoad(String linkageDeviceRoad) {
        this.linkageDeviceRoad = linkageDeviceRoad;
    }

    @Column(name = "linkage_device_data_type")
    public String getLinkageDeviceDataType() {
        return linkageDeviceDataType;
    }

    public void setLinkageDeviceDataType(String linkageDeviceDataType) {
        this.linkageDeviceDataType = linkageDeviceDataType;
    }

    @Column(name = "linkage_device_data_range")
    public String getLinkageDeviceDataRange() {
        return linkageDeviceDataRange;
    }

    public void setLinkageDeviceDataRange(String linkageDeviceDataRange) {
        this.linkageDeviceDataRange = linkageDeviceDataRange;
    }

    @Column(name = "linkage_time")
    public String getLinkageTime() {
        return linkageTime;
    }

    public void setLinkageTime(String linkageTime) {
        this.linkageTime = linkageTime;
    }

    @Column(name = "linkage_delay_time")
    public String getLinkageDelayTime() {
        return linkageDelayTime;
    }

    public void setLinkageDelayTime(String linkageDelayTime) {
        this.linkageDelayTime = linkageDelayTime;
    }

    @ManyToOne
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "gateway_id")
    public GatewayEntity getGateway() {
        return gateway;
    }

    public void setGateway(GatewayEntity gateway) {
        this.gateway = gateway;
    }

    @OneToMany(mappedBy="scene",orphanRemoval=true)
    @Cascade({CascadeType.ALL})
    @OrderBy("sequence")
    public List<SceneDetailEntity> getSceneDetails() {
        return sceneDetails;
    }

    public void setSceneDetails(List<SceneDetailEntity> sceneDetails) {
        this.sceneDetails = sceneDetails;
    }
}
