package in.srain.cube.image.impl;

import in.srain.cube.util.CLog;
import in.srain.cube.util.Debug;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

import android.os.Build;
import android.os.SystemClock;

/**
 * A simple class that fetches images from a URL.
 */
public class SimpleDownloader {

    protected static final String LOG_TAG = Debug.DEBUG_IMAGE_LOG_TAG_PROVIDER;

    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private static final int CONNECTION_TIME_OUT = 5000;

    private static DownloaderListener downloaderListener;

    public static DownloaderListener getDownloaderListener() {
        return downloaderListener;
    }

    public static void setDownloaderListener(DownloaderListener downloaderListener) {
        SimpleDownloader.downloaderListener = downloaderListener;
    }

    /**
     * Download a bitmap from a URL and write the content to an output stream.
     * 
     * @param urlString The URL to fetch
     * @return true if successful, false otherwise
     */
    public static boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        long reqTime = System.currentTimeMillis();
        String serverIp = null;
        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        Integer serverCode = null;
        Integer clientCode = null;
        String msg = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            try {
                String host = url.getHost();
                InetAddress address = InetAddress.getByName(host);
                serverIp = address.getHostAddress();
            } catch (Exception e) {
                CLog.e(LOG_TAG, "Error in get host address - " + e);
            }
            serverCode = urlConnection.getResponseCode();
            if (serverCode != 200) {
                CLog.e(LOG_TAG, "Error in downloadBitmap -  " + serverCode);
                if (null != downloaderListener) {
                    downloaderListener.onDownloaderComplete(serverIp, reqTime, urlString, null,
                            (int) (System.currentTimeMillis() - reqTime), clientCode, serverCode, msg, true);
                }
                return false;
            }
            int len = urlConnection.getContentLength();
            int total = 0;
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                total++;
                out.write(b);
            }
            if (null != downloaderListener) {
                downloaderListener.onDownloaderComplete(serverIp, reqTime, urlString, len, (int) (System.currentTimeMillis() - reqTime),
                        clientCode, serverCode, msg, false);
            }
            return true;
        } catch (final IOException e) {
            clientCode = -1;
            msg = e.getMessage();
            CLog.e(LOG_TAG, "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        if (null != downloaderListener) {
            downloaderListener.onDownloaderComplete(serverIp, reqTime, urlString, null, (int) (System.currentTimeMillis() - reqTime),
                    clientCode, serverCode, msg, true);
        }
        return false;
    }

    /**
     * Workaround for bug pre-Froyo, see here for more info: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public interface DownloaderListener {
        public abstract void onDownloaderComplete(String serverIp, long reqTime, String imgUrl, Integer imgSize, Integer downloadCost,
                Integer clientCode, Integer serverCode, String msg, boolean mustStat);
    }
}
