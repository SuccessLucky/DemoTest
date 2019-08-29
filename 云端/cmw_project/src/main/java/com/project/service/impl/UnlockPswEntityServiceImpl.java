package com.project.service.impl;

import com.project.dao.UnlockPswEntityDao;
import com.project.entity.UnlockPswEntity;
import com.project.service.UnlockPswEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/23.
 */
@Service
public class UnlockPswEntityServiceImpl implements UnlockPswEntityService{

    @Autowired
    private UnlockPswEntityDao unlockPswEntityDao;

    @Override
    @Transactional
    public void addUnlockPsw(UnlockPswEntity unlockPswEntity) {
        unlockPswEntityDao.save(unlockPswEntity);
    }

    @Override
    @Transactional
    public void deleteUnlockPsw(UnlockPswEntity unlockPswEntity) {
        unlockPswEntityDao.delete(unlockPswEntity);
    }

    @Override
    @Transactional
    public void updateUnlockPsw(UnlockPswEntity unlockPswEntity) {
        unlockPswEntityDao.update(unlockPswEntity);
    }

    @Override
    public UnlockPswEntity getDeviceUnlockPswByPsw(int deviceId, String psw) {
        return unlockPswEntityDao.getDeviceUnlockPswByPsw(deviceId, psw);
    }

    @Override
    public UnlockPswEntity getDeviceUnlockPswByid(int id) {
        return unlockPswEntityDao.get(id);
    }

    @Override
    public List<UnlockPswEntity> getDeviceUnlockPsws(int deviceId) {
        return unlockPswEntityDao.getDeviceUnlockPsws(deviceId);
    }
}
