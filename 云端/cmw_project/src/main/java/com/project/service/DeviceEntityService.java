package com.project.service;

import com.project.bean.DeviceResp;
import com.project.entity.DeviceButtonEntity;
import com.project.entity.DeviceEntity;
import com.project.utils.smarttools.DeviceState;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
public interface DeviceEntityService {

    public void addDevice(DeviceEntity entity);
    public List<DeviceEntity> getDevicesByRoomId(int roomId);
    public List<DeviceEntity> getDevicesByGateway(int gatewayId);
    public DeviceEntity getDeviceById(int id);
    public List<DeviceEntity> getDeviceByMacAddress(String macAddress, int gatewayId);
    public void updateDevice(DeviceEntity entity);
    public void deleteDevice(DeviceEntity entity);
    public DeviceResp generateDevice(DeviceEntity entity);
    public List<DeviceButtonEntity> getDeviceButtonsByGateway(int gatewayId);
    public void reportDeviceInfo(DeviceState deviceState, String mac);
    public void updateDeciceSequence(int roomId, int deviceId, int sequence);
}
