package com.kzksmarthome.SmartHouseYCT.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by lizhi on 2017/10/7.
 */

public class VideoViewFullScreen extends VideoView {
    public VideoViewFullScreen(Context context) {
        super(context);
    }

    public VideoViewFullScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewFullScreen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0, widthMeasureSpec);//得到默认的大小（0，宽度测量规范）
        int height = getDefaultSize(0, heightMeasureSpec);//得到默认的大小（0，高度度测量规范）
        setMeasuredDimension(width, height); //设置测量尺寸,将高和宽放进去
    }
}
