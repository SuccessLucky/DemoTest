package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.RoomEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
public interface RoomEntityDao extends IBaseDao<RoomEntity, Integer > {

    public List<RoomEntity> getRoomsByFloorId(int floorId);

}
