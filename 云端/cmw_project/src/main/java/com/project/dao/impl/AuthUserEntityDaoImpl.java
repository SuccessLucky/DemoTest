package com.project.dao.impl;

import com.project.common.config.LoginConstant;
import com.project.common.dao.BaseDaoSupport;
import com.project.dao.AuthUserEntityDao;
import com.project.entity.AuthUserEntity;
import com.project.entity.MemberUserEntity;
import com.project.entity.SessionStatusEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by xieyanhao on 16/3/15.
 */
@Repository
public class AuthUserEntityDaoImpl extends BaseDaoSupport<AuthUserEntity, Integer> implements AuthUserEntityDao {

    private String mapAccessTypeToEntityName(int accessType) {
        String entityName = "";

        if (accessType == LoginConstant.ADMIN_ACCESS)
            entityName = MemberUserEntity.class.getSimpleName();
        else if (accessType == LoginConstant.NORMAL_MEMBER_ACCESS)
            entityName = MemberUserEntity.class.getSimpleName();
        else
            assert false : "bug!";

        return entityName;
    }

    @Override
    public AuthUserEntity getUserBySessionToken(String sessionToken, int accessType) {

        assert sessionToken != null && sessionToken.length() > 0 : "Invalid sessionToken";

        AuthUserEntity authUserEntity = null;
        final String _session = "from SessionStatusEntity s where s.sessionToken= ? and s.type= ?";

        Query query = this.getSession().createQuery(_session);
        query.setParameter(0, sessionToken);
        query.setParameter(1, accessType);
        SessionStatusEntity sessionStatusEntity = (SessionStatusEntity)query.uniqueResult();

        if (sessionStatusEntity != null) {
            String entityName = mapAccessTypeToEntityName(accessType);
            final int authUserId = sessionStatusEntity.getAuthUserId();
            final String _authUser = "from " + entityName + " a where a.id = ?";
            authUserEntity = getByHQL(_authUser, authUserId);

            if (authUserEntity != null) {
                authUserEntity.setSessionStatusEntity(sessionStatusEntity);
                sessionStatusEntity.setAuthUserEntity(authUserEntity);
            }
        }

        return authUserEntity;
    }

    @Override
    public AuthUserEntity verifyUser(String userName, String userPass, int accessType) {

        String entityName = mapAccessTypeToEntityName(accessType);
        final String hql = "from " + entityName + " a where a.userName=? and a.userPass=?";

        AuthUserEntity authUserEntity = getByHQL(hql, userName, userPass);

        if (authUserEntity != null) {
            final int authUserId = authUserEntity.getId();
            final String _session = "from SessionStatusEntity s where s.authUserId=? and s.type=?";

            Query query = this.getSession().createQuery(_session);
            query.setParameter(0, authUserId);
            query.setParameter(1, accessType);
            SessionStatusEntity sessionStatusEntity = (SessionStatusEntity)query.uniqueResult();

            if (sessionStatusEntity != null) {
                sessionStatusEntity.setAuthUserEntity(authUserEntity);
                authUserEntity.setSessionStatusEntity(sessionStatusEntity);
            }
        }

        return authUserEntity;
    }
}
