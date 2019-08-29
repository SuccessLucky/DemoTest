package com.kzksmarthome.common.module.upgrade;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import com.kzksmarthome.SmartHouseYCT.biz.splash.SplashActivity;
import com.kzksmarthome.common.lib.util.FileUtil;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.download.DownloadMgr;
import com.kzksmarthome.common.module.download.DownloadNotifyStateHolder;
import com.kzksmarthome.common.module.download.DownloadProgressListener;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.lib.R;

/**
 * 
 * 应用自升级下载服务，前台检查到有更新，只需把更新地址通过Intent启动此服务，即可自动下载，通知栏显示下载进度，下载完成启动安装
 * 
 * @author panrq
 * @createDate 2015-4-29
 *
 */
public class ArrowUpgradeService extends Service {
    private boolean mRunning;
    private long mUpdateUITime;

    @Override
    public void onCreate() {
        super.onCreate();
        L.i("UpgradeService onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    

    @Override
    public void onDestroy() {
        mRunning = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mRunning) {
            Toast.makeText(this, getString(R.string.start_service_download), Toast.LENGTH_SHORT).show();

        } else {
            mRunning = true;
            final String appUrl = intent.getStringExtra("appUrl");

            if (appUrl == null) {
                Toast.makeText(this, getString(R.string.error_url), Toast.LENGTH_SHORT).show();
                stopSelf();

            } else {
                final Notification mNotif = new Notification();
                final DownloadNotifyStateHolder notifState = new DownloadNotifyStateHolder(getApplicationContext());
                final NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Intent openMainIntent = new Intent();
                openMainIntent.setClass(this, SplashActivity.class);
                openMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                openMainIntent.putExtra("from", "upgrade");
                openMainIntent.setType("notification");
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, openMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // mNotif.icon = R.drawable.icon;
                mNotif.icon = R.drawable.ic_launcher;
                mNotif.tickerText = getString(R.string.start_download);
                // mNotif.setLatestEventInfo(this, getString(R.string.app_name), "%0", NineGameClientApplication.getInstance().getOpenMainAppPendingIntent());
                notifState.tvNotifTitle = getString(R.string.app_name);
                mNotif.contentView = notifState.buildRemoteView();
                mNotif.contentIntent = pendingIntent;
                // mNotif.contentView.setTextViewText(R.id.tvNotifText2, "解压中");
                mNotificationManager.notify(ArrowUpgradeService.class.hashCode(), mNotif);
                
                DownloadProgressListener listener = new DownloadProgressListener() {

                    @Override
                    public void onComplete(String remoteUrl, String localUrl) {
                        L.i("UpgradeService onComplete");
                        Uri uri = Uri.parse("file://" + localUrl);
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent
                                .setDataAndType(uri, "application/vnd.android.package-archive");
                        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mNotif.defaults = Notification.DEFAULT_SOUND;
                        mNotif.tickerText = getString(R.string.app_name) + getString(R.string.download_success);
                        mNotif.icon = R.drawable.ic_launcher;
                        mNotif.flags |= Notification.FLAG_AUTO_CANCEL;
                        notifState.tvNotifText1 = getString(R.string.download_success);
                        notifState.tvNotifText2Visibility = View.GONE;
                        notifState.progressBarVisibility = View.GONE;
                        notifState.ivNetworkIconVisibility = View.GONE;
                        mNotif.contentView = notifState.buildRemoteView();
                        mNotificationManager.notify(ArrowUpgradeService.class.hashCode(), mNotif);
                        try {
                            startActivity(installIntent);
                        } catch (Exception e) {
                            L.w(e);
                        }
                        stopSelf();
                    }

                    @Override
                    public void onError(String remoteUrl, int errorCode) {
                        L.i("UpgradeService onError");
                        mNotif.tickerText = getString(R.string.app_name) + getString(R.string.download_fail);
                        mNotif.icon = R.drawable.ic_launcher;
                        mNotif.flags |= Notification.FLAG_AUTO_CANCEL;
                        notifState.tvNotifText1 = getString(R.string.download_fail);
                        notifState.tvNotifText2Visibility = View.GONE;
                        notifState.progressBarVisibility = View.GONE;
                        notifState.ivNetworkIconVisibility = View.GONE;
                        mNotif.contentView = notifState.buildRemoteView();
                        mNotificationManager.notify(ArrowUpgradeService.class.hashCode(), mNotif);
                        stopSelf();
                    }

                    @Override
                    public void onProgressUpdate(long totalSize, long downloadedSize) {
                        if (totalSize > 0) {
                            boolean needUpdateUI = false;
                            long currentTime = System.currentTimeMillis();

                            if (mUpdateUITime == 0) {
                                needUpdateUI = true;

                            } else {
                                if (currentTime - mUpdateUITime > 500) {
                                    needUpdateUI = true;
                                }
                            }

                            if (needUpdateUI) {
                                mUpdateUITime = currentTime;
                                notifState.progressBarVisibility = View.VISIBLE;
                                notifState.tvNotifText2Visibility = View.VISIBLE;
                                notifState.ivNetworkIconVisibility = View.VISIBLE;
                                int progress = (int) (downloadedSize * 100 / totalSize);
                                notifState.progress = progress;
                                notifState.tvNotifText1 = Util.formatNumberInPercent(progress);
                                notifState.tvNotifText2 = getString(R.string.downloading);
                                mNotif.contentView = notifState.buildRemoteView();
                                mNotificationManager
                                        .notify(ArrowUpgradeService.class.hashCode(), mNotif);
                                L.i("UpgradeService onProgressUpdate needUpdateUI");
                            }
                        }
                    }

                };
                try {
                    File mDestFile = new File(FileUtil.getDownloadFilesDir(getApplication()),
                            appUrl.hashCode() + "_upgrade.apk");
                    DownloadMgr.getInstance().downloadImageFile(listener, appUrl,
                            mDestFile.getAbsolutePath());
                } catch (Exception e) {
                    L.w(e);
                    listener.onError(appUrl, DownloadProgressListener.ERROR_CODE_UNKNOWN);
                }
            }
        }

        return START_REDELIVER_INTENT;
    }
}
