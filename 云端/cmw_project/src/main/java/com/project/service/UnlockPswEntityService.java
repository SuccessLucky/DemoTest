package com.project.service;

import com.project.entity.UnlockPswEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/23.
 */
public interface UnlockPswEntityService {

    public void addUnlockPsw(UnlockPswEntity unlockPswEntity);
    public void deleteUnlockPsw(UnlockPswEntity unlockPswEntity);
    public void updateUnlockPsw(UnlockPswEntity unlockPswEntity);
    public UnlockPswEntity getDeviceUnlockPswByPsw(int deviceId, String psw);
    public UnlockPswEntity getDeviceUnlockPswByid(int id);
    public List<UnlockPswEntity> getDeviceUnlockPsws(int deviceId);

}
