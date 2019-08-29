package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.bean.MemberUserBean;
import com.project.common.AuthenticationFilter;
import com.project.common.config.ApiStatusCode;
import com.project.common.config.LoginConstant;
import com.project.entity.MemberUserEntity;
import com.project.entity.SendSMSEntity;
import com.project.service.AuthUserEntityService;
import com.project.service.MemberUserEntityService;
import com.project.service.SendSMSEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by xieyanhao on 16/3/13.
 */
@Controller
@RequestMapping("/rest")
public class RegisterController extends BaseController {

    @Autowired
    private MemberUserEntityService memberUserEntityService;

    @Autowired
    private SendSMSEntityService sendSMSEntityService;

    @Autowired
    protected AuthUserEntityService authUserEntityService;

    @RequestMapping(value = "/v1/register/phone", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> phoneRegister(@RequestBody ModelMap param) {

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        String code = (String) param.get("code");
        String phone = (String) param.get("phone");
        String nickname = (String) param.get("nickname");
        String password = (String) param.get("password");

        MemberUserBean userBean = new MemberUserBean();
        if (Strings.isNullOrEmpty(phone) || Strings.isNullOrEmpty(code) || Strings.isNullOrEmpty(password)) {
            return falseResult(ApiStatusCode.NOT_NULL, "无法完成您的请求,请完善注册信息");
        } else {

            SendSMSEntity sendSMSEntity = sendSMSEntityService.findSendSMSEntityByPhone(phone, 30, SendSMSEntity.SMS_TYPE_REGISTER);
            if (null == sendSMSEntity) {
                falseResult("", "验证码已过期, 请点击重新发送");
            } else if (!sendSMSEntity.getCode().equals(code)) {
                falseResult("", "您输入的验证码有误,请重新输入");
            } else {

                userBean.setActivated(false);
                userBean.setLoginName(phone);
                userBean.setPassword(password);
                userBean.setPhone(phone);
                userBean.setNickname(nickname);
                userBean.setClient(LoginConstant.FROM_APP_PHONE); // app使用电话注册

                try {

                    MemberUserEntity user = memberUserEntityService.createMemberUser(request, userBean);
                    Map<String, String> reValue = new HashMap<>();
                    reValue.put("token", user.getSessionStatusEntity().getSessionToken());

                    trueResult(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    falseResult(ApiStatusCode.ALREADY_EXISTS, "用户已经存在");
                }
            }
        }

        return responseResult;

    }

    @RequestMapping(value = "/v1/reset_password", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> resetPassword(@RequestBody ModelMap param) {

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        String code = (String) param.get("code");
        String phone = (String) param.get("phone");
        String newPassword = (String) param.get("new_password");

        if (Strings.isNullOrEmpty(phone) || Strings.isNullOrEmpty(code) || Strings.isNullOrEmpty(newPassword)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数有误,无法完成您的请求");
        } else {

            SendSMSEntity sendSMSEntity = sendSMSEntityService.findSendSMSEntityByPhone(phone, 30, SendSMSEntity.SMS_TYPE_RESET);
            if (null == sendSMSEntity) {
                falseResult("", "验证码已过期, 请点击重新发送");
            } else if (!sendSMSEntity.getCode().equals(code)) {
                falseResult("", "您输入的验证码有误,请重新输入");
            } else {
                try {

                    MemberUserEntity member = memberUserEntityService.getMemberByLoginName(phone);
                    member.setUserPass(newPassword);
                    memberUserEntityService.updateMemberUser(member);
                    authUserEntityService.updateSessionToken(member);

                    Map<String, String> reValue = new HashMap<>();
                    reValue.put("token", member.getSessionStatusEntity().getSessionToken());
                    trueResult(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    falseResult(ApiStatusCode.SERVER_EXCEPTION, "无法完成您的请求,请稍后重试");
                }
            }
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/register/sys_user", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> systemRegister(@RequestBody ModelMap param) {

        if (!AuthenticationFilter.authentication(request.getHeader("token"))) {
            return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        String phone = (String) param.get("phone");
        String nickname = (String) param.get("nickname");
        String password = (String) param.get("password");

        MemberUserBean userBean = new MemberUserBean();
        if (Strings.isNullOrEmpty(phone) || Strings.isNullOrEmpty(password)) {
            return falseResult(ApiStatusCode.NOT_NULL, "无法完成您的请求,请完善注册信息");
        } else {

            userBean.setActivated(false);
            userBean.setLoginName(phone);
            userBean.setPassword(password);
            userBean.setPhone(phone);
            userBean.setNickname(nickname);
            userBean.setUserChannel(MemberUserEntity.USER_CHANNEL_SYSTEM); // 系统用户
            userBean.setClient(LoginConstant.FROM_APP_PHONE); // app使用电话注册

            try {

                MemberUserEntity user = memberUserEntityService.createMemberUser(request, userBean);
                Map<String, String> reValue = new HashMap<>();
                reValue.put("token", user.getSessionStatusEntity().getSessionToken());

                trueResult(reValue);
            } catch (Exception e) {
                e.printStackTrace();
                falseResult(ApiStatusCode.ALREADY_EXISTS, "用户已经存在");
            }
        }

        return responseResult;

    }

    @RequestMapping(value = "/v1/register/sys_user_manual", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> systemManualRegister(@RequestBody ModelMap param) {

        if (!AuthenticationFilter.authentication(request.getHeader("token"))) {
            return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        String start = (String) param.get("start");
        String password = (String) param.get("password");
        Integer count = (Integer) param.get("count");

        MemberUserBean userBean = new MemberUserBean();
        if (Strings.isNullOrEmpty(start) || count == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "无法完成您的请求,请完善注册信息");
        } else {

            if (start.length() > 3) {
                return falseResult(ApiStatusCode.NOT_NULL, "start必须是三位数字");
            }

            try {

                for (int i = 0; i < count; i++) {
                    
                    String phone = getLoginName(start);
                    userBean.setActivated(false);
                    userBean.setLoginName(phone);
                    userBean.setPassword(password);
                    userBean.setPhone(phone);
                    userBean.setUserChannel(MemberUserEntity.USER_CHANNEL_SYSTEM_MANUAL); // 系统用户
                    userBean.setClient(LoginConstant.FROM_APP_PHONE); // app使用电话注册

                    MemberUserEntity user = memberUserEntityService.createMemberUser(request, userBean);
                }
                trueResult("success");
            } catch (Exception e) {
                e.printStackTrace();
                falseResult(ApiStatusCode.ALREADY_EXISTS, "用户已经存在");
            }
        }

        return responseResult;

    }

    public static String getLoginName(String start) {
        Random r = new Random();
        String vcode = String.format("%08d", r.nextInt(100000000));
        return start + vcode;
    }

}
