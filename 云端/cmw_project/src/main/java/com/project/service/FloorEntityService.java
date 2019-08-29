package com.project.service;

import com.project.entity.FloorEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
public interface FloorEntityService {

    public void addFloor(FloorEntity entity);
    public List<FloorEntity> getFloorsByGateway(int gatewayId);
    public FloorEntity getFloorById(int id);
    public void updateFloor(FloorEntity entity);
    public void deleteFloor(FloorEntity entity);

}
