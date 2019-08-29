package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: UpdateGwSecurityResponse
 * @Description: 修改网关安防状态返回
 * @author laixj
 * @date 2016/10/31 22:35
 * @version V1.0
 */
public class UpdateGwSecurityResponse extends BaseResponse {
    /**
     * success : true
     * result : success
     */

    private boolean success;
    private String result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
