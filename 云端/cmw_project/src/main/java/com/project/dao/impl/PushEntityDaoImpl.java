package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.PushEntityDao;
import com.project.entity.PushEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/29.
 */
@Repository
public class PushEntityDaoImpl extends BaseDaoSupport<PushEntity, Integer> implements PushEntityDao{

    @Override
    public PushEntity readPushEntity(int userId, String uuid) {
        String hql = "from PushEntity p where p.userId = ? and p.uuid = ?";

        List<PushEntity> rs = getListByHQL(hql, userId, uuid);

        if (rs.size() >= 1)
            return rs.get(0);

        return null;
    }

    @Override
    public PushEntity readPushEntityByDeviceId(String deviceId) {
        String hql = "from PushEntity p where p.deviceId = ?";

        List<PushEntity> rs = getListByHQL(hql, deviceId);

        if (rs.size() >= 1)
            return rs.get(0);

        return null;
    }

    @Override
    public PushEntity readPushEntityByUUId(String uuid) {
        String hql = "from PushEntity p where p.uuid = ?";

        List<PushEntity> rs = getListByHQL(hql, uuid);

        if (rs.size() >= 1)
            return rs.get(0);

        return null;
    }

    @Override
    public List<PushEntity> readPushEntities(int userId) {


        String hql = "from PushEntity p where p.userId = ?";
        return getListByHQL(hql, userId);
    }
}
