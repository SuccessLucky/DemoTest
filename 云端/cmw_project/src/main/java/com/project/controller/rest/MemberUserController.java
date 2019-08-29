package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.*;
import com.project.common.AuthenticationFilter;
import com.project.common.config.ApiStatusCode;
import com.project.entity.GatewayEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.MemberUserEntity;
import com.project.service.MemberUserEntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/9/8.
 */
@Controller
@RequestMapping("/rest")
public class MemberUserController extends BaseController  {

    private static Log logger = LogFactory.getLog(MemberUserController.class);

    @Autowired
    private MemberUserEntityService memberUserEntityService;

    @RequestMapping(value = "/v1/gateway_user/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> addGatewayUser(@RequestBody ModelMap param) {

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

        String phone = (String) param.get("phone");
        if (Strings.isNullOrEmpty(phone)) {
            return falseResult(ApiStatusCode.NOT_NULL, "手机号码不能为空");
        }
        try {
            MemberUserEntity memberUserEntity = memberUserEntityService.getMemberByLoginName(phone);
            if (memberUserEntity == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "该账户不存在!");
            }

            GatewayEntity gatewayEntity = memberGateway.getGateway();
            MemberGatewayEntity oldMemberGateway = memberGatewayEntityService.getMemberGatewayByMember(memberUserEntity, gatewayEntity);
            if (oldMemberGateway != null) {
                return falseResult(ApiStatusCode.COMMON_ERROR, "该账户已存在于当前网关,无需再次添加.");
            }

            memberGatewayEntityService.addMemberGateway(memberUserEntity, gatewayEntity, MemberGatewayEntity.MEMBER_TYPY_FAMILY);

            GatewayMemberBean gatewayMemberBean = new GatewayMemberBean();
            gatewayMemberBean.setMember_id(memberUserEntity.getId());
            gatewayMemberBean.setAccount(memberUserEntity.getUserName());
            gatewayMemberBean.setMember_name(memberUserEntity.getUserEntity().getNickname());
            gatewayMemberBean.setMember_type(MemberGatewayEntity.MEMBER_TYPY_FAMILY);
            gatewayMemberBean.setImage(memberUserEntity.getUserEntity().getImage());

            return trueResult(gatewayMemberBean);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway_user/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> create() {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (memberGateway.getMemberType() != MemberGatewayEntity.MEMBER_TYPY_ADMIN) {
            return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
        }

        try {

            List<MemberGatewayEntity> memberGatewayEntityList = memberGatewayEntityService.getMemberGatewaysByGateway(memberGateway.getGateway().getId());
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
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway_user/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> deleteById(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (memberGateway.getMemberType() != MemberGatewayEntity.MEMBER_TYPY_ADMIN) {
            return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
        }

        Integer memberId = (Integer) param.get("member_id");
        if (memberId == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "参数不能为空");
        }

        try {

            MemberUserEntity memberUserEntity = memberUserEntityService.getMemberUserById(memberId);
            if (memberUserEntity == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "网关用户不存在");
            }

            MemberGatewayEntity memberGatewayEntity = memberGatewayEntityService.getMemberGatewayByMember(memberUserEntity, memberGateway.getGateway());
            if (memberGatewayEntity == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "网关用户不存在");
            }

            if (memberGatewayEntity.getMemberType() == MemberGatewayEntity.MEMBER_TYPY_ADMIN) {
                return falseResult(ApiStatusCode.COMMON_ERROR, "无法删除主账户!");
            }

            // 删除网关子用户
            memberGatewayEntityService.deleteMemberGateway(memberGatewayEntity);
            return trueResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway_user/permissions/save", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> addUserPermissions(@RequestBody ModelMap param) {

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

            PermissionsReq requestBean = gson.fromJson(gson.toJson(param), PermissionsReq.class);
            MemberUserEntity memberUserEntity = memberUserEntityService.getMemberUserById(requestBean.getMember_id());
            if (memberUserEntity == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "网关用户不存在");
            }

            MemberGatewayEntity memberGatewayEntity = memberGatewayEntityService.getMemberGatewayByMember(memberUserEntity, memberGateway.getGateway());
            if (memberGatewayEntity == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "网关用户不存在");
            }

            if (memberGatewayEntity.getMemberType() == MemberGatewayEntity.MEMBER_TYPY_ADMIN) {
                return falseResult(ApiStatusCode.COMMON_ERROR, "无法为主账户添加权限!");
            }

            PermissionsBean permissionsBean = new PermissionsBean();
            permissionsBean.setDevices(requestBean.getDevices());
            permissionsBean.setScenes(requestBean.getScenes());

            memberGatewayEntity.setPermissions(gson.toJson(permissionsBean));
            memberGatewayEntityService.updateMemberGateway(memberGatewayEntity);

            // 获取权限列表
            PermissionsResp permissionsResp = memberGatewayEntityService.getMemberGatewaysPermissions(memberGatewayEntity);
            trueResult(permissionsResp);

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway_user/permissions", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getUserPermissions(@RequestParam("member_id") String member_id) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (memberGateway.getMemberType() != MemberGatewayEntity.MEMBER_TYPY_ADMIN) {
            return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
        }

        if (Strings.isNullOrEmpty(member_id)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {

            MemberUserEntity memberUserEntity = memberUserEntityService.getMemberUserById(Integer.parseInt(member_id));
            if (memberUserEntity == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "网关用户不存在");
            }

            MemberGatewayEntity memberGatewayEntity = memberGatewayEntityService.getMemberGatewayByMember(memberUserEntity, memberGateway.getGateway());
            if (memberGatewayEntity == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "网关用户不存在");
            }

            // 获取权限列表
            PermissionsResp permissionsResp = memberGatewayEntityService.getMemberGatewaysPermissions(memberGatewayEntity);
            trueResult(permissionsResp);

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/gateway_user/init", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> deleteById() {

        String phone = request.getParameter("phone");

        try {
            if (!AuthenticationFilter.authentication(request.getParameter("token"))) {
                return falseResult(ApiStatusCode.COMMON_ERROR, "权限不足,无法完成您的请求!");
            }

            MemberUserEntity memberUserEntity = memberUserEntityService.getMemberByLoginName(phone);
            if (memberUserEntity == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "该账户不存在!");
            }

            logger.warn("清空账户信息 : " + phone);
            memberUserEntityService.deleteMemberUser(memberUserEntity);
            trueResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }
}
