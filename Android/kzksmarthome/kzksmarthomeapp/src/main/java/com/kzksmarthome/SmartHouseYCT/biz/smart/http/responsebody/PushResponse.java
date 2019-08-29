package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

/**
 * @Title: PushResponse
 * @Description: 推送消息
 * @author laixj
 * @date 2016/11/9 21:43
 * @version V1.0
 */
public class PushResponse {
    /**
     * message : 门锁被打开
     * title : 酷庭科技
     * action : 1
     * to : xxxxxx
     * category : 3
     * userType : 1
     * type :
     */

    private String message;
    private String title;
    private String action;
    private String to;
    private String category;
    private String userType;
    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PushResponse{" +
                "message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", action='" + action + '\'' +
                ", to='" + to + '\'' +
                ", category='" + category + '\'' +
                ", userType='" + userType + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
