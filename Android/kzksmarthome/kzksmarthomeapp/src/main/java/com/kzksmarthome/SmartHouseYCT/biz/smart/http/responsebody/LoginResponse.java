package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: LoginResponse
 * @Description: 登陆返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class LoginResponse extends BaseResponse {
    /**
     * success : true
     * result : {"token":"a5c52d8e-c7fb-43a3-ab90-9ebd24ca326f","user_gateways":[{"gateway_id":1,"mac_address":"f4:5c:89:c4:12:8d","member_type":1,"gateway_name":"我爱我家"},{"gateway_id":2,"mac_address":"f4:5c:89:c4:12:9d","member_type":1,"gateway_name":"我爱我家"}]}
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * token : a5c52d8e-c7fb-43a3-ab90-9ebd24ca326f
     * user_gateways : [{"gateway_id":1,"mac_address":"f4:5c:89:c4:12:8d","member_type":1,"gateway_name":"我爱我家"},{"gateway_id":2,"mac_address":"f4:5c:89:c4:12:9d","member_type":1,"gateway_name":"我爱我家"}]
     */

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
        private String token;

        private List<GatewayInfo> user_gateways;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<GatewayInfo> getUser_gateways() {
            return user_gateways;
        }

        public void setUser_gateways(List<GatewayInfo> user_gateways) {
            this.user_gateways = user_gateways;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "token='" + token + '\'' +
                    ", user_gateways=" + user_gateways +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
