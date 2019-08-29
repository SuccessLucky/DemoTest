package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.UnlockPswEntityDao;
import com.project.entity.UnlockPswEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/23.
 */
@Repository
public class UnlockPswEntityDaoImpl extends BaseDaoSupport<UnlockPswEntity, Integer> implements UnlockPswEntityDao{

    @Override
    public UnlockPswEntity getDeviceUnlockPswByPsw(int deviceId, String psw) {

        String hql = "from UnlockPswEntity u where u.deviceId = ? and u.unlockPsw = ? ";
        List<UnlockPswEntity> rs = getListByHQL(hql, deviceId, psw);

        if (rs.size() >= 1)
            return rs.get(0);

        return null;
    }

    @Override
    public List<UnlockPswEntity> getDeviceUnlockPsws(int deviceId) {
        String hql = "from UnlockPswEntity u where u.deviceId = ?  ";
        return getListByHQL(hql, deviceId);
    }
}
