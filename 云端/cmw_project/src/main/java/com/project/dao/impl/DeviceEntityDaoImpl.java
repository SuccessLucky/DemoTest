package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.DeviceEntityDao;
import com.project.entity.DeviceEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Repository
public class DeviceEntityDaoImpl extends BaseDaoSupport<DeviceEntity, Integer> implements DeviceEntityDao {

    @Override
    public List<DeviceEntity> getDevicesByRoomId(int roomId) {

        String hql = "from DeviceEntity d where d.roomId = ? order by d.sequence, d.id";
        List<DeviceEntity> rs = getListByHQL(hql, roomId);

        return rs;
    }

    @Override
    public List<DeviceEntity> getDevicesByGateway(int gatewayId) {

        String hql = "select d from DeviceEntity d, RoomEntity r where d.roomId = r.id and r.floor.gateway.id = ?";
        List<DeviceEntity> rs = getListByHQL(hql, gatewayId);

        return rs;
    }

    @Override
    public List<DeviceEntity> getDeviceByMacAddress(String macAddress, int gatewayId) {

        String hql = "select d from DeviceEntity d, RoomEntity r where d.roomId = r.id and r.floor.gateway.id = ? and d.macAddress = ? ";
        return getListByHQL(hql, gatewayId, macAddress);
    }

    @Override
    public List<DeviceEntity> getDevicesByIds(List<Integer> ids) {

        if (ids.size() == 0) {
            return null;
        }

        String hql = "from DeviceEntity d where d.id in (:ids) ";
        Query query = getSession().createQuery(hql);
        query.setParameterList("ids", ids.toArray());
        return query.list();
    }

    @Override
    public void updateDeciceSequence(int roomId, int deviceId, int sequence) {
        String hql = "update DeviceEntity d set d.sequence = ? where d.roomId = ? and d.id = ?";

        Session session = getSession();
        session.beginTransaction();
        Query query = session.createQuery(hql);
        query.setParameter(0, sequence);
        query.setParameter(1, roomId);
        query.setParameter(2, deviceId);
        int rows = query.executeUpdate();
        session.getTransaction().commit();
    }
}
