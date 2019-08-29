package com.kzksmarthome.SmartHouseYCT.biz.smart.weather;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfWeather;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import in.srain.cube.util.CLog;

/**
 * 定位工具类
 * Created by jack on 2016/9/24.
 */
public class LocationUtil {
    private static final String TAG = "LocationUtil";
    private static LocationUtil mLocationUtil;
    public LocationService locationService;
    /**
     * 纬度
     */
    public double mLatitude;
    /**
     * 经度
     */
    public double mLongitude;

    private LocationUtil() {
    }

    public static LocationUtil getInstance() {
        if (mLocationUtil == null) {
            mLocationUtil = new LocationUtil();
        }
        return mLocationUtil;
    }

    public void init() {
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(SmartHomeApp.getInstance());
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                CLog.i("laixj", "定位返回latitude="+location.getLatitude()+";longitude="+location.getLongitude()+";province="+location.getProvince()+";city="+location.getCity());
                if(!TextUtils.isEmpty(location.getProvince()) && !TextUtils.isEmpty(location.getCity())){
                    locationService.stop();
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();
                    getWeatherData(location.getProvince(),location.getCity());
                }
            }
        }

    };
    /**
     * 获取天气数据
     */
    public void getWeatherData(String province, String city) {
        WeatherRequest weatherRequest = new WeatherRequest();
        weatherRequest.getWeatherData(province,city,weatherCallback);
    }


    Callback weatherCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            EventOfWeather eventOfWeather = new EventOfWeather();
            eventOfWeather.responseStr = null;
            GjjEventBus.getInstance().post(eventOfWeather);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (response != null) {
                String responseStr = response.body().string();
                EventOfWeather eventOfWeather = new EventOfWeather();
                eventOfWeather.responseStr = responseStr;
                GjjEventBus.getInstance().post(eventOfWeather);
            }
        }
    };
}
