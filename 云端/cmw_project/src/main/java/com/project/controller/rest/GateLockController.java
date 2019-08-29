package com.project.controller.rest;

import com.google.common.base.Strings;
import com.project.bean.BaseResultBean;
import com.project.bean.LockUserBean;
import com.project.bean.LockUserBean.UnlockPswBean;
import com.project.common.config.ApiStatusCode;
import com.project.entity.DeviceEntity;
import com.project.entity.LockUserEntity;
import com.project.entity.MemberGatewayEntity;
import com.project.entity.UnlockPswEntity;
import com.project.service.DeviceEntityService;
import com.project.service.LockUserEntityService;
import com.project.service.UnlockPswEntityService;
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
 * Created by xieyanhao on 16/9/24.
 */
@Controller
@RequestMapping("/rest")
public class GateLockController extends BaseController {

    @Autowired
    private LockUserEntityService lockUserEntityService;

    @Autowired
    private UnlockPswEntityService unlockPswEntityService;

    @Autowired
    private DeviceEntityService deviceEntityService;

    @RequestMapping(value = "/v1/gate_lock/add/fingerprint", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> addGateLockUser(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        Integer deviceId = (Integer) param.get("device_id");
        String userName = (String) param.get("user_name");
        String fingerprintId = (String) param.get("fingerprint_id");

        if (deviceId == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "设备信息有误");
        }
        if (Strings.isNullOrEmpty(fingerprintId)) {
            return falseResult(ApiStatusCode.NOT_NULL, "指纹编码不能为空");
        }

        try {

            DeviceEntity deviceEntity = deviceEntityService.getDeviceById(deviceId);
            if (deviceEntity == null) {
                falseResult("", "设备信息不存在");
            } else {

                LockUserEntity lockUserEntity = new LockUserEntity();
                lockUserEntity.setDeviceId(deviceEntity.getId());
                lockUserEntity.setFingerprintId(fingerprintId);
                lockUserEntity.setUserName(userName);
                lockUserEntityService.addLockUser(lockUserEntity);

                LockUserBean lockUserBean = generateLockUserBean(lockUserEntity);
                trueResult(lockUserBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gate_lock/add/psw", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> addGateLockPsw(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        Integer deviceId = (Integer) param.get("device_id");
        String unlockPsw = (String) param.get("unlock_psw");

        if (deviceId == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "设备信息有误");
        }
        if (Strings.isNullOrEmpty(unlockPsw)) {
            return falseResult(ApiStatusCode.NOT_NULL, "密码不能为空");
        }

        try {

            DeviceEntity deviceEntity = deviceEntityService.getDeviceById(deviceId);
            if (deviceEntity == null) {
                falseResult("", "设备信息不存在");
            } else {

                List<UnlockPswEntity> unlockPswEntities = unlockPswEntityService.getDeviceUnlockPsws(deviceId);
                if (unlockPswEntities.size() > 0) {
                    return falseResult(ApiStatusCode.NOT_NULL, "已设置锁密码,无需重复添加");
                }
                UnlockPswEntity unlockPswEntity = new UnlockPswEntity();
                unlockPswEntity.setDeviceId(deviceEntity.getId());
                unlockPswEntity.setUnlockPsw(unlockPsw);

                unlockPswEntityService.addUnlockPsw(unlockPswEntity);
                trueResult("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gate_lock/unlock/psw", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> unlockByPsw(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        Integer deviceId = (Integer) param.get("device_id");
        String unlockPsw = (String) param.get("unlock_psw");

        if (deviceId == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "设备信息有误");
        }
        if (Strings.isNullOrEmpty(unlockPsw)) {
            return falseResult(ApiStatusCode.NOT_NULL, "密码不能为空");
        }

        try {

            UnlockPswEntity unlockPswEntity = unlockPswEntityService.getDeviceUnlockPswByPsw(deviceId, unlockPsw);
            if (unlockPswEntity == null) {
                falseResult(ApiStatusCode.SERVER_EXCEPTION, "密码错误,无法打开门锁");
            } else {
                unlockPswEntity.setUnlockTimes(unlockPswEntity.getUnlockTimes() + 1);
                unlockPswEntityService.updateUnlockPsw(unlockPswEntity);
                trueResult("成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gate_lock/list/psw", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getDeviceUnlockPsw() {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        String deviceId = request.getParameter("device_id");
        if (Strings.isNullOrEmpty(deviceId)) {
            return falseResult(ApiStatusCode.NOT_NULL, "设备不存在");
        }

        try {

            List<UnlockPswEntity> unlockPswEntities = unlockPswEntityService.getDeviceUnlockPsws(Integer.parseInt(deviceId));

            List<UnlockPswBean> unlockPswBeanList = new ArrayList<>();
            for (UnlockPswEntity unlockPswEntity : unlockPswEntities) {
                UnlockPswBean unlockPswBean = new UnlockPswBean();

                unlockPswBean.setDevice_id(unlockPswEntity.getDeviceId());
                unlockPswBean.setLock_id(unlockPswEntity.getId());
                unlockPswBean.setUnlock_psw(unlockPswEntity.getUnlockPsw());
                unlockPswBean.setUnlock_times(unlockPswEntity.getUnlockTimes());

                unlockPswBeanList.add(unlockPswBean);
            }

            return trueResult(unlockPswBeanList);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }


    @RequestMapping(value = "/v1/gate_lock/unlock/fingerprint", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> unlockByFingerprint(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        Integer deviceId = (Integer) param.get("device_id");
        String fingerprintId = (String) param.get("fingerprint_id");

        if (deviceId == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "设备信息有误");
        }
        if (Strings.isNullOrEmpty(fingerprintId)) {
            return falseResult(ApiStatusCode.NOT_NULL, "指纹编码不能为空");
        }

        try {

            LockUserEntity lockUserEntity = lockUserEntityService.getDeviceLockUserByPsw(deviceId, fingerprintId);
            if (lockUserEntity == null) {
                falseResult(ApiStatusCode.SERVER_EXCEPTION, "密码错误,无法打开门锁");
            } else {
                lockUserEntity.setUnlockTimes(lockUserEntity.getUnlockTimes() + 1);
                lockUserEntityService.updateLockUser(lockUserEntity);
                trueResult("成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gate_lock/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResultBean<Object> deleteByFingerprint(@RequestBody ModelMap param) {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        if (param == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        Integer lockId = (Integer) param.get("lock_id");
        String lockType = (String) param.get("lock_type");

        if (Strings.isNullOrEmpty(lockType) || lockId == null) {
            return falseResult(ApiStatusCode.NOT_NULL, "信息有误,删除失败");
        }

        try {

            if ("fingerprint".equals(lockType)) {
                // 删除指纹
                LockUserEntity lockUserEntity = lockUserEntityService.getLockUserById(lockId);
                if (lockUserEntity == null) {
                    return falseResult("", "门锁信息不存在");
                } else {
                    lockUserEntityService.deleteLockUser(lockUserEntity);
                    return trueResult("删除成功");
                }

            } else {
                // 删除密码
                UnlockPswEntity unlockPswEntity = unlockPswEntityService.getDeviceUnlockPswByid(lockId);
                if (unlockPswEntity == null) {
                    return falseResult("", "门锁信息不存在");
                } else {
                    unlockPswEntityService.deleteUnlockPsw(unlockPswEntity);
                    return trueResult("删除成功");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    @RequestMapping(value = "/v1/gate_lock/list/lock_user", method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean<Object> getByRoomId() {

        MemberGatewayEntity memberGateway = getMemberGateway();
        if (memberGateway == null) {
            return responseResult;
        }

        String deviceId = request.getParameter("device_id");
        String macAddress = request.getParameter("mac_address");

        if (Strings.isNullOrEmpty(deviceId) && Strings.isNullOrEmpty(macAddress)) {
            return falseResult(ApiStatusCode.NOT_NULL, "请求参数不能为空");
        }

        try {

            List<LockUserEntity> lockUserEntities = new ArrayList<>();
            if (Strings.isNullOrEmpty(macAddress)) {
                lockUserEntities = lockUserEntityService.getDeviceLockUsers(Integer.parseInt(deviceId));
            } else {
                List<DeviceEntity> deviceEntity = deviceEntityService.getDeviceByMacAddress(macAddress, memberGateway.getGateway().getId());
                if (deviceEntity.size() > 0) {
                    lockUserEntities = lockUserEntityService.getDeviceLockUsers(deviceEntity.get(0).getId());
                }
            }

            List<LockUserBean> lockUserBeanList = new ArrayList<>();
            for (LockUserEntity lockUserEntity : lockUserEntities) {
                LockUserBean lockUserBean = generateLockUserBean(lockUserEntity);
                lockUserBeanList.add(lockUserBean);
            }
            return trueResult(lockUserBeanList);
        } catch (Exception e) {
            e.printStackTrace();
            falseResult(ApiStatusCode.SERVER_EXCEPTION, "发生异常");
        }

        return responseResult;
    }

    public LockUserBean generateLockUserBean(LockUserEntity lockUserEntity) {
        LockUserBean lockUserBean = new LockUserBean();
        lockUserBean.setLock_id(lockUserEntity.getId());
        lockUserBean.setUnlock_times(lockUserEntity.getUnlockTimes());
        lockUserBean.setDevice_id(lockUserEntity.getDeviceId());
        lockUserBean.setUser_name(lockUserEntity.getUserName());
        lockUserBean.setFingerprintId(lockUserEntity.getFingerprintId());
        return lockUserBean;
    }
}
