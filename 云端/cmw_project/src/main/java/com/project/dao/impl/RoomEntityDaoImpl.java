package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.RoomEntityDao;
import com.project.entity.RoomEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Repository
public class RoomEntityDaoImpl extends BaseDaoSupport<RoomEntity, Integer> implements RoomEntityDao {

    @Override
    public List<RoomEntity> getRoomsByFloorId(int floorId) {

        String hql = "from RoomEntity r where r.floor.id = ?";
        List<RoomEntity> rs = getListByHQL(hql, floorId);

        return rs;
    }
}
