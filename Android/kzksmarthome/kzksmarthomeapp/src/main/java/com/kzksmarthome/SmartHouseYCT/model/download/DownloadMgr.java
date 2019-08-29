package com.kzksmarthome.SmartHouseYCT.model.download;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.FileObserver;
import android.text.TextUtils;

import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.common.lib.network.NetworkState;
import com.kzksmarthome.common.lib.network.NetworkStateMgr;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;


public class DownloadMgr {

    private final static int MAX_THREAD_COUNT = 3;
    private final static int MAX_RETRY_COUNT = 3;

    private static DownloadMgr mInstance;

    private ExecutorService mThreadService;
    private final Map<Integer, ArrayList<DownloadProgressListener>> mDownloadingImageMap = new ConcurrentHashMap<Integer, ArrayList<DownloadProgressListener>>();
    private final Map<Integer, Boolean> mDownloadedImageMap = new ConcurrentHashMap<Integer, Boolean>();

    public synchronized static DownloadMgr getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadMgr();
        }
        return mInstance;
    }

    private DownloadMgr() {
        init();
    }
    
    private void init() {
        mThreadService = Executors.newFixedThreadPool(MAX_THREAD_COUNT);

        mThreadService.execute(new Runnable() {

            @Override
            public void run() {
                FilenameFilter filenameFilter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return !name.endsWith(".tmp");
                    }
                };
                final File downloadDir = FileUtil.getDownloadFilesDir(SmartHomeApp.getInstance());

                try {
                    String[] fileNames = downloadDir.list(filenameFilter);
                    String dir = downloadDir.getAbsolutePath();

                    for (int i = 0; i < fileNames.length; ++i) {
                        mDownloadedImageMap.put((dir + File.separator + fileNames[i]).hashCode(), true);
                    }

                } catch (Exception e) {
                    L.w(e);
                }
                
                if (downloadDir != null) {
                    try {
                        FileObserver watcher = new FileObserver(downloadDir.getAbsolutePath(),
                                FileObserver.DELETE | FileObserver.DELETE_SELF) {

                            @Override
                            public void onEvent(int event, String path) {
                                L.d("DownloadMgr# onEvent event: " + event + " path: " + path);
                                if (event == DELETE) {
                                    mDownloadedImageMap.remove((downloadDir.getAbsolutePath() + File.separator + path).hashCode());
                                } else if (event == DELETE_SELF) {
                                    mDownloadedImageMap.clear();
                                    stopWatching();
                                    FileUtil.getDownloadFilesDir(SmartHomeApp.getInstance());
                                    startWatching();
                                }
                            }

                        };
                        watcher.startWatching();

                    } catch (Exception e) {
                        L.w(e);
                    }
                }
            }
            
        });
        
    }

    public void downloadImageFile(final DownloadProgressListener listener, final String remoteUrl, final String localUrl) {
        if (TextUtils.isEmpty(remoteUrl)) {
            return;
        }

        mThreadService.execute(new Runnable() {
            @Override
            public void run() {
                final int key = remoteUrl.hashCode();
                String lUrl = localUrl;
                if (TextUtils.isEmpty(lUrl)) {
                    lUrl = FileUtil.getDownloadFilesDir(SmartHomeApp.getInstance()).getAbsolutePath()
                            + File.separator + key;
                }
                //已经下载到本地，回调下载成功
                if (mDownloadedImageMap.containsKey(key)) {
                    L.d("DownloadMgr# 已经下载到本地，回调下载成功");
                    listener.onComplete(remoteUrl, lUrl);
                    return;
                }

                NetworkState state = NetworkStateMgr.getInstance().getNetworkState();
                //网络不可用，回调下载错误
                if (state == NetworkState.UNAVAILABLE) {
                    L.d("DownloadMgr# 网络不可用，回调下载错误");
                    listener.onError(remoteUrl, DownloadProgressListener.ERROR_CODE_NETWORK_UNAVAILABLE);
                    return;
                }

                synchronized (mDownloadingImageMap) {
                    ArrayList<DownloadProgressListener> listeners = mDownloadingImageMap.get(key);
                    if (listeners == null) {
                        //不在下载，创建下载回调队列
                        listeners = new ArrayList<DownloadProgressListener>(1);
                        L.d("DownloadMgr# 不在下载，创建下载回调队列:");
                        if (listener != null) {
                            L.d("DownloadMgr# 不在下载，需要回调，添加回调接口到队列");
                            listeners.add(listener);
                        }
                        mDownloadingImageMap.put(key, listeners);
                    } else {
                        //正在下载，把回调接口添加到回调队列
                        if (listener != null && !listeners.contains(listener)) {
                            L.d("DownloadMgr# 正在下载，把回调接口添加到回调队列");
                            listeners.add(listener);
                        }
                        return;
                    }
                }
                
                File tmpDestFile = new File(lUrl + ".tmp");

                if (!FileUtil.fileExist(tmpDestFile.getParent())) {
                    // ensures the directory exists.
                    L.d("DownloadMgr# .tmp文件不存在，创建文件 " + tmpDestFile.getAbsolutePath());
                    tmpDestFile.getParentFile().mkdirs();
                }
                downloadFile(remoteUrl, lUrl, tmpDestFile, key, MAX_RETRY_COUNT, mDownloadingImageMap.get(key));
            }
        });
        
    }
    
    public boolean downloadImageFileSync(final String remoteUrl, final String localUrl) {
        if (TextUtils.isEmpty(remoteUrl)) {
            return false;
        }

        final int key = remoteUrl.hashCode();
        String lUrl = localUrl;
        if (TextUtils.isEmpty(lUrl)) {
            lUrl = FileUtil.getDownloadFilesDir(SmartHomeApp.getInstance()).getAbsolutePath()
                    + File.separator + key;
        }
        //已经下载到本地，回调下载成功
        if (mDownloadedImageMap.containsKey(key)) {
            L.d("DownloadMgr# 已经下载到本地，回调下载成功");
            return true;
        }

        NetworkState state = NetworkStateMgr.getInstance().getNetworkState();
        //网络不可用，回调下载错误
        if (state == NetworkState.UNAVAILABLE) {
            L.d("DownloadMgr# 网络不可用，回调下载错误");
            return false;
        }

        synchronized (mDownloadingImageMap) {
            ArrayList<DownloadProgressListener> listeners = mDownloadingImageMap.get(key);
            if (listeners == null) {
                //不在下载，创建下载回调队列
                listeners = new ArrayList<DownloadProgressListener>(1);
                L.d("DownloadMgr# 不在下载，创建下载回调队列:");
                mDownloadingImageMap.put(key, listeners);
            } else {
                //正在下载，把回调接口添加到回调队列
                return false;
            }
        }
        
        File tmpDestFile = new File(lUrl + ".tmp");

        if (!FileUtil.fileExist(tmpDestFile.getParent())) {
            // ensures the directory exists.
            L.d("DownloadMgr# .tmp文件不存在，创建文件 " + tmpDestFile.getAbsolutePath());
            tmpDestFile.getParentFile().mkdirs();
        }
        return downloadFile(remoteUrl, lUrl, tmpDestFile, key, MAX_RETRY_COUNT, mDownloadingImageMap.get(key));
    }
    
    /**
     * 下载文件，当用域名下载下载失败时自动请求天雷并重试
     * 1. 第一次尝试，查询本地是否有未过期域名对应的IP缓存
     * 2. 如果有缓存，则用IP下载;下载失败，后面重试流程都用域名
     * 3. 没有缓存，则用域名下载;下载失败，则向天雷查询IP,查询成功，则后面重试流程都用查询到IP，否则都用域名
     * @param remoteUrl
     * @param os
     * @param retry
     * @param listener
     * @return
     */
    private boolean downloadFile(String remoteUrl, String localUrl, File tmpDestFile, int key, int retry, ArrayList<DownloadProgressListener> listeners) {
        boolean success = false;
        while (retry-- >= 0) {
            InputStream is = null;
            RandomAccessFile rndFile = null;
            
            HttpURLConnection httpConn = null;
            int statusCode = 0;
            try {
                rndFile = new RandomAccessFile(tmpDestFile, "rw");
                long length = rndFile.length();
                if (length > 0) {
                    rndFile.seek(length);
                }
                
                URL url = new URL(remoteUrl);
                httpConn = (HttpURLConnection)url.openConnection();
                httpConn.setConnectTimeout(3000);
                httpConn.setReadTimeout(3000);
                httpConn.setRequestMethod("GET");
                if (length > 0) {
                    L.d("DownloadMgr# 断点下载，已下载长度 " + length);
                    httpConn.addRequestProperty("Range", "bytes=" + length + "-");
                }
                
//                httpConn.setRequestProperty("Host", Util.getHostName(remoteUrl));
                statusCode   = httpConn.getResponseCode();
                //处理302跳转
                while (statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == HttpURLConnection.HTTP_MOVED_PERM) {
                    remoteUrl = httpConn.getHeaderField("Location");
                    L.d("DownloadMgr# 302跳转，跳转后下载地址 " + remoteUrl);
                    httpConn.disconnect();
                    url = new URL(remoteUrl);
                    httpConn = (HttpURLConnection)url.openConnection();
                    httpConn.setConnectTimeout(3000);
                    httpConn.setReadTimeout(3000);
                    httpConn.setRequestMethod("GET");
                    if (length > 0) {
                        httpConn.addRequestProperty("Range", "bytes=" + length + "-");
                    }
                    statusCode = httpConn.getResponseCode();
                }
                L.d("DownloadMgr# 请求返回 http code " + statusCode);
                if (statusCode == 206 || statusCode == 200) {
                    is = httpConn.getInputStream();
                    int lenRead;
                    long downloadedBytes = length;
                    long fileLength = httpConn.getContentLength() + length;
                    byte[] buffer = new byte[8192];
                    while ((lenRead = is.read(buffer)) != -1) {
                        downloadedBytes += lenRead;

                        if (listeners != null) {
                            for (DownloadProgressListener listener : listeners) {
                                listener.onProgressUpdate(fileLength, downloadedBytes);
                            }
                        }

                        rndFile.write(buffer, 0, lenRead);
                    }
                    L.d("DownloadMgr# 下载成功 ");
                    success = true;
                    break;
                }
                
            } catch (Exception e) {
                L.w(e);
                
            } finally {
                Util.closeCloseable(is);
                Util.closeCloseable(rndFile);
                if (httpConn != null) {
                    httpConn.disconnect();
                }
            }
        }
        
        if (success) {
            boolean result = tmpDestFile.renameTo(new File(localUrl));
            L.d("DownloadMgr# 下载成功，重命名文件 " + result);
            mDownloadedImageMap.put(key, true);
            if (listeners != null) {
                for (DownloadProgressListener listener : listeners) {
                    listener.onComplete(remoteUrl, localUrl);
                }
            }
        } else {
            if (listeners != null) {
                for (DownloadProgressListener listener : listeners) {
                    listener.onError(remoteUrl, DownloadProgressListener.ERROR_CODE_RETRY_FAIL);
                }
            }
        }
        L.d("DownloadMgr# 从正在下载队列中移除记录 ");
        synchronized (mDownloadingImageMap) {
            mDownloadingImageMap.remove(key);
        }

        return success;
    }

}
