package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.AlarmEntityDao;
import com.project.entity.AlarmEntity;
import com.project.entity.GatewayEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/28.
 */
@Repository
public class AlarmEntityDaoImpl extends BaseDaoSupport<AlarmEntity, Integer> implements AlarmEntityDao{

    @Override
    public List<AlarmEntity> getAlarmByDate(Date startDate, Date endDate, GatewayEntity gateway) {

        String hql = "select a from AlarmEntity a, DeviceEntity d, RoomEntity r " +
                " where a.deviceId = d.id and d.roomId = r.id and r.floor.gateway.id = ? and a.createTime >= ? and a.createTime < ? order by a.id desc";

        List<AlarmEntity> rs = getListByHQL(hql, gateway.getId(),startDate, endDate);

        return rs;
    }
}
