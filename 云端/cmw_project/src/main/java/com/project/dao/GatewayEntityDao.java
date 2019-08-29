package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.GatewayEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/7.
 */
public interface GatewayEntityDao extends IBaseDao<GatewayEntity, Integer > {

    public List<GatewayEntity> getAllUserGateway();
    public GatewayEntity getGatewayByMac(String macAddress);

}
