package com.project.service.impl;

import com.google.common.base.Strings;
import com.project.bean.MemberUserBean;
import com.project.common.config.LoginConstant;
import com.project.dao.MemberGatewayEntityDao;
import com.project.dao.MemberUserEntityDao;
import com.project.dao.UserEntityDao;
import com.project.entity.*;
import com.project.service.GatewayEntityService;
import com.project.service.MemberUserEntityService;
import com.project.service.SessionStatusEntityService;
import com.project.utils.StringUtil;
import com.project.utils.UserUtils;
import javassist.bytecode.DuplicateMemberException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xieyanhao on 16/3/13.
 */
@Service
public class MemberUserEntityServiceImpl implements MemberUserEntityService {

    private static Log logger = LogFactory.getLog(MemberUserEntityServiceImpl.class);

    @Autowired
    private MemberUserEntityDao memberUserEntityDao;

    @Autowired
    private UserEntityDao userEntityDao;

    @Autowired
    private SessionStatusEntityService sessionStatusEntityService;

    @Autowired
    private MemberGatewayEntityDao memberGatewayEntityDao;

    @Autowired
    private GatewayEntityService gatewayEntityService;

    private boolean verifyMemberUserBeanForRegister(MemberUserBean userBean){

        boolean result = false;
        if (userBean.getPassword() == null || userBean.getPassword().length() == 0) {
            return result;
        }

        String name = "";
        if (!Strings.isNullOrEmpty(userBean.getLoginName())) {
            name = userBean.getLoginName();
        } else if (!Strings.isNullOrEmpty(userBean.getPhone())) {
            name = userBean.getPhone();
        } else if (!Strings.isNullOrEmpty(userBean.getEmail())) {
            name = userBean.getEmail();
        }
        MemberUserEntity user = memberUserEntityDao.getMemberUserByUserName(name);
        if (user == null) {
            result = true;
        }
        return result;
    }

    @Override
    @Transactional
    public MemberUserEntity createMemberUser(HttpServletRequest request, MemberUserBean userBean) throws Exception {

        boolean result = verifyMemberUserBeanForRegister(userBean);
        if (!result) {
            throw new DuplicateMemberException("global.loginSignUpUserAlreadyExists");
        }

        MemberUserEntity memberUser = new MemberUserEntity();
        memberUser.setUserName(userBean.getLoginName());
        memberUser.setUserPass(userBean.getPassword());
        memberUser.setUserChannel(userBean.getUserChannel());

        UserEntity userEntity = new UserEntity();
        try {
            userEntity.setClientChannel(StringUtil.userAgent(request));
        } catch (Exception e) {
             logger.info("member user client channel invalid format!");
        }
        userEntity.setClient(userBean.getClient());
        userEntity.setUserUid(getUid());
        userEntity.setEmail(userBean.getLoginName());
        userEntity.setMobile(userBean.getPhone());
        userEntity.setEmail(userBean.getEmail());
        userEntity.setNickname(userBean.getNickname());
        userEntity.setImage(userBean.getImage());
        userEntity.setAccountStatus(0);
        memberUser.setUserEntity(userEntity);

        memberUserEntityDao.save(memberUser);
        SessionStatusEntity sessionStatus = sessionStatusEntityService.createNew(memberUser, LoginConstant.NORMAL_MEMBER_ACCESS);
        memberUser.setSessionStatusEntity(sessionStatus);
        return memberUser;
    }

    private String getUid() {
        for (int i = 0;i < 6; i++){
            String var = UserUtils.generateUserUID();
            UserEntity userEntityUid = userEntityDao.getUserByUid(var);
            if(userEntityUid == null){
                return var;
            }
        }
        return null;
    }

    @Override
    public List<MemberUserEntity> getAllMemberUser() {
        return memberUserEntityDao.getAllMemberUser();
    }

    @Override
    public MemberUserEntity getMemberUserById(int id) {
        return memberUserEntityDao.get(id);
    }

    @Override
    @Transactional
    public void updateMemberUser(MemberUserEntity entity) {
        memberUserEntityDao.update(entity);
    }

    @Override
    @Transactional
    public void addMemberUser(MemberUserEntity entity) {
        memberUserEntityDao.save(entity);
    }

    @Override
    @Transactional
    public void deleteMemberUser(MemberUserEntity entity) {

        try {
            List<MemberGatewayEntity> memberGatewayEntityList = memberGatewayEntityDao.getMemberGateways(entity.getId());
            for (MemberGatewayEntity memberGatewayEntity : memberGatewayEntityList) {

                if (memberGatewayEntity.getMemberType() == MemberGatewayEntity.MEMBER_TYPY_FAMILY) {
                    memberGatewayEntityDao.delete(memberGatewayEntity);
                } else {
                    GatewayEntity gatewayEntity = memberGatewayEntity.getGateway();
                    if (gatewayEntity == null) {
                        continue;
                    }

                    // 删除网关
                    gatewayEntityService.deleteGateway(gatewayEntity);
                }
            }
        } catch (Exception e) {
            logger.error("删除账户信息异常 : " + entity.getUserName(), e);
        }
    }

    @Override
    public MemberUserEntity getMemberByLoginName(String loginName) {
        return memberUserEntityDao.getMemberUserByUserName(loginName);
    }
}
