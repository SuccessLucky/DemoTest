package com.project.service;

import com.project.entity.RoomEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
public interface RoomEntityService {

    public void addRoom(RoomEntity entity);
    public List<RoomEntity> getRoomsByFloorId(int floorId);
    public RoomEntity getRoomById(int id);
    public void updateRoom(RoomEntity entity);
    public void deleteRoom(RoomEntity entity);

}
