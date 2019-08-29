package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.SendSMSEntity;

/**
 * Created by xieyanhao on 16/4/12.
 */
public interface SendSMSEntityDao extends IBaseDao<SendSMSEntity, Integer > {

    public SendSMSEntity findSendSMSEntity(String code, String phone, Integer userId, int expiredTime);
    public SendSMSEntity findSendSMSEntityByPhone(String phone, int expiredTime, int smsType);
    public SendSMSEntity getSendSMSEntityByPhoneAndCode(String phone, String code, int smsType);

}
