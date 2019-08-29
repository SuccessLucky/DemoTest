package com.kzksmarthome.SmartHouseYCT.biz.event;

import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.ColorLightOrderModel;

/**
 * Created by lizhi on 2017/6/3.
 */

public class EventOfColorLight {
    /**
     * 多彩灯命令数据
     */
    private ColorLightOrderModel colorLightOrderModel;

    public ColorLightOrderModel getColorLightOrderModel() {
        return colorLightOrderModel;
    }

    public void setColorLightOrderModel(ColorLightOrderModel colorLightOrderModel) {
        this.colorLightOrderModel = colorLightOrderModel;
    }
}
