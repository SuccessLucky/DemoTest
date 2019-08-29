package com.kzksmarthome.common.module.download;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.network.NetworkState;
import com.kzksmarthome.common.lib.network.NetworkStateMgr;
import com.kzksmarthome.lib.R;

public class DownloadNotifyStateHolder {

    public static final int ACTION_STOP = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_RETRY_DOWNLOAD = 3;
    public static final int ACTION_INSTALL = 4;
    public static final int ACTION_EXTRACT_DATA = 5;

    private Context mContext;
    // private int btnAction = 0;
    public String tvNotifTitle = "";
    public String tvNotifText1 = "";
    public String tvNotifText2 = "";
    public String tvNotifTime = "";
    public String btnActionText = SmartHomeAppLib.getInstance().getContext().getString(R.string.stop);
    public PendingIntent btnActionPendingIntent = null;
    public int ivStateIconSrc = R.drawable.icon_notify_download;
    // private int ivNetworkIconSrc = R.drawable.icon_download;
    public int progress = 0;
    public int btnActionVisibility = View.GONE;
    public int progressBarVisibility = View.GONE;
    public int tvNotifText1Visibility = View.VISIBLE;
    public int tvNotifText2Visibility = View.VISIBLE;
    public int ivNetworkIconVisibility = View.GONE;
    public int indeterminateProgressBarVisibility = View.GONE;
    /**
     * 是否有下载图标
     */
    private boolean hasIcon = true;

    public DownloadNotifyStateHolder(Context ctx) {
        mContext = ctx;
    }

    public DownloadNotifyStateHolder(Context ctx, boolean hasIcon) {
        mContext = ctx;
        this.hasIcon = hasIcon;
    }

    public RemoteViews buildRemoteView() {
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(),
                R.layout.download_notification);
        contentView.setTextViewText(R.id.tvNotifTitle, tvNotifTitle);
        contentView.setViewVisibility(R.id.tvNotifText1, tvNotifText1Visibility);

        if (tvNotifText1Visibility == View.VISIBLE) {
            contentView.setTextViewText(R.id.tvNotifText1, tvNotifText1);
        }

        contentView.setTextViewText(R.id.tvNotifTime, tvNotifTime);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {// android 3.0以上才显示
            btnActionVisibility = View.GONE;
        }

        contentView.setViewVisibility(R.id.btnAction, btnActionVisibility);

        if (btnActionVisibility == View.VISIBLE) {
            contentView.setTextViewText(R.id.btnAction, btnActionText);

            if (btnActionPendingIntent != null) {
                contentView.setOnClickPendingIntent(R.id.btnAction, btnActionPendingIntent);
            }
        }
        if (hasIcon) {
            contentView.setImageViewResource(R.id.ivStateIcon, ivStateIconSrc);
        }else{
            contentView.setViewVisibility(R.id.ivStateIcon, View.GONE);
        }
        contentView.setViewVisibility(R.id.tvNotifText2, tvNotifText2Visibility);

        if (tvNotifText2Visibility == View.VISIBLE) {
            contentView.setTextViewText(R.id.tvNotifText2, tvNotifText2);
        }

        contentView.setViewVisibility(R.id.progressBarWrapper, progressBarVisibility);

        if (progressBarVisibility == View.VISIBLE) {
            contentView.setProgressBar(R.id.progressBar, 100, progress, false);
        }

        contentView.setViewVisibility(R.id.ivNetworkIcon, ivNetworkIconVisibility);

        if (ivNetworkIconVisibility == View.VISIBLE) {
            NetworkState state = NetworkStateMgr.getInstance().getNetworkState();

            if (state == NetworkState.WIFI) {
                contentView.setImageViewResource(R.id.ivNetworkIcon, R.drawable.body_icon_wifi);

            } else if (state.is234G()) {
                contentView.setImageViewResource(R.id.ivNetworkIcon, R.drawable.body_icon_nowifi);

            } else {
                contentView.setViewVisibility(R.id.ivNetworkIcon, View.GONE);
            }
        }

        return contentView;
    }

}
