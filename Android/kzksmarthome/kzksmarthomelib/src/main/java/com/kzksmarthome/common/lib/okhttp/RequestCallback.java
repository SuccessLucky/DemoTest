package com.kzksmarthome.common.lib.okhttp;

import com.squareup.okhttp.Request;

/**
 * Created by Kop on 2015/7/21.
 */
public interface RequestCallback {
    void onFailure(Request request, String url,  Exception e);

    void onBizSuccess(ResponseParam response, String url, int from);

    void onBizFail(ResponseParam response, String url, int from);
}
