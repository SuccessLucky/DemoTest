package com.project.service;

import com.project.bean.MemberUserBean;
import com.project.entity.MemberUserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xieyanhao on 16/3/13.
 */
public interface MemberUserEntityService {

    public MemberUserEntity createMemberUser(HttpServletRequest request, MemberUserBean bean) throws Exception;
    public List<MemberUserEntity> getAllMemberUser();
    public MemberUserEntity getMemberUserById(int id);
    public void updateMemberUser(MemberUserEntity entity);
    public void addMemberUser(MemberUserEntity entity);
    public void deleteMemberUser(MemberUserEntity entity);
    public MemberUserEntity getMemberByLoginName(String loginName);

}
