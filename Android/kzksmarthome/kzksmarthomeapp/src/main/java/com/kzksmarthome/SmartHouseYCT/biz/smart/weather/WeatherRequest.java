package com.kzksmarthome.SmartHouseYCT.biz.smart.weather;

import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * 获取天气预报数据
 * Created by jack on 2016/9/24.
 */
public class WeatherRequest {
    /**
     * 获取天气数据
     *
     * @return
     */
    public void getWeatherData(final String province, final String city,final Callback callback) {
        BackgroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                httpRequest(province,city,callback);
            }
        });

    }

    /**
     * 执行网络请求
     * @param callback
     */
    private void httpRequest(String province, String city,Callback callback) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //请求地址：http://v.juhe.cn/weather/geo 请求参数：lon=116.39277&lat=39.933748&format=&dtype=&key=031edbf392806f8308cb332365ef5c29
        StringBuilder builder = new StringBuilder();
        builder.append("http://apicloud.mob.com/v1/weather/query");
        builder.append("?key=");
        builder.append("26864c7ba4dd5");
        builder.append("&city=");
        builder.append(city.replaceAll("市",""));
        builder.append("&province=");
        builder.append(province);
        //创建一个Request
        final Request request = new Request.Builder()
                .url(builder.toString())
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(callback);
    }

}
