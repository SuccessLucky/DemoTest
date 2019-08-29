package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.DeviceButtonEntityDao;
import com.project.entity.DeviceButtonEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/16.
 */
@Repository
public class DeviceButtonEntityDaoImpl extends BaseDaoSupport<DeviceButtonEntity, Integer> implements DeviceButtonEntityDao {

    @Override
    public List<DeviceButtonEntity> getDeviceButtonsByGateway(int gatewayId) {

        String hql = "select db from DeviceButtonEntity db, DeviceEntity d, RoomEntity r " +
                "where db.device.id = d.id and d.roomId = r.id and r.floor.gateway.id = ?";
        List<DeviceButtonEntity> rs = getListByHQL(hql, gatewayId);

        return rs;

    }
}
