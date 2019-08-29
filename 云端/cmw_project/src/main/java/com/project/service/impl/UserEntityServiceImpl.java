package com.project.service.impl;

import com.project.dao.UserEntityDao;
import com.project.entity.UserEntity;
import com.project.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xieyanhao on 16/3/3.
 */
@Service
public class UserEntityServiceImpl implements UserEntityService {

    @Autowired
    private UserEntityDao userEntityDao;

    @Override
    @Transactional
    public void createUser(UserEntity entity) {
        userEntityDao.save(entity);
    }

    @Override
    public List<UserEntity> getAllUser() {
        return userEntityDao.getAllUser();
    }

    @Override
    public UserEntity getUserById(int id) {
        return userEntityDao.get(id);
    }

    @Override
    @Transactional
    public void updateUser(UserEntity entity) {
        userEntityDao.update(entity);
    }

    @Override
    @Transactional
    public void deleteUser(UserEntity entity) {
        userEntityDao.delete(entity);
    }
}
