package com.project.service.impl;

import com.project.dao.PushEntityDao;
import com.project.entity.PushEntity;
import com.project.service.PushEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/29.
 */
@Service("pushEntityService")
public class PushEntityServiceImpl implements PushEntityService{

    @Autowired
    private PushEntityDao pushEntityDao;

    @Override
    public PushEntity readPushEntity(int userId, String uuid) {
        return pushEntityDao.readPushEntity(userId, uuid);
    }

    @Override
    public PushEntity readPushEntityByDeviceId(String deviceId) {
        return pushEntityDao.readPushEntityByDeviceId(deviceId);
    }

    @Override
    public PushEntity readPushEntityByUUId(String uuid) {
        return pushEntityDao.readPushEntityByUUId(uuid);
    }

    @Override
    public List<PushEntity> readPushEntities(int userId) {
        return pushEntityDao.readPushEntities(userId);
    }

    @Override
    @Transactional
    public void delete(PushEntity pushEntity) {
        pushEntityDao.delete(pushEntity);
    }

    @Override
    @Transactional
    public void save(PushEntity pushEntity) {
        pushEntityDao.save(pushEntity);
    }

    @Override
    @Transactional
    public void update(PushEntity pushEntity) {
        pushEntityDao.update(pushEntity);
    }
}
