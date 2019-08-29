package com.project.entity;

import com.project.common.entity.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by xieyanhao on 16/9/23.
 */
@Entity
@Table(name = "lock_user_tbl")
public class LockUserEntity extends DomainObject {

    private Integer deviceId;
    private String userName; // 用户名称
    private String fingerprintId; // 指纹ID
    private int unlockTimes; // 开锁次数

    @Column(name = "device_id", columnDefinition = "int(11)")
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "fingerprint_id")
    public String getFingerprintId() {
        return fingerprintId;
    }

    public void setFingerprintId(String fingerprintId) {
        this.fingerprintId = fingerprintId;
    }

    @Column(name = "unlock_times")
    public int getUnlockTimes() {
        return unlockTimes;
    }

    public void setUnlockTimes(int unlockTimes) {
        this.unlockTimes = unlockTimes;
    }
}
