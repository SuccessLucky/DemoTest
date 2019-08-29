package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * 获取报警信息参数
 * Created by jack on 2016/10/29.
 */

public class WarningInfoRequestParam extends BaseModel{
    private String date;

    public String getDateStr() {
        return date;
    }

    public void setDateStr(String date) {
        this.date = date;
    }
}
