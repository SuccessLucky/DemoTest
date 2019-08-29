package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.AlarmEntity;
import com.project.entity.GatewayEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/28.
 */
public interface AlarmEntityDao extends IBaseDao<AlarmEntity, Integer > {

    public List<AlarmEntity> getAlarmByDate(Date startDate, Date endDate, GatewayEntity gateway);
	
}
