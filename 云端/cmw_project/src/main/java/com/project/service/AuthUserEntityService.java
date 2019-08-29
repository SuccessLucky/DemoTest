package com.project.service;

import com.project.bean.UserSessionBean;
import com.project.entity.AuthUserEntity;

/**
 * Created by xieyanhao on 16/3/15.
 */
public interface AuthUserEntityService {

    public AuthUserEntity getValidUserBySessionToken(String sessionToken, int accessType);

    public AuthUserEntity verifyUser(String userName, String userPass, int accessType);

    public void expireSession(UserSessionBean sessionBean);

    public void updateSessionExpireDate(AuthUserEntity user);

    public void updateSessionToken(AuthUserEntity user);

}
