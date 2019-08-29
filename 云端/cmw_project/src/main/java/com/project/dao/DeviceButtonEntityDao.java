package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.DeviceButtonEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/16.
 */
public interface DeviceButtonEntityDao extends IBaseDao<DeviceButtonEntity, Integer > {

    public List<DeviceButtonEntity> getDeviceButtonsByGateway(int gatewayId);

}
