package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.SendSMSEntityDao;
import com.project.entity.SendSMSEntity;
import com.project.utils.DateUtil;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by xieyanhao on 16/4/12.
 */
@Repository
public class SendSMSEntityDaoImpl extends BaseDaoSupport<SendSMSEntity, Integer> implements SendSMSEntityDao {

    @Override
    public SendSMSEntity findSendSMSEntity(String code, String phone, Integer userId, int expiredTime) {

        Date date = DateUtil.getBeforeMinute(expiredTime);
        String hql = "from SendSMSEntity e where e.code=? and e.phone=? and e.userId=? and e.date >=?";
        List<SendSMSEntity> rs = getListByHQL(hql, code, phone, userId, date);

        if (rs != null && rs.size() > 1) {
            return rs.get(0);
        }
        return null;
    }

    @Override
    public SendSMSEntity findSendSMSEntityByPhone(String phone, int expiredTime, int smsType) {

        Date date = DateUtil.getBeforeMinute(expiredTime);
        String hql = "from SendSMSEntity e where e.phone=? and e.date >=? and e.smsType = ? order by e.id desc";
        List<SendSMSEntity> rs = getListByHQL(hql, phone, date, smsType);

        if (rs != null && rs.size() > 0) {
            return rs.get(0);
        }
        return null;
    }

    @Override
    public SendSMSEntity getSendSMSEntityByPhoneAndCode(String phone, String code, int smsType) {

        String hql = "from SendSMSEntity e where e.code=? and e.phone=? and e.smsType=? ";
        List<SendSMSEntity> rs = getListByHQL(hql, code, phone, smsType);

        if (rs != null && rs.size() > 0) {
            return rs.get(0);
        }
        return null;
    }
}
