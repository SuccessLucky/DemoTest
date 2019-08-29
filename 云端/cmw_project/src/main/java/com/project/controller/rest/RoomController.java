package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.bean.RoomBean;
import com.project.common.config.ApiStatusCode;
import com.project.entity.DeviceEntity;
import com.project.entity.FloorEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.RoomEntity;
import com.project.service.DeviceEntityService;
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
 * Created by xieyanhao on 16/4/29.
 */
@Controller
@RequestMapping("/rest")
public class RoomController extends BaseController {

    private static Log logger = LogFactory.getLog(RoomController.class);

    @Autowired
    RoomEntityService roomEntityService;

    @Autowired
    FloorEntityService floorEntityService;

    @RequestMapping(value = "/v1/room/add", method = RequestMethod.POST)
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

            String roomName = (String) param.get("room_name");
            String image = (String) param.get("image");
            String regional = (String) param.get("regional");
            Integer floorId = (Integer) param.get("floor_id");

            if (floorId == null) {
                return falseResult(ApiStatusCode.NOT_NULL, "楼层信息有误");
            }
            if (Strings.isNullOrEmpty(roomName)) {
                return falseResult(ApiStatusCode.NOT_NULL, "房间名称不能为空");
            }

            FloorEntity floorEntity = floorEntityService.getFloorById(floorId);
            if (floorEntity == null) {
                falseResult("", "楼层信息不存在");
            } else {

                RoomEntity roomEntity = new RoomEntity();
                roomEntity.setName(roomName);
                roomEntity.setFloor(floorEntity);
                roomEntity.setImage(image);
                roomEntity.setRegional(regional);
                roomEntityService.addRoom(roomEntity);
                floorEntity.getRooms().add(roomEntity);
                floorEntityService.updateFloor(floorEntity);

                RoomBean roomBean = generateFloorBean(roomEntity);
                trueResult(roomBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/room/update", method = RequestMethod.POST)
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

            String roomName = (String) param.get("room_name");
            String image = (String) param.get("image");
            String regional = (String) param.get("regional");
            Integer id = (Integer) param.get("room_id");

            if (id == null) {
                return falseResult(ApiStatusCode.PARAM_ERROR, "请求参数有误");
            }
            if (Strings.isNullOrEmpty(roomName)) {
                return falseResult(ApiStatusCode.NOT_NULL, "请输入楼层名称");
            }

            RoomEntity roomEntity = roomEntityService.getRoomById(id);
            if (roomEntity == null) {
                falseResult("", "房间信息不存在");
            } else {
                roomEntity.setName(roomName);
                roomEntity.setImage(image);
                if (!Strings.isNullOrEmpty(regional)) {
                    roomEntity.setRegional(regional);
                }
                roomEntityService.updateRoom(roomEntity);

                RoomBean roomBean = generateFloorBean(roomEntity);
                trueResult(roomBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/room/{id}", method = RequestMethod.GET)
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
            RoomEntity roomEntity = roomEntityService.getRoomById(Integer.parseInt(id));
            if (roomEntity == null) {
                return falseResult("", "房间信息不存在");
            }

            RoomBean roomBean = generateFloorBean(roomEntity);
            trueResult(roomBean);

        }catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/room/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getByFloorId(@RequestParam("floor_id") String floor_id) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (Strings.isNullOrEmpty(floor_id)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {
            List<RoomBean> roomBeanList = new ArrayList<>();
            List<RoomEntity> roomEntities = roomEntityService.getRoomsByFloorId(Integer.parseInt(floor_id));
            if (roomEntities.size() > 0) {
                for (RoomEntity roomEntity : roomEntities) {
                    RoomBean roomBean = generateFloorBean(roomEntity);
                    roomBeanList.add(roomBean);
                }
            }
            trueResult(roomBeanList);
        }catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/room/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> deleteById(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        try {
            Integer id = (Integer) param.get("room_id");
            RoomEntity roomEntity = roomEntityService.getRoomById(id);
            if (roomEntity == null) {
                return falseResult("", "设备信息不存在");
            }

            logger.warn("删除房间 : " + id);
            roomEntityService.deleteRoom(roomEntity);
            trueResult("success");

        }catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    public RoomBean generateFloorBean(RoomEntity roomEntity) {
        RoomBean roomBean = new RoomBean();
        roomBean.setId(roomEntity.getId());
        roomBean.setRegional(roomEntity.getRegional());
        roomBean.setFloor_id(roomEntity.getFloor().getId());
        roomBean.setName(roomEntity.getName());
        roomBean.setImage(roomEntity.getImage());
        return roomBean;
    }

}
