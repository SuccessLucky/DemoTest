package com.project.utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.project.bean.PermissionsBean;
import com.project.entity.MemberGatewayEntity;

import java.util.List;
import java.util.UUID;

/**
 * Created by xieyanhao on 16/3/13.
 */
public class UserUtils {

    public static String generateActivationCode() {
        return UUID.randomUUID().toString();
    }

    public static String generateUserUID() {
        String randReferId = StringUtil.getCharAndNumr(5);
        return randReferId;
    }

    public static PermissionsBean getUserPermission(MemberGatewayEntity memberGateway) {

        PermissionsBean permissionsBean = new PermissionsBean();
        if (memberGateway == null) {
            return permissionsBean;
        }
        String permissions = memberGateway.getPermissions();
        if (!Strings.isNullOrEmpty(permissions)) {
            Gson gson = new Gson();
            permissionsBean = gson.fromJson(permissions, PermissionsBean.class);
        }

        return permissionsBean;
    }

    public static boolean isHavePermission(MemberGatewayEntity memberGateway, int matchId, int type) {
        if (memberGateway.getMemberType() == MemberGatewayEntity.MEMBER_TYPY_ADMIN) {
            return true;
        }
        boolean result = false;
        PermissionsBean permissionsBean = getUserPermission(memberGateway);

        List<Integer> scenes = permissionsBean.getScenes();
        List<Integer> devices = permissionsBean.getDevices();

        if (type == MemberGatewayEntity.PERMISSION_SCENE) {
            result = scenes.contains(matchId);
        } else if (type == MemberGatewayEntity.PERMISSION_DEVICE) {
            result = devices.contains(matchId);
        }

        return result;
    }

}
