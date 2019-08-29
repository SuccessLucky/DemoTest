package com.kzksmarthome.SmartHouseYCT.biz.h5;

import org.json.JSONException;
import org.json.JSONObject;

import com.kzksmarthome.common.module.log.L;

public class ParameterObject {
//    public final static String CLZ = "clz";
    public final static String ARGS = "args";
    public final static String METHOD = "method";
//    private String clz;
    private JSONObject args;
    private String method;

//    public String getClassName() {
//        return clz;
//    }

    public String getMethodName() {
        return method;
    }

    public JSONObject getArgs() {
        return args;
    }

    public ParameterObject(JSONObject json) {
        try {
//            clz = json.get(CLZ).toString();
            method = json.get(METHOD).toString();

            if (json.has(ARGS)) {
                args = json.getJSONObject(ARGS);
            }

        } catch (JSONException e) {
            L.e(e);
        }
    }
}
