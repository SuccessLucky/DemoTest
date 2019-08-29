package com.project.entity;

import com.project.common.entity.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by xieyanhao on 16/10/25.
 */
@Entity
@Table(name = "push_tbl", uniqueConstraints = @UniqueConstraint(columnNames = {"uuid"}))
public class PushEntity extends DomainObject {

    private int userId; // memberUserId, merchantUserId
    private String uuid;
    private String deviceId;
    private String userAgent;

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "user_agent")
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
