package com.kzksmarthome.common.lib.okhttp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.kzksmarthome.common.lib.util.GsonHelper;
import com.google.gson.Gson;

public class RequestParam<T> {
    public RequestHeader header;
    public T body;
    
    public static RequestParam fromJson(String json, Class... clazz) {
        Gson gson = GsonHelper.getGson();
        Type objectType = type(RequestParam.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public String toJson(Class<T> clazz) {
        Gson gson = GsonHelper.getGson();
        Type objectType = type(RequestParam.class, clazz);
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
