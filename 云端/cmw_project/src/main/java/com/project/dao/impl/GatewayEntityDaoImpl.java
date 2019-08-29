package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.GatewayEntityDao;
import com.project.entity.GatewayEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/7.
 */
@Repository
public class GatewayEntityDaoImpl extends BaseDaoSupport<GatewayEntity, Integer> implements GatewayEntityDao {

    @Override
    public List<GatewayEntity> getAllUserGateway() {

        String hql = "form GatewayEntity";

        return getListByHQL(hql, null);
    }

    @Override
    public GatewayEntity getGatewayByMac(String macAddress) {

        String hql = "from GatewayEntity u where u.macAddress = ? ";
        List<GatewayEntity> rs = getListByHQL(hql, macAddress);

        if (rs.size() >= 1)
            return rs.get(0);

        return null;
    }
}
