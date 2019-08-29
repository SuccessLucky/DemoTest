package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.UserEntityDao;
import com.project.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/3/4.
 */
@Repository
public class UserEntityDaoImpl extends BaseDaoSupport<UserEntity, Integer> implements UserEntityDao {

    @Override
    public List<UserEntity> getAllUser() {

        String hql = "form UserEntity";

        return getListByHQL(hql, null);
    }

    @Override
    public UserEntity getUserByUid(String Uid) {

        String hql = "from UserEntity u where u.userUid = ? ";
        List<UserEntity> rs = getListByHQL(hql, Uid);

        if (rs.size() >= 1)
            return rs.get(0);

        return null;

    }

}
