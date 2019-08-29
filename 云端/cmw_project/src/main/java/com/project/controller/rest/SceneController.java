package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.bean.SceneReq;
import com.project.bean.SceneResp;
import com.project.common.config.ApiStatusCode;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.SceneEntity;
import com.project.service.SceneEntityService;
import com.project.utils.UserUtils;
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
import java.util.List;

/**
 * Created by xieyanhao on 16/10/11.
 */
@Controller
@RequestMapping("/rest")
public class SceneController extends BaseController {

    private static Log logger = LogFactory.getLog(SceneController.class);

    @Autowired
    private SceneEntityService sceneEntityService;

    @RequestMapping(value = "/v1/scene/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> create(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }
//        logger.warn("SceneController add : " + gson.toJson(param) );

        try {

            SceneReq sceneBean = gson.fromJson(gson.toJson(param), SceneReq.class);
            SceneEntity sceneEntity = sceneEntityService.addScene(sceneBean, memberGateway.getGateway());
            // 封装返回结构体
            SceneResp sceneResp = sceneEntityService.getSceneRespByScene(sceneEntity);
            trueResult(sceneResp);

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "信息有误,无法添加场景");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/scene/update", method = RequestMethod.POST)
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

            SceneReq sceneBean = gson.fromJson(gson.toJson(param), SceneReq.class);
            if (sceneBean.getScene_id() == null || sceneBean.getScene_id() <= 0) {
                return falseResult(ApiStatusCode.NOT_NULL, "场景信息不存在");
            }
            SceneEntity sceneEntity = sceneEntityService.getSceneById(sceneBean.getScene_id());
            if (sceneEntity == null || !getRequestGateway().equals(sceneEntity.getGateway().getMacAddress())) {
                return falseResult(ApiStatusCode.NOT_NULL, "场景信息不存在");
            }

            sceneEntityService.updateScene(sceneBean, sceneEntity);
            // 封装返回结构体
            SceneResp sceneResp = sceneEntityService.getSceneRespByScene(sceneEntity);
            trueResult(sceneResp);

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "场景信息有误,无法更新");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/scene/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getScenes() {
        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        try {
            String scene_type = request.getParameter("type");
            int sceneType = -1;
            if (!Strings.isNullOrEmpty(scene_type)) {
                sceneType = Integer.parseInt(scene_type);
            }

            List<SceneEntity> sceneEntityList = sceneEntityService.getScenesByGateway(memberGateway.getGateway().getId(), sceneType);

            List<SceneResp> sceneRespList = new ArrayList<>();
            for (SceneEntity sceneEntity : sceneEntityList) {
                // 筛选权限
                if (UserUtils.isHavePermission(memberGateway, sceneEntity.getId(), MemberGatewayEntity.PERMISSION_SCENE)) {
                    SceneResp sceneResp = sceneEntityService.getSceneRespByScene(sceneEntity);
                    sceneRespList.add(sceneResp);
                }
            }

//            logger.warn("SceneController getScenes : " + gson.toJson(sceneRespList) );
            trueResult(sceneRespList);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/scene/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> deleteById(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {

            Integer id = (Integer) param.get("scene_id");
            SceneEntity sceneEntity = sceneEntityService.getSceneById(id);
            if (sceneEntity == null) {
                falseResult("", "场景信息不存在");
            } else {
                sceneEntityService.deleteScene(sceneEntity);
                trueResult("success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }
}
