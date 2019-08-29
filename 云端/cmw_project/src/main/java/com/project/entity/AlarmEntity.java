package com.project.entity;

import com.project.common.entity.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by xieyanhao on 16/10/27.
 */
@Entity
@Table(name = "alarm_tbl")
public class AlarmEntity extends DomainObject {

    private Integer deviceId;
    private int alarmStatus; // 警报状态 默认0:正常, 1警报
    private String message; // 警报消息
    private Date createTime;

    public static int NormalStatus = 0;
    public static int AlarmStatus = 1;

    @Column(name = "device_id", columnDefinition = "int(11)")
    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "alarm_status")
    public int getAlarmStatus() {
        return alarmStatus;
    }

    @Column(name = "message")
    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getMessage() {
        return message;
    }

    @Column(name = "create_time", columnDefinition = "datetime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
