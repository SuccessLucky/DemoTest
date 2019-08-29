package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.MemberGatewayEntityDao;
import com.project.entity.GatewayEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.MemberUserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/9/28.
 */
@Repository
public class MemberGatewayEntityDaoImpl extends BaseDaoSupport<MemberGatewayEntity, Integer> implements MemberGatewayEntityDao{

    @Override
    public MemberGatewayEntity getMemberGatewayByMember(MemberUserEntity memberUser, GatewayEntity gateway) {

        String hql = "from MemberGatewayEntity m where m.memberUser.id = ? and m.gateway.id = ? ";
        List<MemberGatewayEntity> rs = getListByHQL(hql, memberUser.getId(), gateway.getId());

        if (rs.size() > 0)
            return rs.get(0);

        return null;
    }

    @Override
    public List<MemberGatewayEntity> getMemberGateways(int memberId) {

        String hql = "from MemberGatewayEntity m where m.gateway.flag = 0 and m.memberUser.id = ? ";
        List<MemberGatewayEntity> memberGateways = getListByHQL(hql, memberId);
        if (memberGateways.isEmpty()) {
            memberGateways = new ArrayList<>();
        }
        return memberGateways;
    }

    @Override
    public List<MemberGatewayEntity> getMemberGatewaysByGateway(int gatewayId) {
        String hql = "from MemberGatewayEntity m where m.gateway.id = ? ";
        return getListByHQL(hql, gatewayId);
    }
}
