package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.GatewayEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.MemberUserEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/9/28.
 */
public interface MemberGatewayEntityDao extends IBaseDao<MemberGatewayEntity, Integer > {

    public MemberGatewayEntity getMemberGatewayByMember(MemberUserEntity memberUser, GatewayEntity gateway);

    public List<MemberGatewayEntity> getMemberGateways(int memberId);

    public List<MemberGatewayEntity> getMemberGatewaysByGateway(int gatewayId);

}
