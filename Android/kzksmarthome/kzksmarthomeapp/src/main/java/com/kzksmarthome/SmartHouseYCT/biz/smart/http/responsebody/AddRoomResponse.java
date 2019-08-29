package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.RoomInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: AddRoomResponse
 * @Description: 添加房间返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class AddRoomResponse extends BaseResponse {
    /**
     * success : true
     * result : null
     * error : {"code":"","message":""}
     */

    private boolean success;
    private RoomInfo result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public RoomInfo getResult() {
        return result;
    }

    public void setResult(RoomInfo result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AddRoomResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }
}
