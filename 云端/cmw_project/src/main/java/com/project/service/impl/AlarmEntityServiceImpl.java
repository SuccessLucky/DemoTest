package com.project.service.impl;

import com.project.bean.AlarmBeanResp;
import com.project.dao.AlarmEntityDao;
import com.project.dao.DeviceEntityDao;
import com.project.dao.RoomEntityDao;
import com.project.entity.AlarmEntity;
import com.project.entity.DeviceEntity;
import com.project.entity.GatewayEntity;
import com.project.entity.RoomEntity;
import com.project.service.AlarmEntityService;
import com.project.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/28.
 */
@Service
public class AlarmEntityServiceImpl implements AlarmEntityService{

    @Autowired
    private AlarmEntityDao alarmEntityDao;

    @Autowired
    private DeviceEntityDao deviceEntityDao;

    @Autowired
    private RoomEntityDao roomEntityDao;

    @Override
    @Transactional
    public List<AlarmBeanResp> getAlarmByDate(Date startDate, Date endDate, GatewayEntity gateway) {

        List<AlarmEntity> alarmEntityList = alarmEntityDao.getAlarmByDate(startDate, endDate, gateway);

        List<AlarmBeanResp> alarmBeanRespList = new ArrayList<>();
        for (AlarmEntity alarmEntity : alarmEntityList) {
            AlarmBeanResp alarmBean = new AlarmBeanResp();

            DeviceEntity deviceEntity = deviceEntityDao.get(alarmEntity.getDeviceId());
            if (deviceEntity == null || deviceEntity.isLogicalDeleted()) {
                alarmEntity.setLogicalDeletion();
                alarmEntityDao.update(alarmEntity);
                continue;
            }

            RoomEntity roomEntity = roomEntityDao.get(deviceEntity.getRoomId());
            if (roomEntity == null || roomEntity.isLogicalDeleted()) {
                alarmEntity.setLogicalDeletion();
                alarmEntityDao.update(alarmEntity);
                continue;
            }

            alarmBean.setAlarm_id(alarmEntity.getId());
            alarmBean.setCreate_date(DateUtil.dateToString(alarmEntity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            alarmBean.setAlarm_msg(alarmEntity.getMessage());
            alarmBeanRespList.add(alarmBean);
        }

        return alarmBeanRespList;
    }

    @Override
    @Transactional
    public void addAlarm(AlarmEntity alarmEntity) {
        alarmEntityDao.save(alarmEntity);
    }
}
