package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.FloorEntityDao;
import com.project.entity.FloorEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Repository
public class FloorEntityDaoImpl extends BaseDaoSupport<FloorEntity, Integer> implements FloorEntityDao {

    @Override
    public List<FloorEntity> getFloorsByGateway(int gatewayId) {

        String hql = "from FloorEntity f where f.gateway.id = ?";
        List<FloorEntity> rs = getListByHQL(hql, gatewayId);
        return rs;
    }
}
