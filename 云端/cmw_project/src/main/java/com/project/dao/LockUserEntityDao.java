package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.LockUserEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/23.
 */
public interface LockUserEntityDao extends IBaseDao<LockUserEntity, Integer > {

    public LockUserEntity getDeviceLockUserByPsw(int deviceId, String fingerprintId);
    public List<LockUserEntity> getDeviceLockUsers(int deviceId);

}
