package com.project.service;

import com.project.entity.GatewayEntity;

/**
 * Created by xieyanhao on 16/9/8.
 */
public interface GatewayEntityService {

    public GatewayEntity getGatewayByMac(String macAddress);
    public void addGateway(GatewayEntity gateway);
    public void updateGateway(GatewayEntity gateway);
    public void deleteGateway(GatewayEntity gateway);

}
