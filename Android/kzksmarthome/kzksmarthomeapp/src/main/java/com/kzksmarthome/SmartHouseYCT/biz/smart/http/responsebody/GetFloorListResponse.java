package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: GetFloorListResponse
 * @Description: 获取楼层列表返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class GetFloorListResponse extends BaseResponse {
    /**
     * success : true
     * result : [{"id":2,"name":"第2层","image":"0002"},{"id":3,"name":"第3层","image":"0003"}]
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * id : 2
     * name : 第2层
     * image : 0002
     */

    private List<FloorInfo> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<FloorInfo> getResult() {
        return result;
    }

    public void setResult(List<FloorInfo> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GetFloorListResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }
}
