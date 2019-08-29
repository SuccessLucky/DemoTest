package com.project.service.impl;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.project.bean.DeviceResp;
import com.project.bean.DeviceResp.DeviceButtonBean;
import com.project.common.config.GlobalConfiguration;
import com.project.dao.*;
import com.project.entity.*;
import com.project.service.*;
import com.project.utils.YunPianSMSUtil;
import com.project.utils.push.Push;
import com.project.utils.smarttools.DeviceState;
import com.project.utils.smarttools.Tools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Service("deviceEntityService")
public class DeviceEntityServiceImpl implements DeviceEntityService {

    private static Log logger = LogFactory.getLog(DeviceEntityServiceImpl.class);

    @Autowired
    private DeviceEntityDao deviceEntityDao;

    @Autowired
    private DeviceButtonEntityDao deviceButtonEntityDao;

    @Autowired
    private RoomEntityDao roomEntityDao;

    @Autowired
    private UnlockPswEntityDao unlockPswEntityDao;

    @Autowired
    private LockUserEntityDao lockUserEntityDao;

    @Autowired
    private GatewayEntityService gatewayEntityService;

    @Autowired
    private AlarmEntityService alarmEntityService;

    @Autowired
    private MemberGatewayEntityService memberGatewayEntityService;

    @Autowired
    private LockUserEntityService lockUserEntityService;

    @Autowired
    private UserEntityDao userEntityDao;

    @Override
    @Transactional
    public void addDevice(DeviceEntity entity) {
        deviceEntityDao.save(entity);
    }

    @Override
    public List<DeviceEntity> getDevicesByRoomId(int roomId) {
        return deviceEntityDao.getDevicesByRoomId(roomId);
    }

    @Override
    public List<DeviceEntity> getDevicesByGateway(int gatewayId) {
        return deviceEntityDao.getDevicesByGateway(gatewayId);
    }

    @Override
    public DeviceEntity getDeviceById(int id) {
        return deviceEntityDao.get(id);
    }

    @Override
    public List<DeviceEntity> getDeviceByMacAddress(String macAddress, int gatewayId) {
        return deviceEntityDao.getDeviceByMacAddress(macAddress, gatewayId);
    }

    @Override
    @Transactional
    public void updateDevice(DeviceEntity entity) {
        deviceEntityDao.update(entity);
    }

    @Override
    @Transactional
    public void deleteDevice(DeviceEntity entity) {

        // 删除锁密码
        List<UnlockPswEntity> unlockPswEntities = unlockPswEntityDao.getDeviceUnlockPsws(entity.getId());
        for (UnlockPswEntity unlockPswEntity : unlockPswEntities) {
            unlockPswEntityDao.delete(unlockPswEntity);
        }

        // 删除锁用户
        List<LockUserEntity> lockUserEntities = lockUserEntityDao.getDeviceLockUsers(entity.getId());
        for (LockUserEntity lockUserEntity : lockUserEntities) {
            lockUserEntityDao.delete(lockUserEntity);
        }

        deviceEntityDao.delete(entity);
    }

    @Override
    public DeviceResp generateDevice(DeviceEntity deviceEntity) {
        DeviceResp deviceResp = new DeviceResp();

        deviceResp.setDevice_id(deviceEntity.getId());
        deviceResp.setRoom_id(deviceEntity.getRoomId());
        deviceResp.setDevice_name(deviceEntity.getName());
        deviceResp.setImage(deviceEntity.getImage());
        deviceResp.setDevice_OD(deviceEntity.getDeviceOD());
        deviceResp.setCategory(deviceEntity.getCategory());
        deviceResp.setSindex(deviceEntity.getSindex());
        deviceResp.setSindex_length(deviceEntity.getsIndexLength());
        deviceResp.setCmd_id(deviceEntity.getCmdId());
        deviceResp.setDevice_state1(deviceEntity.getDeviceState1());
        deviceResp.setDevice_state2(deviceEntity.getDeviceState2());
        deviceResp.setDevice_state3(deviceEntity.getDeviceState3());
        deviceResp.setOther_status(deviceEntity.getOtherStatus());
        deviceResp.setOther_info(deviceEntity.getOtherInfo());
        deviceResp.setDevice_type(deviceEntity.getDeviceType());
        deviceResp.setAlarm_status(deviceEntity.getAlarmStatus());
        deviceResp.setMac_address(deviceEntity.getMacAddress());
        deviceResp.setRegional(deviceEntity.getRegional());
        deviceResp.setControl_type(deviceEntity.getControlType());

        RoomEntity roomEntity = roomEntityDao.get(deviceEntity.getRoomId());
        if (roomEntity != null) {
            FloorEntity floor = roomEntity.getFloor();
            deviceResp.setFloor_id(floor.getId());
            deviceResp.setFloor_name(floor.getName());
            deviceResp.setRoom_name(roomEntity.getName());
        }

        for (DeviceButtonEntity deviceButtonEntity : deviceEntity.getDeviceButtons()) {
            DeviceButtonBean deviceButton = new DeviceButtonBean();
            deviceButton.setButton_id(deviceButtonEntity.getId());
            deviceButton.setName(deviceButtonEntity.getName());
            deviceButton.setInstruction_code(deviceButtonEntity.getInstructionCode());
            deviceResp.getDevice_buttons().add(deviceButton);
        }

        return deviceResp;
    }

