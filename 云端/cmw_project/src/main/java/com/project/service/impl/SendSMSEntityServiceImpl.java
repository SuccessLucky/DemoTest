package com.project.service.impl;

import com.project.bean.BaseResultBean;
import com.project.common.config.GlobalConfiguration;
import com.project.dao.MemberUserEntityDao;
import com.project.dao.SendSMSEntityDao;
import com.project.entity.AuthUserEntity;
import com.project.entity.SendSMSEntity;
import com.project.service.SendSMSEntityService;
import com.project.utils.YunPianSMSUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by xieyanhao on 16/4/12.
 */
@Service
public class SendSMSEntityServiceImpl implements SendSMSEntityService{

    private static Log logger = LogFactory.getLog(SendSMSEntityServiceImpl.class);

    private String toNum;
    private String code;
    private String sendMessage;

    //编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";

    @Autowired
    private SendSMSEntityDao sendSMSEntityDao;

    @Autowired
    private MemberUserEntityDao memberUserEntityDao;

    @Override
    @Transactional
    public BaseResultBean<Object> sendRegisterSMS(HttpServletRequest request, String phoneNum) throws IOException {

        this.toNum = phoneNum;
        BaseResultBean<Object> resultBean = new BaseResultBean<Object>();
        try {
            if (memberUserEntityDao.getMemberUserByUserName(this.toNum) != null) {
                resultBean.getError().setCode("");
                resultBean.getError().setMessage("此手机号已被注册使用");
            }else if (sendSMSEntityDao.findSendSMSEntityByPhone(toNum, 1, SendSMSEntity.SMS_TYPE_REGISTER) != null) {
                resultBean.getError().setCode("");
                resultBean.getError().setMessage("短信只能在60秒内请求发送一次");
            }else {
                this.code = random(30, SendSMSEntity.SMS_TYPE_REGISTER); // 30分钟内有效
                this.sendMessage = URLEncoder.encode("#code#",ENCODING) +"="
                        + URLEncoder.encode(code, ENCODING);

                String sendResult = YunPianSMSUtil.tplSendSms(GlobalConfiguration.getYunpianTplVertify(), this.sendMessage, toNum);
                logger.warn(sendResult);
                sendSMSRecord(null, SendSMSEntity.SMS_TYPE_REGISTER); // 短信发送记录
                resultBean.setSuccess(true);

            }
        } catch (Exception e) {
            resultBean.getError().setCode("");
            resultBean.getError().setMessage("请输入正确的手机号码");
        }
        return resultBean;
    }

    @Override
    public BaseResultBean<Object> resetPasswordSMS(HttpServletRequest request, String phoneNum) throws IOException {
        this.toNum = phoneNum;
        BaseResultBean<Object> resultBean = new BaseResultBean<Object>();

        try {
            if (memberUserEntityDao.getMemberUserByUserName(phoneNum) == null) {
                resultBean.getError().setCode("");
                resultBean.getError().setMessage("账户信息不存在");
            }else if (sendSMSEntityDao.findSendSMSEntityByPhone(toNum, 1, SendSMSEntity.SMS_TYPE_RESET) != null) {
                resultBean.getError().setCode("");
                resultBean.getError().setMessage("短信只能在60秒内请求发送一次");
            }else {
                this.code = random(30, SendSMSEntity.SMS_TYPE_RESET); // 30分钟内有效
                this.sendMessage = URLEncoder.encode("#code#",ENCODING) +"="
                        + URLEncoder.encode(code, ENCODING);

                String sendResult = YunPianSMSUtil.tplSendSms(GlobalConfiguration.getYunpianTplVertify(), this.sendMessage, toNum);
                logger.warn(sendResult);
                sendSMSRecord(null, SendSMSEntity.SMS_TYPE_RESET); // 短信发送记录
                resultBean.setSuccess(true);
            }
        } catch (Exception e) {
            resultBean.getError().setCode("");
            resultBean.getError().setMessage("请输入正确的手机号码");
        }

        return resultBean;
    }

    @Override
    public SendSMSEntity findSendSMSEntityByPhone(String phone , int expiredTime, int smsType) {
        return sendSMSEntityDao.findSendSMSEntityByPhone(phone, expiredTime, smsType);
    }

    private String random(int minute, int smsType) {
        SendSMSEntity sendSMSEntity = sendSMSEntityDao.findSendSMSEntityByPhone(toNum, minute, smsType);
        if (sendSMSEntity != null) {
            return sendSMSEntity.getCode();
        }
        String code = "";
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < 6; i++) {
            code = code + rand.nextInt(10);
        }
        return code;
    }

    private void sendSMSRecord(AuthUserEntity authUserEntity, int smsType) {

        SendSMSEntity sendSMSEntity = new SendSMSEntity();
        if (authUserEntity != null) {
            sendSMSEntity.setUserId(authUserEntity.getId());
        }
        sendSMSEntity.setPhone(toNum);
        sendSMSEntity.setCode(code);
        sendSMSEntity.setSmsType(smsType);
        sendSMSEntityDao.save(sendSMSEntity);
    }

    @Override
    @Transactional
    public void updateSMSRecord(SendSMSEntity sendSMSEntity) {
        sendSMSEntityDao.update(sendSMSEntity);
    }

    @Override
    public SendSMSEntity getSendSMSEntityByPhoneAndCode(String phone, String code, int smsType) {
        return sendSMSEntityDao.getSendSMSEntityByPhoneAndCode(phone, code, smsType);
    }
}
