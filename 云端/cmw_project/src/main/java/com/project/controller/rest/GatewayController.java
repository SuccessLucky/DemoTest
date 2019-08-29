package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.bean.GatewayMemberBean;
import com.project.bean.UserGatewayBean;
import com.project.common.AuthenticationFilter;
import com.project.common.config.ApiStatusCode;
import com.project.entity.GatewayEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.MemberUserEntity;
import com.project.service.GatewayEntityService;
import com.project.service.MemberGatewayEntityService;
import com.project.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xieyanhao on 16/9/28.
 */
@Controller
@RequestMapping("/rest")
public class GatewayController extends BaseController {

    private static Log logger = LogFactory.getLog(GatewayController.class);

    @Autowired
    private GatewayEntityService gatewayEntityService;

    @Autowired
    private MemberGatewayEntityService memberGatewayEntityService;

    @RequestMapping(value = "/v1/gateway/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> create(@RequestBody ModelMap param) {

        MemberUserEntity memberUser = getMemberUser();
        if (memberUser == null) {
            return falseResult(ApiStatusCode.NOT_LOGIN_USER, "请先登录");
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {

            String gatewayName = (String) param.get("gateway_name");
            String gatewayMac = (String) param.get("mac_address");
            String wifiMacAddress = (String) param.get("wifi_mac_address");
            if (Strings.isNullOrEmpty(gatewayMac) || Strings.isNullOrEmpty(gatewayName)) {
                return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
            }

            GatewayEntity gatewayEntity = userGatewayEntityService.getGatewayByMac(gatewayMac);
            if (gatewayEntity == null) {
                // 创建网关环境
                gatewayEntity = new GatewayEntity();
                gatewayEntity.setGatewayName(gatewayName);
                gatewayEntity.setMacAddress(gatewayMac);
                gatewayEntity.setWifiMacAddress(wifiMacAddress);
                gatewayEntityService.addGateway(gatewayEntity);

                // 添加用户网关
                memberGatewayEntityService.addMemberGateway(memberUser, gatewayEntity, MemberGatewayEntity.MEMBER_TYPY_ADMIN);
                List<UserGatewayBean> userGatewayBeanList = memberGatewayEntityService.getUserGateways(memberUser);

                Map<String, Object> reValue = new HashMap<>();
                reValue.put("user_gateways", userGatewayBeanList);
                return trueResult(reValue);

            } else if (memberGatewayEntityService.getMemberGatewayByMember(memberUser, gatewayEntity) != null) {
                return falseResult(ApiStatusCode.COMMON_ERROR, "已存在于当前网关!");
            } else {
                return falseResult(ApiStatusCode.COMMON_ERROR, "当前网关已经存在,无法再次创建!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getGateways() {

        MemberUserEntity memberUser = getMemberUser();
        if (memberUser == null) {
            return falseResult(ApiStatusCode.NOT_LOGIN_USER, "请先登录");
        }

        try {

            List<UserGatewayBean> userGatewayBeanList = memberGatewayEntityService.getUserGateways(memberUser);
            Map<String, Object> reValue = new HashMap<>();
            reValue.put("user_gateways", userGatewayBeanList);
            return trueResult(reValue);

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }


    @RequestMapping(value = "/v1/gateway/security", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> update(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {

            Integer securityStatus = (Integer) param.get("security_status"); // on off
            if (securityStatus == null) {
                falseResult("", "修改安防状态失败");
            } else {
                GatewayEntity gateway = memberGateway.getGateway();
                gateway.setSecurityStatus(securityStatus);
                gatewayEntityService.updateGateway(gateway);
                trueResult("success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway/info", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getGatewaySecurity() {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        try {
            GatewayEntity gateway = memberGateway.getGateway();
            UserGatewayBean userGatewayBean = new UserGatewayBean();

            userGatewayBean.setGateway_id(memberGateway.getId());
            userGatewayBean.setGateway_name(gateway.getGatewayName());
            userGatewayBean.setMember_type(memberGateway.getMemberType());
            userGatewayBean.setSecurity_status(gateway.getSecurityStatus());
            userGatewayBean.setMac_address(gateway.getMacAddress());
            userGatewayBean.setWifi_mac_address(gateway.getWifiMacAddress());

            trueResult(userGatewayBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> deleteById(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        if (memberGateway.getMemberType() != MemberGatewayEntity.MEMBER_TYPY_ADMIN) {
            return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
        }

        try {

            logger.warn(memberGateway.getMemberUser().getUserName() + ", 删除网关 : " + gson.toJson(param));
            String macAddress = (String) param.get("gateway_mac");

            GatewayEntity gatewayEntity = userGatewayEntityService.getGatewayByMac(macAddress);
            if (gatewayEntity == null) {
                return falseResult(ApiStatusCode.COMMON_ERROR, "网关不存在!");
            }

            // 删除网关
            gatewayEntityService.deleteGateway(gatewayEntity);
            trueResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway/init", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> initByMac() {

        if (!AuthenticationFilter.authentication(request.getParameter("token"))) {
            return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
        }

        try {

            String macAddress = request.getParameter("mac");
            logger.warn(StringUtil.getIpAddr(request) + ", 删除网关 : " + macAddress);

            if (!Strings.isNullOrEmpty(macAddress)) {
                macAddress = macAddress.toUpperCase();
            }

            GatewayEntity gatewayEntity = userGatewayEntityService.getGatewayByMac(macAddress);
            if (gatewayEntity == null) {
                return falseResult(ApiStatusCode.COMMON_ERROR, "网关不存在!");
            }

            // 删除网关
            gatewayEntityService.deleteGateway(gatewayEntity);
            trueResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway/details", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getGatewayDetails() {

        if (!AuthenticationFilter.authentication(request.getParameter("token"))) {
            return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
        }

        try {
            String macAddress = request.getParameter("mac");
            GatewayEntity gatewayEntity = userGatewayEntityService.getGatewayByMac(macAddress);
            if (gatewayEntity == null) {
                return falseResult(ApiStatusCode.COMMON_ERROR, "网关不存在!");
            }

            List<MemberGatewayEntity> memberGatewayEntityList = memberGatewayEntityService.getMemberGatewaysByGateway(gatewayEntity.getId());
            List<GatewayMemberBean> gatewayMemberBeanList = new ArrayList<>();
            for (MemberGatewayEntity memberGatewayEntity : memberGatewayEntityList) {
                GatewayMemberBean gatewayMemberBean = new GatewayMemberBean();

                MemberUserEntity memberUserEntity = memberGatewayEntity.getMemberUser();
                gatewayMemberBean.setMember_id(memberUserEntity.getId());
                gatewayMemberBean.setAccount(memberUserEntity.getUserName());
                gatewayMemberBean.setMember_name(memberUserEntity.getUserEntity().getNickname());
                gatewayMemberBean.setMember_type(memberGatewayEntity.getMemberType());
                gatewayMemberBean.setImage(memberUserEntity.getUserEntity().getImage());
                gatewayMemberBeanList.add(gatewayMemberBean);
            }

            return trueResult(gatewayMemberBeanList);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求发生异常");
        }
        return responseResult;
    }
}
