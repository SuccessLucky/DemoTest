package com.project.service;

import com.project.entity.AuthUserEntity;
import com.project.entity.SessionStatusEntity;

/**
 * Created by xieyanhao on 16/3/13.
 */
public interface SessionStatusEntityService {

    public SessionStatusEntity createNew(AuthUserEntity user, int type);

}
