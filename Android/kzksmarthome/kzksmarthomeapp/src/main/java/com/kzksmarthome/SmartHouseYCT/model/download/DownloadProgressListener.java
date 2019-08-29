package com.kzksmarthome.SmartHouseYCT.model.download;

public interface DownloadProgressListener {
    public static final int ERROR_CODE_NETWORK_UNAVAILABLE = -1;
    public static final int ERROR_CODE_RETRY_FAIL = -2;
    public static final int ERROR_CODE_HTTP_404 = 404;
    public static final int ERROR_CODE_HTTP_500 = 500;
    public static final int ERROR_CODE_HTTP_502 = 502;
    public void onComplete(String remoteUrl, String localUrl);
    public void onError(String remoteUrl, int errorCode);
    public void onProgressUpdate(long totalSize, long downloadedSize);
}
