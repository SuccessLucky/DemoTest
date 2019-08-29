package com.project.service.impl;

import com.project.common.config.LoginConstant;
import com.project.dao.SessionStatusEntityDao;
import com.project.entity.AuthUserEntity;
import com.project.entity.SessionStatusEntity;
import com.project.service.SessionStatusEntityService;
import com.project.utils.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by xieyanhao on 16/3/13.
 */
@Service
public class SessionStatusEntityServiceImpl implements SessionStatusEntityService {

    @Autowired
    private SessionStatusEntityDao sessionStatusEntityDao;

    @Override
    @Transactional
    public SessionStatusEntity createNew(AuthUserEntity user, int type) {
        SessionStatusEntity sessionStatus = new SessionStatusEntity();

        Date expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + LoginConstant.SESSION_EXPIRE_TIME);

        sessionStatus.setSessionExpireDate(expireDate);
        sessionStatus.setSessionToken(LoginUtils.generateSessionToken());
        sessionStatus.setType(type);
        sessionStatus.setAuthUserId(user.getId());
        sessionStatusEntityDao.save(sessionStatus);
        return sessionStatus;
    }

}
