package com.project.service;

import com.project.entity.UserEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/3/3.
 */
public interface UserEntityService {

    public void createUser(UserEntity entity);
    public List<UserEntity> getAllUser();
    public UserEntity getUserById(int id);
    public void updateUser(UserEntity entity);
    public void deleteUser(UserEntity entity);

}
