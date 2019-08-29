package com.kzksmarthome.common.lib.okhttp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.kzksmarthome.common.lib.util.GsonHelper;
import com.google.gson.Gson;

public class ResponseParam<T> {

    public static final int FAILED_FROM_REMOTE_CODE = -1;
    public static final int FAILED_FROM_CACHE_CODE = -2;
    public static final int SUCCESS_FROM_REMOTE_CODE = 0;
    public static final int SUCCESS_FROM_CACHE_CODE = 1;

    public ResponseHeader header;
    public T body;
    
    public static ResponseParam fromJson(String json, Class clazz) {
        Gson gson = GsonHelper.getGson();
        Type objectType = type(ResponseParam.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public String toJson(Class<T> clazz) {
        Gson gson = GsonHelper.getGson();
        Type objectType = type(ResponseParam.class, clazz);
        return gson.toJson(this, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
