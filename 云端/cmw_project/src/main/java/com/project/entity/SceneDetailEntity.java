package com.project.entity;

import com.project.common.entity.DomainObject;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

/**
 * Created by xieyanhao on 16/9/26.
 */
@Entity
@Table(name = "scene_detail_tbl")
public class SceneDetailEntity extends DomainObject {

    private SceneEntity scene;
    private Integer deviceId;
    private int deviceState1; // 状态 默认0:关闭,1:打开
    private int deviceState2; // 状态 默认0:关闭,1:打开
    private int deviceState3; // 状态 默认0:关闭,1:打开
    private String otherStatus;
    private int sequence; // 排序

    @ManyToOne
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE})
    @JoinColumn(name="scene_id")
    public SceneEntity getScene() {
        return scene;
    }

    public void setScene(SceneEntity scene) {
        this.scene = scene;
    }

    @Column(name = "device_id", columnDefinition = "int(11)")
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
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

    @Column(name = "sequence", columnDefinition = "int default 0")
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
