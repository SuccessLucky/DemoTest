package com.kzksmarthome.SmartHouseYCT.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.kzksmarthome.SmartHouseYCT.R;

/**
 * Created by huyg on 15/12/9.
 */
public class ColorPicker extends ImageView {
    private Context mContext;
    private OnColorChangedListener mListener;
    private Bitmap mBitMap;
    private Rect mRect;
    private Paint mSelectPaint;
    private float touchX;
    private float touchY;
    private float lastTouchX;
    private float lastTouchY;
    private int color = Color.GREEN;
    private int pointRadius;
    private long time;

    public ColorPicker(Context context) {
        super(context);
        init(context, null);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attributeSet) {
        this.mContext = context;
        setScaleType(ScaleType.CENTER_CROP);
        mBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.img_dimmer);
        setImageBitmap(mBitMap);

        mSelectPaint = new Paint();
        mSelectPaint.setAntiAlias(true);
        mSelectPaint.setStyle(Paint.Style.STROKE);
        mSelectPaint.setColor(Color.BLACK);
        mSelectPaint.setStrokeWidth(2f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int intrinsicSize = 2 * 150;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(intrinsicSize, widthSize);
        } else {
            width = intrinsicSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(intrinsicSize, heightSize);
        } else {
            height = intrinsicSize;
        }
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        touchX = getWidth() / 2;
        touchY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawRect(touchX - 10, touchY - 10, touchX + 10, touchY + 10, mSelectPaint);
        canvas.drawCircle(touchX, touchY, 20, mSelectPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        switch (event.getAction()) {
            //case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                double radius = Math.sqrt(Math.pow(touchX - getWidth() / 2, 2) + Math.pow(touchY - getHeight() / 2, 2));
                if (getWidth() / 2 >= radius) {
                    lastTouchX = touchX;
                    lastTouchY = touchY;
                } else {
                    touchX = lastTouchX;
                    touchY = lastTouchY;
                }
                color = mBitMap.getPixel((int) (touchX / getWidth() * mBitMap.getWidth()), (int) (touchY / getHeight() * mBitMap.getHeight()));
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                    if(null != mListener){
                        mListener.onColorChanged(color);
                    }
                break;
        }

        return true;
    }

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.mListener = listener;
    }

    public OnColorChangedListener getColorChangedListener() {
        return this.mListener;
    }
}