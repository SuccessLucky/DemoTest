package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.RoomInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: GetRoomListResponse
 * @Description: 根据楼层id获取房间列表返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class GetRoomListResponse extends BaseResponse {
    /**
     * success : true
     * result : [{"id":1,"name":"测试天天","image":"0002","floor_id":2},{"id":2,"name":"大明湖畔","image":"0001","floor_id":2}]
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * id : 1
     * name : 测试天天
     * image : 0002
     * floor_id : 2
     */

    private List<RoomInfo> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<RoomInfo> getResult() {
        return result;
    }

    public void setResult(List<RoomInfo> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GetRoomListResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }
}
