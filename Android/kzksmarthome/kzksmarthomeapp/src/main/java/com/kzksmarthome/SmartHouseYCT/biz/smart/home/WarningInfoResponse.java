package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * 报警返回数据
 * Created by jack on 2016/10/29.
 */

public class WarningInfoResponse extends BaseResponse {

    /**
     * success : true
     * result : [{"alarm_id":1,"create_date":"2016-10-27 20:07:19","alarm_msg":"第三层书房窗帘警报"},{"alarm_id":2,"create_date":"2016-10-27 21:07:19","alarm_msg":"第三层卫生间马桶警报"},{"alarm_id":3,"create_date":"2016-10-27 22:07:19","alarm_msg":"第一层客厅电视警报"}]
     */

    private boolean success;
    /**
     * alarm_id : 1
     * create_date : 2016-10-27 20:07:19
     * alarm_msg : 第三层书房窗帘警报
     */

    private List<WarningInfoBean> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<WarningInfoBean> getResult() {
        return result;
    }

    public void setResult(List<WarningInfoBean> result) {
        this.result = result;
    }

}
