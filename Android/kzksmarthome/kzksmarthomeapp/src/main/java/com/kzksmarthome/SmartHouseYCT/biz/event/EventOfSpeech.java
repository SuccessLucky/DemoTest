package com.kzksmarthome.SmartHouseYCT.biz.event;

/**
 * 讯飞语音通话处理
 * @author lizhid
 * @version V1.0
 * @description:
 * @date 2017/6/2
 */
public class EventOfSpeech {
    /**
     * 说话内容
     */
    private String speechStr;

    public String getSpeechStr() {
        return speechStr;
    }

    public void setSpeechStr(String speechStr) {
        this.speechStr = speechStr;
    }
}
