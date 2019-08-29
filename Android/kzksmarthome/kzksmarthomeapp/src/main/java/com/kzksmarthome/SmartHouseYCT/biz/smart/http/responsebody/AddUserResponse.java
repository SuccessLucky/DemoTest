package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FamilyMemberInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: AddUserResponse
 * @Description: 添加家庭成员返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class AddUserResponse extends BaseResponse {
    /**
     * success : true
     * result : {"member_id":2,"member_name":"xiexie","member_type":2,"image":"xx.png"}
     */

    private boolean success;
    private FamilyMemberInfo result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public FamilyMemberInfo getResult() {
        return result;
    }

    public void setResult(FamilyMemberInfo result) {
        this.result = result;
    }
}
