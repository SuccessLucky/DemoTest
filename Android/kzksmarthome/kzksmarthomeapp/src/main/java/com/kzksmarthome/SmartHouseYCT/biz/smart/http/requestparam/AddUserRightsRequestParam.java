package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

import java.util.List;

/**
 * @Title: AddUserRightsRequestParam
 * @Description: 增加子账户权限请求参数
 * @author laixj
 * @date 2016/10/22 8:33
 * @version V1.0
 */
public class AddUserRightsRequestParam {
    /**
     * member_id : 2
     * scenes : [1,2,3,4]
     * devices : [1,2,3,4]
     */

    private int member_id;
    private List<Integer> scenes;
    private List<Integer> devices;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public List<Integer> getScenes() {
        return scenes;
    }

    public void setScenes(List<Integer> scenes) {
        this.scenes = scenes;
    }

    public List<Integer> getDevices() {
        return devices;
    }

    public void setDevices(List<Integer> devices) {
        this.devices = devices;
    }
}
