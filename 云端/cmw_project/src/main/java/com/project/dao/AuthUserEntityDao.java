package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.AuthUserEntity;

/**
 * Created by xieyanhao on 16/3/15.
 */
public interface AuthUserEntityDao extends IBaseDao<AuthUserEntity, Integer > {

    public AuthUserEntity getUserBySessionToken(String sessionToken, int accessType);

    public AuthUserEntity verifyUser(String userName, String userPass, int accessType);

}
