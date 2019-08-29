package com.project.service;

import com.project.entity.PushEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/29.
 */
public interface PushEntityService {

    public PushEntity readPushEntity(int userId, String uuid);
    public PushEntity readPushEntityByDeviceId(String deviceId);
    public PushEntity readPushEntityByUUId(String uuid);
    public List<PushEntity> readPushEntities(int userId);
    public void delete(PushEntity pushEntity);
    public void save(PushEntity pushEntity);
    public void update(PushEntity pushEntity);

}
