package com.project.service.impl;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.project.bean.PermissionsBean;
import com.project.bean.PermissionsResp;
import com.project.bean.UserGatewayBean;
import com.project.dao.DeviceEntityDao;
import com.project.dao.MemberGatewayEntityDao;
import com.project.dao.RoomEntityDao;
import com.project.dao.SceneEntityDao;
import com.project.entity.*;
import com.project.service.MemberGatewayEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/9/28.
 */
@Service
public class MemberGatewayEntityServiceImpl implements MemberGatewayEntityService{

    @Autowired
    private MemberGatewayEntityDao memberGatewayEntityDao;

    @Autowired
    private SceneEntityDao sceneEntityDao;

    @Autowired
    private DeviceEntityDao deviceEntityDao;

    @Autowired
    private RoomEntityDao roomEntityDao;

    @Override
    @Transactional
    public MemberGatewayEntity addMemberGateway(MemberUserEntity memberUser, GatewayEntity gateway, int memberType) {

        MemberGatewayEntity memberGateway = new MemberGatewayEntity();
        memberGateway.setMemberUser(memberUser);
        memberGateway.setMemberType(memberType);
        memberGateway.setGateway(gateway);

        memberGatewayEntityDao.save(memberGateway);
        return memberGateway;
    }

    @Override
    public List<UserGatewayBean> getUserGateways(MemberUserEntity memberUser) {

        List<MemberGatewayEntity> memberGatewayEntityList = memberGatewayEntityDao.getMemberGateways(memberUser.getId());
        List<UserGatewayBean> userGatewayBeanList = new ArrayList<>();
        for (MemberGatewayEntity memberGatewayEntity : memberGatewayEntityList) {
            UserGatewayBean userGatewayBean = new UserGatewayBean();

            userGatewayBean.setGateway_id(memberGatewayEntity.getId());
            userGatewayBean.setGateway_name(memberGatewayEntity.getGateway().getGatewayName());
            userGatewayBean.setMember_type(memberGatewayEntity.getMemberType());
            userGatewayBean.setSecurity_status(memberGatewayEntity.getGateway().getSecurityStatus());
            userGatewayBean.setMac_address(memberGatewayEntity.getGateway().getMacAddress());
            userGatewayBean.setWifi_mac_address(memberGatewayEntity.getGateway().getWifiMacAddress());
            userGatewayBeanList.add(userGatewayBean);
        }

        return userGatewayBeanList;
    }

    @Override
    public List<MemberGatewayEntity> getMemberGatewaysByGateway(int gatewayId) {
        return memberGatewayEntityDao.getMemberGatewaysByGateway(gatewayId);
    }

    @Override
    public MemberGatewayEntity getMemberGatewayByMember(MemberUserEntity memberUser, GatewayEntity gateway) {
        return memberGatewayEntityDao.getMemberGatewayByMember(memberUser, gateway);
    }

    @Override
    @Transactional
    public void deleteMemberGateway(MemberGatewayEntity memberGatewayEntity) {
        memberGatewayEntityDao.delete(memberGatewayEntity);
    }

    @Override
    @Transactional
    public void updateMemberGateway(MemberGatewayEntity entity) {
        memberGatewayEntityDao.update(entity);
    }

    @Override
    public PermissionsResp getMemberGatewaysPermissions(MemberGatewayEntity memberGatewayEntity) {

        PermissionsResp permissionsResp = new PermissionsResp();

        String permissions = memberGatewayEntity.getPermissions();
        if (Strings.isNullOrEmpty(permissions)) {
            return permissionsResp;
        }

        try {
            PermissionsBean permissionsBean = new Gson().fromJson(permissions, PermissionsBean.class);
            if (permissionsBean != null) {

                List<SceneEntity> sceneEntityList = sceneEntityDao.getScenesByIds(permissionsBean.getScenes());
                List<DeviceEntity> deviceEntityList = deviceEntityDao.getDevicesByIds(permissionsBean.getDevices());

                if (sceneEntityList != null) {
                    for (SceneEntity sceneEntity : sceneEntityList) {

                        PermissionsResp.SceneItemBean sceneItemBean = new PermissionsResp.SceneItemBean();
                        sceneItemBean.setScene_id(sceneEntity.getId());
                        sceneItemBean.setName(sceneEntity.getName());
                        sceneItemBean.setImage(sceneEntity.getImage());

                        permissionsResp.getScenes().add(sceneItemBean);
                    }
                }

                if (deviceEntityList != null) {
                    for (DeviceEntity deviceEntity : deviceEntityList) {

                        PermissionsResp.DeviceItemBean deviceItemBean = new PermissionsResp.DeviceItemBean();
                        deviceItemBean.setDevice_id(deviceEntity.getId());
                        deviceItemBean.setDevice_name(deviceEntity.getName());
                        deviceItemBean.setImage(deviceEntity.getImage());

                        RoomEntity roomEntity = roomEntityDao.get(deviceEntity.getRoomId());
                        if (roomEntity != null) {
                            FloorEntity floor = roomEntity.getFloor();
                            deviceItemBean.setRoom_id(roomEntity.getId());
                            deviceItemBean.setFloor_id(floor.getId());
                            deviceItemBean.setFloor_name(floor.getName());
                            deviceItemBean.setRoom_name(roomEntity.getName());
                        }
                        permissionsResp.getDevices().add(deviceItemBean);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return permissionsResp;
    }
}
