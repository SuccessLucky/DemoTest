package com.project.controller.rest;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.project.bean.BaseResultBean;
import com.project.common.config.ApiStatusCode;
import com.project.common.config.LoginConstant;
import com.project.entity.AuthUserEntity;
import com.project.entity.GatewayEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.MemberUserEntity;
import com.project.service.AuthUserEntityService;
import com.project.service.GatewayEntityService;
import com.project.service.MemberGatewayEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xieyanhao on 16/3/13.
 */
public class BaseController {

    private static final long serialVersionUID = 1L;

    @Autowired
    protected AuthUserEntityService authUserEntityService;

    @Autowired
    protected GatewayEntityService userGatewayEntityService;

    @Autowired
    protected MemberGatewayEntityService memberGatewayEntityService;

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected BaseResultBean<Object> responseResult;
    protected Gson gson = new Gson();

    @ModelAttribute
    public void prepare(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    protected BaseResultBean<Object> falseResult(String code, String message) {
        responseResult = new BaseResultBean<>();
        responseResult.getError().setCode(code);
        responseResult.getError().setMessage(message);
        return responseResult;
    }

    protected BaseResultBean<Object> trueResult(Object result) {
        responseResult = new BaseResultBean<>();
        responseResult.setSuccess(true);
        if (result != null) {
            responseResult.setResult(result);
        }
        return responseResult;
    }

    /**
     * 通过 token 获取当前用户信息
     *
     * @return
     */
    protected MemberUserEntity getMemberUser() {
        String token = getRequestToken();
        MemberUserEntity memberUser = null;
        if (!Strings.isNullOrEmpty(token)) {
            AuthUserEntity authUserEntity = authUserEntityService.getValidUserBySessionToken(token, LoginConstant.NORMAL_MEMBER_ACCESS);

            if (authUserEntity != null) {
                memberUser = (MemberUserEntity) authUserEntity;
            }
        }
        return memberUser;
    }

    protected String getRequestToken() {
        String token = request.getHeader("token");
        if (Strings.isNullOrEmpty(token)) {
            token = request.getHeader("Token");
        }
        return token;
    }

    protected String getRequestGateway() {
        String gateway = request.getHeader("gateway");
        if (Strings.isNullOrEmpty(gateway)) {
            gateway = request.getHeader("Gateway");
        }
        return gateway;
    }

    protected MemberGatewayEntity getMemberGateway() {
        MemberUserEntity memberUser = null;
        MemberGatewayEntity memberGateway = null;

        String token = getRequestToken();
        if (!Strings.isNullOrEmpty(token)) {
            AuthUserEntity authUserEntity = authUserEntityService.getValidUserBySessionToken(token, LoginConstant.NORMAL_MEMBER_ACCESS);
            if (authUserEntity != null) {
                memberUser = (MemberUserEntity) authUserEntity;
                if (memberUser != null) {
                    String requestGateway = getRequestGateway();
                    if (!Strings.isNullOrEmpty(requestGateway)) {
                        GatewayEntity gateway = userGatewayEntityService.getGatewayByMac(requestGateway);
                        if (gateway != null) {
                            memberGateway = memberGatewayEntityService.getMemberGatewayByMember(memberUser, gateway);
                        }
                    }
                }
            }
        }

        if (memberUser == null) {
            falseResult(ApiStatusCode.NOT_LOGIN_USER, "请先登录");
        } else if (memberGateway == null) {
            falseResult(ApiStatusCode.NOT_IN_THIS_GATEWAY, "请选择网关");
        }

        return memberGateway;
    }
}
