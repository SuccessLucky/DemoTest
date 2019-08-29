package com.kzksmarthome.common.module.download;

public interface DownloadProgressListener {
    public static final int ERROR_CODE_UNKNOWN = -1;
    public static final int ERROR_CODE_NETWORK_UNAVAILABLE = -2;
    public static final int ERROR_CODE_RETRY_FAIL = -3;
    public static final int ERROR_CODE_HTTP_404 = 404;
    public static final int ERROR_CODE_HTTP_500 = 500;
    public static final int ERROR_CODE_HTTP_502 = 502;
    public void onComplete(String remoteUrl, String localUrl);
    public void onError(String remoteUrl, int errorCode);
    public void onProgressUpdate(long totalSize, long downloadedSize);
}
