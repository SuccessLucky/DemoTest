package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.UserEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/3/4.
 */
public interface UserEntityDao extends IBaseDao<UserEntity, Integer > {

    public List<UserEntity> getAllUser();

    public UserEntity getUserByUid(String Uid);

}
