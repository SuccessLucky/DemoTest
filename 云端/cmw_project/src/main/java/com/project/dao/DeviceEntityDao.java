package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.DeviceEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
public interface DeviceEntityDao extends IBaseDao<DeviceEntity, Integer > {

    public List<DeviceEntity> getDevicesByRoomId(int roomId);
    public List<DeviceEntity> getDevicesByGateway(int gatewayId);
    public List<DeviceEntity> getDeviceByMacAddress(String macAddress, int gatewayId);
    public List<DeviceEntity> getDevicesByIds(List<Integer> ids);
    public void updateDeciceSequence(int roomId, int deviceId, int sequence);

}
