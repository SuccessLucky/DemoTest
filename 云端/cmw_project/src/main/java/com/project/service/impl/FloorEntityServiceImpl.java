package com.project.service.impl;

import com.project.dao.FloorEntityDao;
import com.project.entity.FloorEntity;
import com.project.service.FloorEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Service
public class FloorEntityServiceImpl implements FloorEntityService{

    @Autowired
    private FloorEntityDao floorEntityDao;

    @Override
    @Transactional
    public void addFloor(FloorEntity entity) {
        floorEntityDao.save(entity);
    }

    @Override
    public List<FloorEntity> getFloorsByGateway(int gatewayId) {
        return floorEntityDao.getFloorsByGateway(gatewayId);
    }

    @Override
    public FloorEntity getFloorById(int id) {
        return floorEntityDao.get(id);
    }

    @Override
    @Transactional
    public void updateFloor(FloorEntity entity) {
        floorEntityDao.update(entity);
    }

    @Override
    @Transactional
    public void deleteFloor(FloorEntity entity) {
        floorEntityDao.delete(entity);
    }
}
