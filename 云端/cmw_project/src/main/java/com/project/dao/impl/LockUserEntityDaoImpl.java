package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.LockUserEntityDao;
import com.project.entity.LockUserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/23.
 */
@Repository
public class LockUserEntityDaoImpl extends BaseDaoSupport<LockUserEntity, Integer> implements LockUserEntityDao{

    @Override
    public LockUserEntity getDeviceLockUserByPsw(int deviceId, String fingerprintId) {

        String hql = "from LockUserEntity u where u.deviceId = ? and u.fingerprintId = ? ";
        List<LockUserEntity> rs = getListByHQL(hql, deviceId, fingerprintId);

        if (rs.size() >= 1)
            return rs.get(0);

        return null;
    }

    @Override
    public List<LockUserEntity> getDeviceLockUsers(int deviceId) {

        String hql = " from LockUserEntity u where u.deviceId = ? ";
        return getListByHQL(hql, deviceId);
    }
}
