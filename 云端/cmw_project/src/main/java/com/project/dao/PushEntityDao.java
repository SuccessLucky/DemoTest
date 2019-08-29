package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.PushEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/29.
 */
public interface PushEntityDao extends IBaseDao<PushEntity, Integer > {

    public PushEntity readPushEntity(int userId, String uuid);
    public PushEntity readPushEntityByDeviceId(String deviceId);
    public PushEntity readPushEntityByUUId(String uuid);
    public List<PushEntity> readPushEntities(int userId);

}
