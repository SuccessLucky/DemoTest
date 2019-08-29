package com.project.service.impl;

import com.google.common.base.Strings;
import com.project.bean.DeviceResp;
import com.project.bean.SceneReq;
import com.project.bean.SceneResp;
import com.project.dao.DeviceEntityDao;
import com.project.dao.RoomEntityDao;
import com.project.dao.SceneEntityDao;
import com.project.entity.*;
import com.project.service.SceneEntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xieyanhao on 16/10/11.
 */
@Service
public class SceneEntityServiceImpl implements SceneEntityService {

    private static Log logger = LogFactory.getLog(SceneEntityServiceImpl.class);

    @Autowired
    private SceneEntityDao sceneEntityDao;

    @Autowired
    private DeviceEntityDao deviceEntityDao;

    @Autowired
    private RoomEntityDao roomEntityDao;

    @Override
    @Transactional
    public SceneEntity addScene(SceneReq sceneBean, GatewayEntity gateway) throws Exception {

        SceneEntity sceneEntity = new SceneEntity();

        sceneEntity.setName(sceneBean.getName());
        sceneEntity.setImage(sceneBean.getImage());
        sceneEntity.setSceneType(sceneBean.getScene_type());
        sceneEntity.setNeedLinkage(sceneBean.getNeed_linkage());
        sceneEntity.setNeedTimeDelay(sceneBean.getNeed_time_delay());
        sceneEntity.setNeedTiming(sceneBean.getNeed_timing());
        sceneEntity.setNeedSecurityOn(sceneBean.getNeed_security_on());
        sceneEntity.setNeedSecurityOff(sceneBean.getNeed_security_off());
        sceneEntity.setDelayTime(sceneBean.getDelay_time());
        sceneEntity.setTimingTime(sceneBean.getTiming_time());
        sceneEntity.setGateway(gateway);
        //新增字段
        sceneEntity.setSubCommandIdentifer(sceneBean.getSub_command_identifer());
        sceneEntity.setSerialNumber(sceneBean.getSerial_number());
        sceneEntity.setReservedProperty(sceneBean.getReserved_property());
        sceneEntity.setForceLinkage(sceneBean.getForce_linkage());
        sceneEntity.setEnabledOrDisableIdentifer(sceneBean.getEnabled_or_disable_identifer());
        sceneEntity.setArmingOrDisarmingIdentifer(sceneBean.getArming_or_disarming_identifer());
        sceneEntity.setLinkageDeviceMacAddr(sceneBean.getLinkage_device_mac_addr());
        sceneEntity.setLinkageDeviceRoad(sceneBean.getLinkage_device_road());
        sceneEntity.setLinkageDeviceDataType(sceneBean.getLinkage_device_data_type());
        sceneEntity.setLinkageDeviceDataRange(sceneBean.getLinkage_device_data_range());
        sceneEntity.setLinkageTime(sceneBean.getLinkage_time());
        sceneEntity.setLinkageDelayTime(sceneBean.getLinkage_delay_time());

        List<SceneDetailEntity> sceneDetailEntityList = new ArrayList<>();
        int sequence = 1;
        for (SceneReq.SceneDetailBean sceneDetailBean : sceneBean.getScene_details()) {

            DeviceEntity deviceEntity = deviceEntityDao.get(sceneDetailBean.getDevice_id());
            if (deviceEntity == null) {
                logger.warn("addScene 设备信息不存在, scene id : " + sceneEntity.getId());
                throw new Exception("设备信息不存在!");
            }
            SceneDetailEntity sceneDetailEntity = new SceneDetailEntity();
            sceneDetailEntity.setDeviceId(deviceEntity.getId());
            sceneDetailEntity.setDeviceState1(sceneDetailBean.getDevice_state1());
            sceneDetailEntity.setDeviceState2(sceneDetailBean.getDevice_state2());
            sceneDetailEntity.setDeviceState3(sceneDetailBean.getDevice_state3());
            sceneDetailEntity.setScene(sceneEntity);
            sceneDetailEntity.setOtherStatus(sceneDetailBean.getOther_status());
            sceneDetailEntity.setSequence(sequence);
            sceneDetailEntityList.add(sceneDetailEntity);
            sequence++;
        }
        sceneEntity.setSceneDetails(sceneDetailEntityList);
        sceneEntityDao.save(sceneEntity);

        return sceneEntity;
    }

