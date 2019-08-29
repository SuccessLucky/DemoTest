package com.project.service.impl;

import com.project.dao.RoomEntityDao;
import com.project.entity.DeviceEntity;
import com.project.entity.RoomEntity;
import com.project.service.DeviceEntityService;
import com.project.service.RoomEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Service
public class RoomEntityServiceImpl implements RoomEntityService{

    @Autowired
    private RoomEntityDao roomEntityDao;

    @Autowired
    private DeviceEntityService deviceEntityService;

    @Override
    @Transactional
    public void addRoom(RoomEntity entity) {
        roomEntityDao.save(entity);
    }

    @Override
    public RoomEntity getRoomById(int id) {
        return roomEntityDao.get(id);
    }

    @Override
    @Transactional
    public void updateRoom(RoomEntity entity) {
        roomEntityDao.update(entity);
    }

    @Override
    @Transactional
    public void deleteRoom(RoomEntity entity) {

        // 删除设备
        List<DeviceEntity> deviceEntities = deviceEntityService.getDevicesByRoomId(entity.getId());
        for (DeviceEntity deviceEntity : deviceEntities) {

            deviceEntityService.deleteDevice(deviceEntity);
        }
        roomEntityDao.delete(entity);
    }

    @Override
    public List<RoomEntity> getRoomsByFloorId(int floorId) {
        return roomEntityDao.getRoomsByFloorId(floorId);
    }
}
