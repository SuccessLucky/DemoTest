package com.project.service.impl;

import com.project.dao.*;
import com.project.entity.*;
import com.project.service.DeviceEntityService;
import com.project.service.GatewayEntityService;
import com.project.service.SessionStatusEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/8.
 */
@Service
public class GatewayEntityServiceImpl implements GatewayEntityService {

    @Autowired
    private GatewayEntityDao gatewayEntityDao;

    @Autowired
    private MemberUserEntityDao memberUserEntityDao;

    @Autowired
    private UserEntityDao userEntityDao;

    @Autowired
    private SessionStatusEntityService sessionStatusEntityService;

    @Autowired
    private MemberGatewayEntityDao memberGatewayEntityDao;

    @Autowired
    private SceneEntityDao sceneEntityDao;

    @Autowired
    private DeviceEntityService deviceEntityService;

    @Autowired
    private FloorEntityDao floorEntityDao;

    @Override
    public GatewayEntity getGatewayByMac(String macAddress) {
        return gatewayEntityDao.getGatewayByMac(macAddress);
    }

    @Override
    @Transactional
    public void addGateway(GatewayEntity gateway) {
        gatewayEntityDao.save(gateway);
    }

    @Override
    @Transactional
    public void updateGateway(GatewayEntity gateway) {
        gatewayEntityDao.update(gateway);
    }

    @Override
    @Transactional
    public void deleteGateway(GatewayEntity gatewayEntity) {

        // 删除场景
        List<SceneEntity> sceneEntities = sceneEntityDao.getScenesByGateway(gatewayEntity.getId(), -1);
        for (SceneEntity sceneEntity : sceneEntities) {
            sceneEntityDao.delete(sceneEntity);
        }

        // 删除设备
        List<DeviceEntity> deviceEntities = deviceEntityService.getDevicesByGateway(gatewayEntity.getId());
        for (DeviceEntity deviceEntity : deviceEntities) {

            deviceEntityService.deleteDevice(deviceEntity);
        }

        // 删除楼层
        List<FloorEntity> floorEntities = floorEntityDao.getFloorsByGateway(gatewayEntity.getId());
        for (FloorEntity floorEntity : floorEntities) {
            floorEntityDao.delete(floorEntity);
        }

        // 删除网关下所有账户
        List<MemberGatewayEntity> memberGatewayEntities = memberGatewayEntityDao.getMemberGatewaysByGateway(gatewayEntity.getId());
        for (MemberGatewayEntity memberGateway : memberGatewayEntities) {
            memberGatewayEntityDao.delete(memberGateway);
        }

        // 删除网关
        gatewayEntityDao.delete(gatewayEntity);
    }
}