    @Override
    @Transactional
    public void updateScene(SceneReq sceneBean, SceneEntity sceneEntity) throws Exception {

        if (sceneEntity == null) {
            throw new Exception("场景信息不存在");
        }

        sceneEntity.setName(sceneBean.getName());
        sceneEntity.setImage(sceneBean.getImage());
        sceneEntity.setSceneType(sceneBean.getScene_type());
        sceneEntity.setNeedLinkage(sceneBean.getNeed_linkage());
        sceneEntity.setNeedTimeDelay(sceneBean.getNeed_time_delay());
        sceneEntity.setNeedTiming(sceneBean.getNeed_timing());
        sceneEntity.setNeedSecurityOn(sceneBean.getNeed_security_on());
        sceneEntity.setNeedSecurityOff(sceneBean.getNeed_security_off());
        sceneEntity.setDelayTime(sceneBean.getDelay_time());
        sceneEntity.setTimingTime(sceneBean.getTiming_time());

        //新增字段
        if (!Strings.isNullOrEmpty(sceneBean.getSub_command_identifer())) {
            sceneEntity.setSubCommandIdentifer(sceneBean.getSub_command_identifer());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getSerial_number())) {
            sceneEntity.setSerialNumber(sceneBean.getSerial_number());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getReserved_property())) {
            sceneEntity.setReservedProperty(sceneBean.getReserved_property());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getForce_linkage())) {
            sceneEntity.setForceLinkage(sceneBean.getForce_linkage());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getEnabled_or_disable_identifer())) {
            sceneEntity.setEnabledOrDisableIdentifer(sceneBean.getEnabled_or_disable_identifer());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getArming_or_disarming_identifer())) {
            sceneEntity.setArmingOrDisarmingIdentifer(sceneBean.getArming_or_disarming_identifer());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getLinkage_device_mac_addr())) {
            sceneEntity.setLinkageDeviceMacAddr(sceneBean.getLinkage_device_mac_addr());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getLinkage_device_road())) {
            sceneEntity.setLinkageDeviceRoad(sceneBean.getLinkage_device_road());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getLinkage_device_data_type())) {
            sceneEntity.setLinkageDeviceDataType(sceneBean.getLinkage_device_data_type());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getLinkage_device_data_range())) {
            sceneEntity.setLinkageDeviceDataRange(sceneBean.getLinkage_device_data_range());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getLinkage_time())) {
            sceneEntity.setLinkageTime(sceneBean.getLinkage_time());
        }
        if (!Strings.isNullOrEmpty(sceneBean.getLinkage_delay_time())) {
            sceneEntity.setLinkageDelayTime(sceneBean.getLinkage_delay_time());
        }

        Map<Integer, SceneDetailEntity> sceneDetailEntityMap = new HashMap<>();
        for (SceneDetailEntity sceneDetailEntity : sceneEntity.getSceneDetails()) {
            sceneDetailEntityMap.put(sceneDetailEntity.getId(), sceneDetailEntity);
        }

        List<SceneDetailEntity> sceneDetailEntityNew = sceneEntity.getSceneDetails();
        sceneEntity.getSceneDetails().clear();
        int sequence = 1;
        for (SceneReq.SceneDetailBean sceneDetailBean : sceneBean.getScene_details()) {

            DeviceEntity deviceEntity = deviceEntityDao.get(sceneDetailBean.getDevice_id());
            if (deviceEntity == null) {
                logger.warn("updateScene 设备信息不存在, scene id : " + sceneEntity.getId() + " , device id : " + sceneDetailBean.getScene_detail_id());
                throw new Exception("设备信息不存在!");
            }
            SceneDetailEntity sceneDetailEntity = sceneDetailEntityMap.get(sceneDetailBean.getScene_detail_id());
            if (sceneDetailEntity == null) {
                sceneDetailEntity = new SceneDetailEntity();
            }
            sceneDetailEntity.setDeviceId(deviceEntity.getId());
            sceneDetailEntity.setDeviceState1(sceneDetailBean.getDevice_state1());
            sceneDetailEntity.setDeviceState2(sceneDetailBean.getDevice_state2());
            sceneDetailEntity.setDeviceState3(sceneDetailBean.getDevice_state3());
            sceneDetailEntity.setScene(sceneEntity);
            sceneDetailEntity.setOtherStatus(sceneDetailBean.getOther_status());
            sceneDetailEntity.setSequence(sequence);
            sceneDetailEntityNew.add(sceneDetailEntity);
            sequence++;
        }

        sceneEntity.setSceneDetails(sceneDetailEntityNew);
        sceneEntityDao.update(sceneEntity);
    }

    @Override
    @Transactional
    public void saveOrUpdateScene(SceneEntity entity) {
        sceneEntityDao.saveOrUpdate(entity);
    }

    @Override
    public List<SceneEntity> getScenesByGateway(int gatewayId, int sceneType) {
        return sceneEntityDao.getScenesByGateway(gatewayId, sceneType);
    }

    @Override
    public SceneEntity getSceneById(int id) {
        return sceneEntityDao.get(id);
    }

    @Override
    @Transactional
    public SceneResp getSceneRespByScene(SceneEntity sceneEntity) {

        if (sceneEntity == null) {
            return null;
        }

        try {
            SceneResp sceneResp = new SceneResp();

            sceneResp.setScene_id(sceneEntity.getId());
            sceneResp.setName(sceneEntity.getName());
            sceneResp.setImage(sceneEntity.getImage());
            sceneResp.setScene_type(sceneEntity.getSceneType());
            sceneResp.setNeed_linkage(sceneEntity.getNeedLinkage());
            sceneResp.setNeed_time_delay(sceneEntity.getNeedTimeDelay());
            sceneResp.setNeed_timing(sceneEntity.getNeedTiming());
            sceneResp.setNeed_security_on(sceneEntity.getNeedSecurityOn());
            sceneResp.setNeed_security_off(sceneEntity.getNeedSecurityOff());
            sceneResp.setDelay_time(sceneEntity.getDelayTime());
            sceneResp.setTiming_time(sceneEntity.getTimingTime());

            sceneResp.setSub_command_identifer(sceneEntity.getSubCommandIdentifer());
            sceneResp.setSerial_number(sceneEntity.getSerialNumber());
            sceneResp.setReserved_property(sceneEntity.getReservedProperty());
            sceneResp.setForce_linkage(sceneEntity.getForceLinkage());
            sceneResp.setEnabled_or_disable_identifer(sceneEntity.getEnabledOrDisableIdentifer());
            sceneResp.setArming_or_disarming_identifer(sceneEntity.getArmingOrDisarmingIdentifer());
            sceneResp.setLinkage_device_mac_addr(sceneEntity.getLinkageDeviceMacAddr());
            sceneResp.setLinkage_device_road(sceneEntity.getLinkageDeviceRoad());
            sceneResp.setLinkage_device_data_type(sceneEntity.getLinkageDeviceDataType());
            sceneResp.setLinkage_device_data_range(sceneEntity.getLinkageDeviceDataRange());
            sceneResp.setLinkage_time(sceneEntity.getLinkageTime());
            sceneResp.setLinkage_delay_time(sceneEntity.getLinkageDelayTime());

            List<SceneResp.SceneDeviceBean> scene_details = new ArrayList<>();
            for (SceneDetailEntity sceneDetailEntity : sceneEntity.getSceneDetails()) {

                DeviceEntity deviceEntity = deviceEntityDao.get(sceneDetailEntity.getDeviceId());
                if (deviceEntity == null || deviceEntity.isLogicalDeleted()) {
                    continue;
                }
                SceneResp.SceneDeviceBean sceneDeviceBean = new SceneResp.SceneDeviceBean();

                sceneDeviceBean.setScene_detail_id(sceneDetailEntity.getId());
                sceneDeviceBean.setDevice_id(deviceEntity.getId());
                sceneDeviceBean.setRoom_id(deviceEntity.getRoomId());
                sceneDeviceBean.setDevice_name(deviceEntity.getName());
                sceneDeviceBean.setImage(deviceEntity.getImage());
                sceneDeviceBean.setDevice_OD(deviceEntity.getDeviceOD());
                sceneDeviceBean.setDevice_type(deviceEntity.getDeviceType());
                sceneDeviceBean.setCategory(deviceEntity.getCategory());
                sceneDeviceBean.setSindex(deviceEntity.getSindex());
                sceneDeviceBean.setSindex_length(deviceEntity.getsIndexLength());
                sceneDeviceBean.setCmd_id(deviceEntity.getCmdId());
                sceneDeviceBean.setDevice_state1(sceneDetailEntity.getDeviceState1());
                sceneDeviceBean.setDevice_state2(sceneDetailEntity.getDeviceState2());
                sceneDeviceBean.setDevice_state3(sceneDetailEntity.getDeviceState3());
                sceneDeviceBean.setOther_status(sceneDetailEntity.getOtherStatus());
                sceneDeviceBean.setAlarm_status(deviceEntity.getAlarmStatus());
                sceneDeviceBean.setMac_address(deviceEntity.getMacAddress());

                RoomEntity roomEntity = roomEntityDao.get(deviceEntity.getRoomId());
                if (roomEntity != null) {
                    FloorEntity floor = roomEntity.getFloor();
                    sceneDeviceBean.setFloor_id(floor.getId());
                    sceneDeviceBean.setFloor_name(floor.getName());
                    sceneDeviceBean.setRoom_name(roomEntity.getName());
                }

                for (DeviceButtonEntity deviceButtonEntity : deviceEntity.getDeviceButtons()) {
                    DeviceResp.DeviceButtonBean deviceButton = new DeviceResp.DeviceButtonBean();
                    deviceButton.setButton_id(deviceButtonEntity.getId());
                    deviceButton.setName(deviceButtonEntity.getName());
                    deviceButton.setInstruction_code(deviceButtonEntity.getInstructionCode());
                    sceneDeviceBean.getDevice_buttons().add(deviceButton);
                }

                scene_details.add(sceneDeviceBean);
            }
            sceneResp.setScene_details(scene_details);
            return sceneResp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    @Transactional
    public void deleteScene(SceneEntity entity) {
        sceneEntityDao.delete(entity);
    }
}
