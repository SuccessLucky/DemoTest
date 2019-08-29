package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.FloorEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
public interface FloorEntityDao extends IBaseDao<FloorEntity, Integer > {

    public List<FloorEntity> getFloorsByGateway(int gatewayId);

}
