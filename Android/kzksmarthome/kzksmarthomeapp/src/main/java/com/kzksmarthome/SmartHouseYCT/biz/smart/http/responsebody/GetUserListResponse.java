package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FamilyMemberInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @author laixj
 * @version V1.0
 * @Title: GetUserListResponse
 * @Description: 获取家庭成员列表返回
 * @date 2016/10/10 20:40
 */
public class GetUserListResponse extends BaseResponse {
    /**
     * success : true
     * result : [{"member_id":1,"member_name":"xiexie","member_type":1,"image":null},{"member_id":2,"member_name":"xiexie","member_type":2,"image":null}]
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * member_id : 1
     * member_name : xiexie
     * member_type : 1
     * image : null
     */

    private List<FamilyMemberInfo> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<FamilyMemberInfo> getResult() {
        return result;
    }

    public void setResult(List<FamilyMemberInfo> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GetUserListResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
