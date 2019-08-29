package com.project.service;

import com.project.bean.BaseResultBean;
import com.project.entity.SendSMSEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by xieyanhao on 16/4/12.
 */
public interface SendSMSEntityService {

    public BaseResultBean<Object> sendRegisterSMS(HttpServletRequest request, String phoneNum) throws IOException;
    public BaseResultBean<Object> resetPasswordSMS(HttpServletRequest request, String phoneNum) throws IOException;
    public SendSMSEntity findSendSMSEntityByPhone(String phone, int expiredTime, int smsType);
    public void updateSMSRecord(SendSMSEntity sendSMSEntity);
    public SendSMSEntity getSendSMSEntityByPhoneAndCode(String phone, String code, int smsType);

}
