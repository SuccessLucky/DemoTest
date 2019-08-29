package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: GetGatewayInfoResponse
 * @Description: 获取当前网关信息返回
 * @author laixj
 * @date 2016/10/31 22:45
 * @version V1.0
 */
public class GetGatewayInfoResponse extends BaseResponse {
    /**
     * success : true
     * result : {"gateway_id":1,"mac_address":"f4:5c:89:c4:12:8d","wifi_mac_address":"hsahdkjahksj","security_status":1,"member_type":1,"gateway_name":"我爱我家"}
     */

    private boolean success;
    /**
     * gateway_id : 1
     * mac_address : f4:5c:89:c4:12:8d
     * wifi_mac_address : hsahdkjahksj
     * security_status : 1
     * member_type : 1
     * gateway_name : 我爱我家
     */

    private GatewayInfo result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public GatewayInfo getResult() {
        return result;
    }

    public void setResult(GatewayInfo result) {
        this.result = result;
    }
}
