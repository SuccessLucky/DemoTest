package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.*;
import com.project.bean.DeviceButtonsReq.DeviceButtonBean;
import com.project.bean.DeviceReq.DeviceItem;
import com.project.common.config.ApiStatusCode;
import com.project.entity.DeviceButtonEntity;
import com.project.entity.DeviceEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.RoomEntity;
import com.project.service.DeviceEntityService;
import com.project.service.RoomEntityService;
import com.project.utils.DeviceUtils;
import com.project.utils.StringUtil;
import com.project.utils.UserUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by xieyanhao on 16/5/3.
 */
@Controller
@RequestMapping("/rest")
public class DeviceController extends BaseController {

    private static Log logger = LogFactory.getLog(DeviceController.class);

    @Autowired
    private DeviceEntityService deviceEntityService;

    @Autowired
    RoomEntityService roomEntityService;

    @RequestMapping(value = "/v1/device/add", method = RequestMethod.POST)
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

            DeviceReq deviceReq = gson.fromJson(gson.toJson(param), DeviceReq.class);
            logger.warn("添加设备 : " + gson.toJson(deviceReq));
            System.out.println("添加设备 : " + gson.toJson(deviceReq));
            List<DeviceItem> deviceItems = deviceReq.getDevices();
            List<DeviceResp> deviceResps = new ArrayList<>();
            for (DeviceItem deviceItem : deviceItems) {
                if (deviceItem.getRoom_id() == null) {
                    return falseResult(ApiStatusCode.NOT_NULL, "房间不存在");
                }
                if (Strings.isNullOrEmpty(deviceItem.getDevice_name())) {
                    return falseResult(ApiStatusCode.NOT_NULL, "设备名称不能为空");
                }
                if (Strings.isNullOrEmpty(deviceItem.getDevice_OD()) || Strings.isNullOrEmpty(deviceItem.getMac_address())) {
                    return falseResult(ApiStatusCode.NOT_NULL, "设备信息不完整");
                }

                RoomEntity roomEntity = roomEntityService.getRoomById(deviceItem.getRoom_id());
                if (roomEntity == null) {
                    return falseResult("", "房间信息不存在");
                }

                String category = deviceItem.getCategory();
                String macAddress = deviceItem.getMac_address();
                int deviceState1 = deviceItem.getDevice_state1();
                int deviceState2 = deviceItem.getDevice_state2();
                int deviceState3 = deviceItem.getDevice_state3();
                DeviceEntity deviceEntity = new DeviceEntity();
                // 红外设备不校验mac地址
                if (!DeviceUtils.isGeneralMacDevice(deviceItem)) {
                    if (deviceEntityService.getDeviceByMacAddress(macAddress, memberGateway.getGateway().getId()).size() > 0) {
                        return falseResult("", "设备信息已经存在,无法再次添加");
                    }
                }
				
                deviceEntity.setRoomId(deviceItem.getRoom_id());
                deviceEntity.setName(deviceItem.getDevice_name());
                deviceEntity.setImage(deviceItem.getImage());
                deviceEntity.setDeviceOD(deviceItem.getDevice_OD());
                deviceEntity.setDeviceType(deviceItem.getDevice_type());
                deviceEntity.setCategory(category);
                deviceEntity.setSindex(deviceItem.getSindex());
                deviceEntity.setsIndexLength(deviceItem.getSindex_length());
                deviceEntity.setCmdId(deviceItem.getCmd_id());
                deviceEntity.setMacAddress(macAddress);
                deviceEntity.setRegional(deviceItem.getRegional());
                deviceEntity.setControlType(deviceItem.getControl_type());

                String otherStatus = deviceItem.getOther_status();
                if (StringUtil.containsChinese(otherStatus)) {
                    otherStatus = "";
                }
                deviceEntity.setOtherInfo(deviceItem.getOther_info());
                deviceEntity.setOtherStatus(otherStatus);
                deviceEntity.setDeviceState1(deviceState1);
                deviceEntity.setDeviceState2(deviceState2);
                deviceEntity.setDeviceState3(deviceState3);

                deviceEntityService.addDevice(deviceEntity);

                DeviceResp deviceResp = deviceEntityService.generateDevice(deviceEntity);
                deviceResps.add(deviceResp);

            }
            trueResult(deviceResps);

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/device/update", method = RequestMethod.POST)
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
            logger.info("更改设备 : " + gson.toJson(param));

            Integer id = (Integer) param.get("device_id");
            String deviceName = (String) param.get("device_name");
            String image = (String) param.get("image");
            Integer deviceState1 = (Integer) param.get("device_state1");
            Integer deviceState2 = (Integer) param.get("device_state2");
            Integer deviceState3 = (Integer) param.get("device_state3");
            String otherState = (String) param.get("other_state");
            String otherInfo = (String) param.get("other_info");
            String regional = (String) param.get("regional");; // 位置信息
            Integer control_type = (Integer) param.get("control_type"); // 控制类型 单控:0,群控:1
            if (StringUtil.containsChinese(otherState)) {
                otherState = "";
            }

