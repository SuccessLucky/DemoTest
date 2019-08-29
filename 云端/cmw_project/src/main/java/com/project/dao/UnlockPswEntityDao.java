package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.UnlockPswEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/23.
 */
public interface UnlockPswEntityDao extends IBaseDao<UnlockPswEntity, Integer > {

    public UnlockPswEntity getDeviceUnlockPswByPsw(int deviceId, String psw);
    public List<UnlockPswEntity> getDeviceUnlockPsws(int deviceId);

}
