package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: GetGwListResponse
 * @Description: 获取网关列表返回
 * @author laixj
 * @date 2016/10/10 20:22
 * @version V1.0
 */
public class GetGwListResponse extends BaseResponse {
    /**
     * success : true
     * result : {"user_gateways":[{"gateway_id":1,"mac_address":"f4:5c:89:c4:12:8d","member_type":1,"gateway_name":"我家"},{"gateway_id":2,"mac_address":"f4:5c:89:c4:12:9d","member_type":1,"gateway_name":"公司"}]}
     * error : {"code":"","message":""}
     */

    private boolean success;
    private ResultBean result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * gateway_id : 1
         * mac_address : f4:5c:89:c4:12:8d
         * member_type : 1
         * gateway_name : 我家
         */

        private List<GatewayInfo> user_gateways;

        public List<GatewayInfo> getUser_gateways() {
            return user_gateways;
        }

        public void setUser_gateways(List<GatewayInfo> user_gateways) {
            this.user_gateways = user_gateways;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "user_gateways=" + user_gateways +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetGwListResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
