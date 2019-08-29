package com.project.service.impl;

import com.project.dao.LockUserEntityDao;
import com.project.entity.LockUserEntity;
import com.project.service.LockUserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/23.
 */
@Service
public class LockUserEntityServiceImpl implements LockUserEntityService {

    @Autowired
    private LockUserEntityDao lockUserEntityDao;

    @Override
    @Transactional
    public void addLockUser(LockUserEntity lockUserEntity) {
        lockUserEntityDao.save(lockUserEntity);
    }

    @Override
    @Transactional
    public void deleteLockUser(LockUserEntity lockUserEntity) {
        lockUserEntityDao.delete(lockUserEntity);
    }

    @Override
    @Transactional
    public void updateLockUser(LockUserEntity lockUserEntity) {
        lockUserEntityDao.update(lockUserEntity);
    }

    @Override
    public LockUserEntity getDeviceLockUserByPsw(int deviceId, String fingerprintId) {
        return lockUserEntityDao.getDeviceLockUserByPsw(deviceId, fingerprintId);
    }

    @Override
    public LockUserEntity getLockUserById(int id) {
        return lockUserEntityDao.get(id);
    }

    @Override
    public List<LockUserEntity> getDeviceLockUsers(int deviceId) {
        return lockUserEntityDao.getDeviceLockUsers(deviceId);
    }
}
