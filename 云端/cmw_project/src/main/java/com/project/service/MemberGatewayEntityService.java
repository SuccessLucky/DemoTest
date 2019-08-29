package com.project.service;

import com.project.bean.PermissionsResp;
import com.project.bean.UserGatewayBean;
import com.project.entity.DeviceEntity;
import com.project.entity.GatewayEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.MemberUserEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/28.
 */
public interface MemberGatewayEntityService {

    public MemberGatewayEntity addMemberGateway(MemberUserEntity memberUser, GatewayEntity gateway, int memberType);
    public List<UserGatewayBean> getUserGateways(MemberUserEntity memberUser);
    public List<MemberGatewayEntity> getMemberGatewaysByGateway(int gatewayId);
    public MemberGatewayEntity getMemberGatewayByMember(MemberUserEntity memberUser, GatewayEntity gateway);
    public void deleteMemberGateway(MemberGatewayEntity memberGatewayEntity);
    public void updateMemberGateway(MemberGatewayEntity entity);
    public PermissionsResp getMemberGatewaysPermissions(MemberGatewayEntity memberGatewayEntity);

}
