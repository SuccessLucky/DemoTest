package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.bean.FlooorBean;
import com.project.common.config.ApiStatusCode;
import com.project.entity.FloorEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.RoomEntity;
import com.project.service.FloorEntityService;
import com.project.service.RoomEntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Controller
@RequestMapping("/rest")
public class FloorController extends BaseController {

    private static Log logger = LogFactory.getLog(FloorController.class);

    @Autowired
    FloorEntityService floorEntityService;

    @Autowired
    RoomEntityService roomEntityService;

    @RequestMapping(value = "/v1/floor/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> create(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {

            String floorName = (String) param.get("floor_name");
            String image = (String) param.get("image");
            if (Strings.isNullOrEmpty(floorName)) {
                return falseResult(ApiStatusCode.NOT_NULL, "楼层名称不能为空");
            }

            FloorEntity floorEntity = new FloorEntity();
            floorEntity.setImage(image);
            floorEntity.setName(floorName);
            floorEntity.setGateway(memberGateway.getGateway());

            floorEntityService.addFloor(floorEntity);
            FlooorBean flooorBean = generateFloorBean(floorEntity);
            trueResult(flooorBean);

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/floor/update", method = RequestMethod.POST)
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

            String floorName = (String) param.get("floor_name");
            String image = (String) param.get("image");
            Integer id = (Integer) param.get("floor_id");

            if (id == null) {
                return falseResult(ApiStatusCode.PARAM_ERROR, "请求参数有误");
            }
            if (Strings.isNullOrEmpty(floorName)) {
                return falseResult(ApiStatusCode.NOT_NULL, "请输入楼层名称");
            }

            FloorEntity floorEntity = floorEntityService.getFloorById(id);
            if (floorEntity == null) {
                falseResult("", "楼层信息不存在");
            } else {
                if (!Strings.isNullOrEmpty(floorName)) {
                    floorEntity.setName(floorName);
                }
                if (!Strings.isNullOrEmpty(image)) {
                    floorEntity.setImage(image);
                }
                floorEntityService.updateFloor(floorEntity);
                FlooorBean flooorBean = generateFloorBean(floorEntity);
                trueResult(flooorBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/floor/delete", method = RequestMethod.POST)
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
            Integer id = (Integer) param.get("floor_id");
            FloorEntity floorEntity = floorEntityService.getFloorById(id);
            if (floorEntity == null) {
                return falseResult("", "楼层信息不存在");
            }

            List<RoomEntity> roomEntities = roomEntityService.getRoomsByFloorId(floorEntity.getId());
            if (roomEntities.size() > 0) {
                falseResult("", "暂时无法删除楼层,楼层内有房间信息");
            } else {
                floorEntityService.deleteFloor(floorEntity);
                trueResult(null);
            }
        }catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/floor/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getById(@PathVariable String id) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (Strings.isNullOrEmpty(id)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {
            FloorEntity floorEntity = floorEntityService.getFloorById(Integer.parseInt(id));
            if (floorEntity == null) {
                return falseResult("", "楼层信息不存在");
            }

            FlooorBean flooorBean = generateFloorBean(floorEntity);
            trueResult(flooorBean);

        }catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/floor/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getFloorList() {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        try {
            List<FlooorBean> flooorBeanList = new ArrayList<>();
            List<FloorEntity> floorEntities = floorEntityService.getFloorsByGateway(memberGateway.getGateway().getId());
            if (floorEntities.size() > 0) {
                for (FloorEntity floorEntity : floorEntities) {
                    FlooorBean flooorBean = generateFloorBean(floorEntity);
                    flooorBeanList.add(flooorBean);
                }
            }
            trueResult(flooorBeanList);
        }catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    public FlooorBean generateFloorBean(FloorEntity floorEntity) {
        FlooorBean flooorBean = new FlooorBean();
        flooorBean.setId(floorEntity.getId());
        flooorBean.setName(floorEntity.getName());
        flooorBean.setImage(floorEntity.getImage());
        return flooorBean;
    }
}
