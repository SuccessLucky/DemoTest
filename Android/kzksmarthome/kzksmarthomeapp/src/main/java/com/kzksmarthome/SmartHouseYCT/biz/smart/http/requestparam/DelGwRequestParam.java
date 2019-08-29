package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: DelFloorRequestParam
 * @Description: 删除网关请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class DelGwRequestParam {
    String gateway_mac;

    public String getGateway_mac() {
        return gateway_mac;
    }

    public void setGateway_mac(String gateway_mac) {
        this.gateway_mac = gateway_mac;
    }
}
