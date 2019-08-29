package com.project.entity;

import com.project.common.entity.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by xieyanhao on 16/9/23.
 */
@Entity
@Table(name = "unlock_psw_tbl")
public class UnlockPswEntity extends DomainObject {

    private Integer deviceId;
    private String unlockPsw; // 开锁密码
    private int unlockTimes; // 开锁次数

    @Column(name = "device_id", columnDefinition = "int(11)")
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "unlock_psw")
    public String getUnlockPsw() {
        return unlockPsw;
    }

    public void setUnlockPsw(String unlockPsw) {
        this.unlockPsw = unlockPsw;
    }

    @Column(name = "unlock_times")
    public int getUnlockTimes() {
        return unlockTimes;
    }

    public void setUnlockTimes(int unlockTimes) {
        this.unlockTimes = unlockTimes;
    }
}
