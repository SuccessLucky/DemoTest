package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.common.config.ApiStatusCode;
import com.project.entity.MemberUserEntity;
import com.project.service.MemberUserEntityService;
import com.project.service.SendSMSEntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyanhao on 16/4/12.
 */
@Controller
@RequestMapping("/rest")
public class SmsController extends BaseController {
    private static Log logger = LogFactory.getLog(SmsController.class);

    @Autowired
    private SendSMSEntityService sendSMSEntityService;

    @Autowired
    private MemberUserEntityService memberUserEntityService;

    @RequestMapping(value = "/v1/send_sms/register", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> sendRegisterSMS(@RequestBody ModelMap param) {
        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {
            String phoneNum = (String) param.get("phone");
            if (Strings.isNullOrEmpty(phoneNum)) {
                return falseResult("", "手机号不能为空");
            }
            return sendSMSEntityService.sendRegisterSMS(request, phoneNum);
        } catch (Exception e) {
            e.printStackTrace();
            return falseResult("", "短信发送失败,请重新发送");
        }
    }

    @RequestMapping(value = "/v1/send_sms/reset_password", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> resetPasswordSMS(@RequestBody ModelMap param) {
        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {
            String phoneNum = (String) param.get("phone");

            if (Strings.isNullOrEmpty(phoneNum)) {
                return falseResult("", "手机号不能为空");
            }

            MemberUserEntity member = memberUserEntityService.getMemberByLoginName(phoneNum);
            if (member == null) {
                return falseResult("", "账户信息不存在");
            } else if (member.getUserChannel() != MemberUserEntity.USER_CHANNEL_NORMAL) {
                return falseResult("", "系统账号，不支持找回密码！");
            }

            return sendSMSEntityService.resetPasswordSMS(request, phoneNum);
        } catch (Exception e) {
            e.printStackTrace();
            return falseResult("", "短信发送失败,请重新发送");
        }
    }
}
