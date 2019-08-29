package com.project.dao.impl;

import com.project.common.config.LoginConstant;
import com.project.common.dao.BaseDaoSupport;
import com.project.dao.MemberUserEntityDao;
import com.project.entity.MemberUserEntity;
import com.project.entity.SessionStatusEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/3/13.
 */
@Repository
public class MemberUserEntityDaoImpl extends BaseDaoSupport<MemberUserEntity, Integer> implements MemberUserEntityDao{

    @Override
    public List<MemberUserEntity> getAllMemberUser() {

        String hql = "form MemberUserEntity";

        return getListByHQL(hql, null);
    }

    @Override
    public MemberUserEntity getMemberUserByUserName(String userName) {
        String hql = "from MemberUserEntity u where u.userName = ? ";

        List<MemberUserEntity> ret = getListByHQL(hql, userName);
        if (ret.size() > 0) {
            MemberUserEntity memberUserEntity = ret.get(0);
            setSessionStatusEntity(memberUserEntity);
            return memberUserEntity;
        }
        return null;
    }

    private void setSessionStatusEntity(MemberUserEntity memberUserEntity) {
        final Integer authUserId = memberUserEntity.getId();
        final String _session = "from SessionStatusEntity s where s.authUserId=? and s.type=?";

        Query query = this.getSession().createQuery(_session);
        query.setParameter(0, authUserId);
        query.setParameter(1, LoginConstant.NORMAL_MEMBER_ACCESS);
        SessionStatusEntity sessionStatusEntity = (SessionStatusEntity)query.uniqueResult();

        if (null != sessionStatusEntity) {
            memberUserEntity.setSessionStatusEntity(sessionStatusEntity);
            sessionStatusEntity.setAuthUserEntity(memberUserEntity);
        }
    }
}
