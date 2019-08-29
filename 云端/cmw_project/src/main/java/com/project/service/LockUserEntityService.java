package com.project.service;

import com.project.entity.LockUserEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/23.
 */
public interface LockUserEntityService {

    public void addLockUser(LockUserEntity lockUserEntity);
    public void deleteLockUser(LockUserEntity lockUserEntity);
    public void updateLockUser(LockUserEntity lockUserEntity);
    public LockUserEntity getDeviceLockUserByPsw(int deviceId, String fingerprintId);
    public LockUserEntity getLockUserById(int id);
    public List<LockUserEntity> getDeviceLockUsers(int deviceId);

}
