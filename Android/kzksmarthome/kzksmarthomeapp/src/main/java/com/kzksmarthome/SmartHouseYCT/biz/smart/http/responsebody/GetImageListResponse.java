package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: GetImageListResponse
 * @Description: 获取图片列表返回
 * @author laixj
 * @date 2016/10/10 20:55
 * @version V1.0
 */
public class GetImageListResponse extends BaseResponse {
    /**
     * success : true
     * result : {"images":[{"id":1,"base_url":"http://localhost:8080/staticFile/imgs/room/","name":"room000","image_type":"png"},{"id":2,"base_url":"http://localhost:8080/staticFile/imgs/room/","name":"room001","image_type":"png"},{"id":3,"base_url":"http://localhost:8080/staticFile/imgs/room/","name":"room002","image_type":"png"}]}
     * error : {"code":"","message":""}
     */

    private boolean success;
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
        /**
         * id : 1
         * base_url : http://localhost:8080/staticFile/imgs/room/
         * name : room000
         * image_type : png
         */

        private List<ImageInfo> images;

        public List<ImageInfo> getImages() {
            return images;
        }

        public void setImages(List<ImageInfo> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "images=" + images +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetImageListResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
