package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.common.config.ApiStatusCode;
import com.project.entity.MemberUserEntity;
import com.project.entity.PushEntity;
import com.project.service.MemberUserEntityService;
import com.project.service.PushEntityService;
import com.project.utils.StringUtil;
import com.project.utils.push.Push;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/29.
 */
@Controller
@RequestMapping("/rest")
public class PushController extends BaseController {

    private static Log logger = LogFactory.getLog(PushController.class);

    @Autowired
    private PushEntityService pushEntityService;

    @Autowired
    private MemberUserEntityService memberUserEntityService;

    // 测试使用
    @RequestMapping(value = "/v1/push/test", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> testPush(@RequestBody ModelMap param) {
        String phone = (String) param.get("phone");
        String action = (String) param.get("type");
        String message = (String) param.get("message");
        String category = (String) param.get("category");

        if (Strings.isNullOrEmpty(phone)) {
            return falseResult(ApiStatusCode.NOT_NULL, "楼层名称不能为空");
        }

        try {

            new Push().toUser(Integer.valueOf(memberUserEntityService.getMemberByLoginName(phone).getUserEntity().getId())).
                    test(action, category, message).send();
            trueResult("success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/push", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> toPush(@RequestBody ModelMap param) {

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        PushEntity pushEntity = null;
        try {
            String uuId = (String) param.get("uuid");
            if (Strings.isNullOrEmpty(uuId)) {
                logger.warn("push token is null");
                return falseResult(ApiStatusCode.NOT_NULL, "push uuid 为空 ");
            }

            MemberUserEntity memberUserEntity = getMemberUser();
            if (memberUserEntity == null) {
                return falseResult(ApiStatusCode.NOT_LOGIN_USER, "请先登录");
            }

            int userId = memberUserEntity.getUserEntity().getId();
            pushEntity = pushEntityService.readPushEntityByUUId(uuId);
            if (pushEntity != null) {
                boolean temp = false;
                if (pushEntity.getUserId() != userId) {
                    pushEntity.setUserId(userId);
                    temp = true;
                }
                if (temp) {
                    try {
                        pushEntity.setUserAgent(StringUtil.userAgent(request));
                    } catch (Exception e) {
                        logger.info("member user client channel invalid format!");
                    }
                    pushEntityService.update(pushEntity);
                }
            } else {
                pushEntity = new PushEntity();
                pushEntity.setUuid(uuId);
                pushEntity.setUserId(userId);

                try {
                    pushEntity.setUserAgent(StringUtil.userAgent(request));
                } catch (Exception e) {
                    logger.info("member user client channel invalid format!");
                }
                pushEntityService.save(pushEntity);
            }

            List<PushEntity> pushEntityList = pushEntityService.readPushEntities(userId);
            for (PushEntity userPush : pushEntityList) {
                if (userPush.getId().equals(pushEntity.getId())) {
                    continue;
                }
                pushEntityService.delete(userPush);
            }

            trueResult("success");
        } catch (Exception e) {
            String errorInfo = pushEntity == null ? "" : pushEntity.toString();
            logger.error("重复插入push token : " + errorInfo, e);
            falseResult(ApiStatusCode.COMMON_ERROR, " 重复插入push token ");
        }
        return responseResult;
    }

    public BaseResultBean<Object> toPush0(@RequestBody ModelMap param) {

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        PushEntity pushEntity = null;
        try {
            String uuid = (String) param.get("uuid");
            if (Strings.isNullOrEmpty(uuid)) {
                logger.warn("push token is null");
                return falseResult(ApiStatusCode.NOT_NULL, "push uuid 为空 ");
            }

            String deviceId = request.getHeader("_pt");
            MemberUserEntity memberUserEntity = getMemberUser();
            if (memberUserEntity == null) {
                return falseResult(ApiStatusCode.NOT_LOGIN_USER, "请先登录");
            }

            int userId = memberUserEntity.getUserEntity().getId();
            pushEntity = pushEntityService.readPushEntityByDeviceId(deviceId);
            if (pushEntity != null) {
                boolean temp = false;
                if (!pushEntity.getUuid().equals(uuid)) {
                    pushEntity.setUuid(uuid);
                    temp = true;
                }
                if (pushEntity.getUserId() != userId) {
                    pushEntity.setUserId(userId);
                    temp = true;
                }
                if (temp) {
                    pushEntityService.update(pushEntity);
                }
            } else {
                pushEntity = new PushEntity();
                pushEntity.setDeviceId(deviceId);
                pushEntity.setUuid(uuid);
                pushEntity.setUserId(userId);
                pushEntityService.save(pushEntity);
            }

            List<PushEntity> pushEntityList = pushEntityService.readPushEntities(userId);
            for (PushEntity userPush : pushEntityList) {
                if (userPush.getId().equals(pushEntity.getId())) {
                    continue;
                }
                pushEntityService.delete(userPush);
            }

            trueResult("success");
        } catch (Exception e) {
            String errorInfo = pushEntity == null ? "" : pushEntity.toString();
            logger.error("重复插入push token : " + errorInfo, e);
            falseResult(ApiStatusCode.COMMON_ERROR, " 重复插入push token ");
        }
        return responseResult;
    }
}