    @Override
    public List<DeviceButtonEntity> getDeviceButtonsByGateway(int gatewayId) {
        return deviceButtonEntityDao.getDeviceButtonsByGateway(gatewayId);
    }

    @Override
    @Transactional
    public void reportDeviceInfo(DeviceState deviceState, String gatewayMacAddress) {

        if (deviceState == null) {
            return;
        }

        try {
            if (deviceState.dstAddr == null) {
                logger.error("上报设备信息失败,设备Mac地址为null : " + new Gson().toJson(deviceState));
                return;
            }

//            logger.warn("开始上报设备信息 : " + new Gson().toJson(deviceState));

            String deviceMacAddress = Tools.byte2HexStr(deviceState.dstAddr);

//            logger.warn("网关Mac地址 : " + gatewayMacAddress);
//            logger.warn("设备Mac地址 : " + deviceMacAddress);
            GatewayEntity gatewayEntity = gatewayEntityService.getGatewayByMac(gatewayMacAddress);
            if (gatewayEntity == null) {
                logger.error("设备上报状态失败,网关不存在 : " + gatewayMacAddress);
            } else {

                List<DeviceEntity> deviceEntities = deviceEntityDao.getDeviceByMacAddress(deviceMacAddress, gatewayEntity.getId());
                if (deviceEntities.isEmpty() || deviceEntities.size() == 0) {
                    logger.error("设备上报状态失败,网关下不存在设备MAC地址 : " + deviceMacAddress);
                } else {
                    int deviceState1 = (int) deviceState.result_data_01; // 状态 默认0:关闭,1:打开
                    int deviceState2 = (int) deviceState.result_data_02; // 状态 默认0:关闭,1:打开
                    int deviceState3 = (int) deviceState.result_data_03; // 状态 默认0:关闭,1:打开
//                    int alarmStatus; // 警报状态 默认0:正常

                    for (DeviceEntity deviceEntity : deviceEntities) {
                        deviceEntity.setDeviceState1(deviceState1);
                        deviceEntity.setDeviceState2(deviceState2);
                        deviceEntity.setDeviceState3(deviceState3);
                        deviceEntityDao.update(deviceEntity);
                    }

                    //检查警报状态
                    checkAlarmState(deviceState, deviceEntities);
                }
            }

        } catch (Exception e) {
            logger.error("上报设备信息失败", e);
        }
    }

