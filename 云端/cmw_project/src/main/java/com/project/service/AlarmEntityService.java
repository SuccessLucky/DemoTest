package com.project.service;

import com.project.bean.AlarmBeanResp;
import com.project.entity.AlarmEntity;
import com.project.entity.GatewayEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/28.
 */
public interface AlarmEntityService {

    public List<AlarmBeanResp> getAlarmByDate(Date startDate, Date endDate, GatewayEntity gateway);
    public void addAlarm(AlarmEntity alarmEntity);

}
