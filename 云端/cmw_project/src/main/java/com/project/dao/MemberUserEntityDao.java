package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.MemberUserEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/3/13.
 */
public interface MemberUserEntityDao extends IBaseDao<MemberUserEntity, Integer > {

    public List<MemberUserEntity> getAllMemberUser();

    public MemberUserEntity getMemberUserByUserName(String userName);

}