    public void checkAlarmState(DeviceState deviceState, List<DeviceEntity> deviceEntities) {

        try {
            DeviceEntity deviceEntity = deviceEntities.get(0);

            String alarmState = "";
            boolean specialDevice = false;
            if (deviceState != null) {
                byte[] srcAddr = deviceState.srcAddr;
                if (srcAddr != null && srcAddr.length > 0) {

                } else {

                    byte[] od = deviceState.deviceOD;
                    if (od == null) {
                        return;
                    }
                    if (od[0] == 0x0f && od[1] == (byte) 0xaa) {// 开关控制4010
                        switch (deviceState.deviceType) {
                            case 0x09://声光警报

                                break;
                            case (byte) 0x81:// 人体红外感应
                                switch (deviceState.deviceProduct) {
                                    case 0x02:// 人体红外感应-new
                                        if (deviceState.result_data_01 == 0x01) {// 开
                                            alarmState = "人体红外感应--有人进入！";
                                        } else if (deviceState.result_data_01 == 0x02) {// 关
                                            System.out.println("红外感应 ：休眠状态或关" + deviceState.result_data_01);
                                        }
                                        break;
                                    case 0x03:// 一氧化碳-new
                                        specialDevice = true;
                                        if (deviceState.result_data_01 == 0x01) {
                                            alarmState = "燃气有警报！";
                                        } else if (deviceState.result_data_01 == 0x02) {
                                            System.out.println("燃气警报解除或无警报：" + deviceState.result_data_01);
                                        }
                                        break;
                                    case 0x04:// 烟雾传感器-new
                                        if (deviceState.deviceProduct == 0x02) {
                                            if (deviceState.result_data_01 == 0x01) {
                                                alarmState = "烟雾有警报！";
                                            } else if (deviceState.result_data_01 == 0x02) {
                                                System.out.println("烟雾警报解除或无警报：" + deviceState.result_data_01);
                                            }
                                        }
                                        break;
                                    case 0x05:
                                        if (deviceState.uploadState == (byte) 0xc0) {
                                            specialDevice = true;
                                            if (deviceState.result_data_01 == 0x01) {
                                                alarmState = String.format("有人SOS求救", deviceEntity.getName());
                                            } else if (deviceState.result_data_01 == 0x02) {
                                                alarmState = String.format("有人取消了SOS求救！", deviceEntity.getName());
                                            }
                                        }
                                        break;
                                }
                                break;
                            case (byte) 0x82:// 一氧化碳感应器

                                break;
                            case (byte) 0x83:// 烟雾感应器

                                break;
                        }
                    } else if (od[0] == 0x0f && od[1] == (byte) 0xbe) {// 多用途休眠设备控制器4030
                        switch (deviceState.deviceType) {
                            case 0x01:
                                if (deviceState.deviceProduct == 0x02) {// 门磁设备 1-开；2-关；3-由关到开；4-由开到关；门磁
                                    switch (deviceState.result_data_01) {
                                        case 0x01:
                                            System.out.println("门磁状态：开---" + deviceState.result_data_01);
                                            alarmState = "门开了！";
                                            break;
                                        case 0x02:
                                            System.out.println("门磁状态：关---" + deviceState.result_data_01);
                                            break;
                                        case 0x03:
                                            alarmState = "门磁状态：由关到开！";
                                            break;
                                        case 0x04:
                                            System.out.println("门磁状态：由开到关---" + deviceState.result_data_01);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                break;
                            case 0x02:

                                String userName = "";
                                if (deviceState.deviceProduct == 0x02) {//02 普通指纹锁
                                    specialDevice = true;

                                    int lockId = deviceState.lockState;
                                    LockUserEntity lockUserEntity = lockUserEntityService.getDeviceLockUserByPsw(deviceEntity.getId(), String.valueOf(lockId));
                                    if (lockUserEntity != null) {
                                        userName = "「" + lockUserEntity.getUserName() + "」";
                                    }
                                    switch (deviceState.lockOperateType) {
                                        case 0x00:
                                            if (deviceState.cmdId == 0x07) {
                                                alarmState = "门锁被" + userName + "远程打开！";
                                            }
                                            break;
                                        case 0x50://指纹开锁上报
                                            alarmState = "门锁被" + userName + "指纹打开！";
                                            break;
                                        case 0x51://密码开锁上报
                                            alarmState = "门锁被密码打开！";
                                            break;
                                        case 0x60://远程开锁上报
                                            alarmState = "门锁被" + userName + "打开！";
                                            break;
                                    }

                                } else if (deviceState.deviceProduct == 0x03) { //03 小蛮腰指纹锁
                                    specialDevice = true;
                                    byte[] lockData = deviceState.lockData;
                                    // lockData[0] = 用户ID, lockData[1] = 操作类型

                                    int lockId = lockData[0];
                                    LockUserEntity lockUserEntity = lockUserEntityService.getDeviceLockUserByPsw(deviceEntity.getId(), String.valueOf(lockId));
                                    if (lockUserEntity != null) {
                                        userName = "「" + lockUserEntity.getUserName() + "」";
                                    }
                                    if (lockData != null && lockData.length >= 3 && lockData[2] == 1) {
                                        switch (lockData[1]) {
                                            case 1:
                                                alarmState = "门锁被" + userName + "管理指纹打开！";
                                                break;
                                            case 2:
                                                alarmState = "门锁被" + userName + "普通指纹打开！";
                                                break;
                                            case 3:
                                                alarmState = "门锁被" + userName + "客人指纹打开！";
                                                break;
                                            case 4:
                                                alarmState = "门锁被" + userName + "挟持指纹打开！";
                                                break;
                                            case 5:
                                                alarmState = "门锁被" + userName + "遥控打开！";
                                                break;
                                            case 7:
                                                alarmState = "门锁被" + userName + "普通密码打开！";
                                                break;
                                            case 8:
                                                alarmState = "门锁被" + userName + "劫持密码打开！";
                                                break;
                                            case 9:
                                                alarmState = "门锁被" + userName + "指纹加密码打开！";
                                                break;
                                            case 10:
                                                alarmState = "门锁被" + userName + "网络打开！";
                                                break;
                                            case 11:
                                                alarmState = "门锁被" + userName + "门卡打开！";
                                                break;
                                            case 12:
                                                alarmState = "门锁被" + userName + "指纹加卡打开！";
                                                break;
                                        }
                                    }
                                }
                                break;
                            case 0x03:
                                if (deviceState.deviceProduct == 0x02) {//燃气警报
                                    specialDevice = true;
                                    if (deviceState.result_data_01 == 1) {
                                        alarmState = "燃气有警报！";
                                    }
                                }
                                break;
                            case 0x04:
                                if (deviceState.deviceProduct == 0x02) {//人体红外
                                    if (deviceState.result_data_01 == 1) {
                                        alarmState = "人体红外感应--有人进入！";
                                    }
                                }
                                break;
                            case 0x05:
                                if (deviceState.deviceProduct == 0x02) {//水浸传感器
                                    specialDevice = true;
                                    if (deviceState.result_data_01 == 1) {
                                        alarmState = "水浸传感--进水了！";
                                    }
                                }
                                break;
                            case 0x07:
                                if (deviceState.deviceProduct == 0x02) {//烟感
                                    specialDevice = true;
                                    if (deviceState.result_data_01 == 0x01) {
                                        alarmState = "烟雾警报了！";
                                    }
                                }
                                break;
                            case (byte) 0x81:
                                if (deviceState.deviceProduct == 0x02) {//门磁-new
                                    if (deviceState.result_data_01 == 1) {
                                        alarmState = "门开了！";
                                    }
                                } else if (deviceState.deviceProduct == 0x03) {//窗磁-new

                                }
                                break;
                            case (byte) 0x83:// 水浸传感器-new
                                if (deviceState.deviceProduct == 0x02) {
                                    if (deviceState.result_data_01 == 1) {
                                        alarmState = "水浸传感--进水了！";
                                    }
                                }
                                break;
                            case (byte) 0x86:// 人体红外感应-new
                                if (deviceState.deviceProduct == 0x02) {
                                    if (deviceState.result_data_01 == 1) {
                                        alarmState = "水浸传感--进水了！";
                                    }
                                }
                                break;
                        }
                    }
                }
            }

            if (!Strings.isNullOrEmpty(alarmState)) {
                // 警报记录
                RoomEntity roomEntity = roomEntityDao.get(deviceEntity.getRoomId());
                // 推送消息
                GatewayEntity gatewayEntity = roomEntity.getFloor().getGateway();
                // 0 撤防, 1布防
                if (gatewayEntity.getSecurityStatus() == 1 || specialDevice) {
                    String roomName = "";
                    if (roomEntity != null) {
                        roomName = roomEntity.getFloor().getName() + roomEntity.getName();
                    }
                    String alarmMsg = roomName + deviceEntity.getName() + " [" + alarmState + "]";

                    AlarmEntity alarmEntity = new AlarmEntity();
                    alarmEntity.setAlarmStatus(AlarmEntity.AlarmStatus);
                    alarmEntity.setCreateTime(new Date());
                    alarmEntity.setDeviceId(deviceEntity.getId());
                    alarmEntity.setMessage(alarmMsg);
                    alarmEntityService.addAlarm(alarmEntity);

                    List<MemberGatewayEntity> memberGatewayEntityList = memberGatewayEntityService.getMemberGatewaysByGateway(gatewayEntity.getId());
                    logger.warn("设备ID : " + deviceEntity.getId() + ",准备发送警报push : " + alarmMsg + ",发送用户数 : " + memberGatewayEntityList.size());
                    for (MemberGatewayEntity memberGatewayEntity : memberGatewayEntityList) {

                        MemberUserEntity memberUserEntity = memberGatewayEntity.getMemberUser();
                        UserEntity userEntity = memberUserEntity.getUserEntity();
                        new Push().toUser(userEntity.getId()).newAlarm(deviceEntity.getId(), alarmMsg).send();
                        logger.warn("设备ID : " + deviceEntity.getId() + ",成功发送警报push到用户 : " + memberUserEntity.getUserName());

                        if (GlobalConfiguration.SMS_ON.equals(GlobalConfiguration.getSMSAlert()) && memberUserEntity.getUserChannel() == MemberUserEntity.USER_CHANNEL_NORMAL) {
                            try {
                                // 发送短信
                                int smsCount = userEntity.getSmsCount();
                                if (smsCount > 0) {
                                    String tpl_value = URLEncoder.encode("#content#", YunPianSMSUtil.ENCODING) + "=" + URLEncoder.encode(alarmMsg, YunPianSMSUtil.ENCODING);
                                    String sendResult = YunPianSMSUtil.tplSendSms(GlobalConfiguration.getYunpianTplAlarm(), tpl_value, memberUserEntity.getUserName());
                                    logger.warn("警报短信发送成功 : " + sendResult);

                                    // 扣除短信数量
                                    userEntity.setSmsCount(smsCount - 1);
                                    userEntityDao.update(userEntity);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDeciceSequence(int roomId, int deviceId, int sequence) {
        deviceEntityDao.updateDeciceSequence(roomId, deviceId, sequence);
    }
}
