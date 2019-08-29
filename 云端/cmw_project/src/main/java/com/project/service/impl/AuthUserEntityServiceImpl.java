package com.project.service.impl;

import com.google.common.base.Strings;
import com.project.bean.UserSessionBean;
import com.project.common.config.LoginConstant;
import com.project.dao.AuthUserEntityDao;
import com.project.dao.SessionStatusEntityDao;
import com.project.entity.AuthUserEntity;
import com.project.entity.SessionStatusEntity;
import com.project.service.AuthUserEntityService;
import com.project.utils.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by xieyanhao on 16/3/15.
 */
@Service("authUserEntityService")
public class AuthUserEntityServiceImpl implements AuthUserEntityService{

    @Autowired
    private AuthUserEntityDao authUserEntityDao;

    @Autowired
    private SessionStatusEntityDao sessionStatusEntityDao;

    @Override
    public AuthUserEntity getValidUserBySessionToken(String sessionToken, int accessType) {

        if (Strings.isNullOrEmpty(sessionToken)) return null;

        AuthUserEntity user = authUserEntityDao.getUserBySessionToken(sessionToken, accessType);
        if (user == null) return null;

        Date expireDate = user.getSessionStatusEntity().getSessionExpireDate();
        if (expireDate.before(new Date())) return null;

        return user;
    }

    @Override
    public AuthUserEntity verifyUser(String userName, String userPass, int accessType) {

        if (userName == null || userPass == null)
            return null;
        return authUserEntityDao.verifyUser(userName, userPass, accessType);
    }

    @Override
    public void expireSession(UserSessionBean sessionBean) {

    }

    @Override
    @Transactional
    public void updateSessionExpireDate(AuthUserEntity user) {

        SessionStatusEntity sessionStatusEntity = user.getSessionStatusEntity();

        Date sessionExpireDate = new Date();
        long expireDate = LoginConstant.SESSION_EXPIRE_TIME;

        sessionExpireDate.setTime(sessionExpireDate.getTime() + expireDate);
        sessionStatusEntity.setSessionExpireDate(sessionExpireDate);
        sessionStatusEntityDao.update(sessionStatusEntity);
    }

    @Override
    @Transactional
    public void updateSessionToken(AuthUserEntity user) {
        SessionStatusEntity sessionStatusEntity = user.getSessionStatusEntity();
        sessionStatusEntity.setSessionToken(LoginUtils.generateSessionToken());
        sessionStatusEntityDao.update(sessionStatusEntity);
    }
}