            if (id == null || deviceState1 == null) {
                return falseResult(ApiStatusCode.PARAM_ERROR, "请求参数有误");
            }
            if (Strings.isNullOrEmpty(deviceName)) {
                return falseResult(ApiStatusCode.NOT_NULL, "请输入设备名称");
            }

            DeviceEntity deviceEntity = deviceEntityService.getDeviceById(id);
            if (deviceEntity == null) {
                falseResult("", "设备信息不存在");
            } else {
                if (!Strings.isNullOrEmpty(deviceName)) {
                    deviceEntity.setName(deviceName);
                }
                if (!Strings.isNullOrEmpty(image)) {
                    deviceEntity.setImage(image);
                }
                if (deviceState1 != null) {
                    deviceEntity.setDeviceState1(deviceState1);
                }
                if (deviceState2 != null) {
                    deviceEntity.setDeviceState2(deviceState2);
                }
                if (deviceState3 != null) {
                    deviceEntity.setDeviceState3(deviceState3);
                }
                if (!Strings.isNullOrEmpty(otherState)) {
                    deviceEntity.setOtherStatus(otherState);
                }
                if (!Strings.isNullOrEmpty(otherInfo)) {
                    deviceEntity.setOtherInfo(otherInfo);
                }
                if (!Strings.isNullOrEmpty(regional)) {
                    deviceEntity.setRegional(regional);
                }
                if (control_type != null) {
                    deviceEntity.setControlType(control_type);
                }
                deviceEntityService.updateDevice(deviceEntity);

                DeviceResp deviceResp = deviceEntityService.generateDevice(deviceEntity);
                trueResult(deviceResp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "请求服务发生异常,请稍后重试");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/device/{idStr}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getById(@PathVariable String idStr) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (Strings.isNullOrEmpty(idStr)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {
            int id = Integer.parseInt(idStr);
//            if (!UserUtils.isHavePermission(memberGateway, id, MemberGatewayEntity.PERMISSION_DEVICE)) {
//                return falseResult(ApiStatusCode.NOT_GNOUGH_PERMISSION, "权限不够,请联系主账户");
//            }
            DeviceEntity deviceEntity = deviceEntityService.getDeviceById(id);
            if (deviceEntity == null) {
                falseResult("", "设备信息不存在");
            } else {

                DeviceResp deviceResp = deviceEntityService.generateDevice(deviceEntity);
                trueResult(deviceResp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/device", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getByRoomId(@RequestParam("room_id") String room_id) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (Strings.isNullOrEmpty(room_id)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {
            List<DeviceEntity> deviceEntities = deviceEntityService.getDevicesByRoomId(Integer.parseInt(room_id));
            List<DeviceResp> deviceRespList = new ArrayList<>();
            for (DeviceEntity deviceEntity : deviceEntities) {
                // 筛选权限
                if (UserUtils.isHavePermission(memberGateway, deviceEntity.getId(), MemberGatewayEntity.PERMISSION_DEVICE)) {
                    DeviceResp deviceResp = deviceEntityService.generateDevice(deviceEntity);
                    deviceRespList.add(deviceResp);
                }
            }

            trueResult(deviceRespList);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/device/all", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getDeviceListV1() {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        try {
            List<DeviceEntity> deviceEntities = deviceEntityService.getDevicesByGateway(memberGateway.getGateway().getId());
            List<DeviceResp> deviceRespList = new ArrayList<>();
            for (DeviceEntity deviceEntity : deviceEntities) {
                // 筛选权限
                if (UserUtils.isHavePermission(memberGateway, deviceEntity.getId(), MemberGatewayEntity.PERMISSION_DEVICE)) {
                    DeviceResp deviceResp = deviceEntityService.generateDevice(deviceEntity);
                    deviceRespList.add(deviceResp);
                }
            }

            trueResult(deviceRespList);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v2/device/all", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getDeviceListV2() {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        try {
            List<DeviceEntity> deviceEntities = deviceEntityService.getDevicesByGateway(memberGateway.getGateway().getId());

            LinkedHashMap<Integer, List<DeviceResp>> deviceMaps = new LinkedHashMap<>();
            for (DeviceEntity deviceEntity : deviceEntities) {
                // 筛选权限
                if (UserUtils.isHavePermission(memberGateway, deviceEntity.getId(), MemberGatewayEntity.PERMISSION_DEVICE)) {
                    DeviceResp deviceResp = deviceEntityService.generateDevice(deviceEntity);
                    int roomId = deviceEntity.getRoomId();
                    if (deviceMaps.get(roomId) == null) {
                        List<DeviceResp> deviceRespList = new ArrayList<>();
                        deviceRespList.add(deviceResp);
                        deviceMaps.put(roomId, deviceRespList);
                    } else {
                        deviceMaps.get(roomId).add(deviceResp);
                    }
                }
            }

            List<RoomDetailBean> roomDetails = new ArrayList<>();
            for (Integer key : deviceMaps.keySet()) {
                List<DeviceResp> deviceRespList = deviceMaps.get(key);
                if (deviceRespList.size() == 0) continue;
                RoomEntity roomEntity = roomEntityService.getRoomById(deviceRespList.get(0).getRoom_id());
                RoomDetailBean roomDetail = new RoomDetailBean();
                if (roomEntity != null) {
                    roomDetail.setDevices(deviceRespList);
                    roomDetail.setRoom_id(roomEntity.getId());
                    roomDetail.setRoom_image(roomEntity.getImage());
                    roomDetail.setFloor_name(roomEntity.getFloor().getName());
                    roomDetail.setRoom_name(roomEntity.getName());
                }

                roomDetails.add(roomDetail);
            }
            trueResult(roomDetails);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/device/delete", method = RequestMethod.POST)
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
            logger.info("删除设备 : " + gson.toJson(param));

            Integer id = (Integer) param.get("device_id");
            String macAddress = (String) param.get("mac_address");

            List<DeviceEntity> deviceEntityList = new ArrayList<>();
            if (id != null && id > 0) {
                DeviceEntity deviceEntity = deviceEntityService.getDeviceById(id);
                if (deviceEntity != null) {
                    deviceEntityList.add(deviceEntity);
                }
            } else if (!Strings.isNullOrEmpty(macAddress)) {
                deviceEntityList = deviceEntityService.getDeviceByMacAddress(macAddress, memberGateway.getGateway().getId());
            } else {
                return falseResult("", "设备信息不存在");
            }

            if (deviceEntityList.size() == 0) {
                falseResult("", "设备信息不存在");
            } else {
                for (DeviceEntity deviceEntity : deviceEntityList) {
                    deviceEntityService.deleteDevice(deviceEntity);
                }
                trueResult("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }
        return responseResult;
    }

    @RequestMapping(value = "/v1/device/button", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> button(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {
            String requestStr = gson.toJson(param);
            logger.warn("保存设备按键信息 : " + requestStr);

            DeviceButtonsReq requestBean = gson.fromJson(requestStr, DeviceButtonsReq.class);
            Integer deviceId = requestBean.getDevice_id();
            DeviceEntity deviceEntity = deviceEntityService.getDeviceById(deviceId);
            if (deviceEntity == null) {
                return falseResult("", "设备信息不存在");
            }

            Map<String, DeviceButtonEntity> deviceButtonEntityMap = new HashMap<>();
            for (DeviceButtonEntity deviceButtonEntity : deviceEntity.getDeviceButtons()) {
                deviceButtonEntityMap.put(deviceButtonEntity.getInstructionCode(), deviceButtonEntity);
            }

            List<DeviceButtonEntity> newDeviceButtons = deviceEntity.getDeviceButtons();
            deviceEntity.getDeviceButtons().clear();
            for (DeviceButtonBean deviceButtonBean : requestBean.getDevice_buttons()) {

                String instructionCode = deviceButtonBean.getInstruction_code();
                if (Strings.isNullOrEmpty(instructionCode)) {
                    return falseResult("", "设备信息更新失败,指令不能为空");
                }

                DeviceButtonEntity deviceButton = deviceButtonEntityMap.get(instructionCode);
                if (deviceButton == null) {
                    deviceButton = new DeviceButtonEntity();
                    deviceButton.setName(deviceButtonBean.getName());
                    deviceButton.setInstructionCode(instructionCode);
                    deviceButton.setDevice(deviceEntity);
                } else {
                    deviceButton.setName(deviceButtonBean.getName());
                }
                newDeviceButtons.add(deviceButton);
            }

            deviceEntity.setDeviceButtons(newDeviceButtons);
            deviceEntityService.updateDevice(deviceEntity);

            DeviceResp deviceResp = deviceEntityService.generateDevice(deviceEntity);
            trueResult(deviceResp);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/device/button/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> buttonList() {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        try {

            List<DeviceButtonEntity> deviceButtonEntities = deviceEntityService.getDeviceButtonsByGateway(memberGateway.getGateway().getId());

            List<DeviceButtonBean> deviceButtons = new ArrayList<>();
            for (DeviceButtonEntity deviceButtonEntity : deviceButtonEntities) {

                DeviceButtonBean deviceButton = new DeviceButtonBean();
                deviceButton.setButton_id(deviceButtonEntity.getId());
                deviceButton.setName(deviceButtonEntity.getName());
                deviceButton.setInstruction_code(deviceButtonEntity.getInstructionCode());
                deviceButtons.add(deviceButton);
            }
            trueResult(deviceButtons);

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/device/sequence", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> updateSequence(@RequestBody ModelMap param) {
        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {
            RoomSequenceReq requestBean = gson.fromJson(gson.toJson(param), RoomSequenceReq.class);
            int roomId = requestBean.getRoom_id();
            RoomEntity roomEntity = roomEntityService.getRoomById(roomId);
            if (roomEntity == null) {
                falseResult("", "房间信息不存在");
            } else {
                List<Integer> deviceIds = requestBean.getDevice_ids();
                int sequence = 1;
                for (Integer deviceId : deviceIds) {
                    if (deviceId == null) continue;

                    deviceEntityService.updateDeciceSequence(roomId, deviceId, sequence);
                    sequence++;
                }
                trueResult("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }
}
