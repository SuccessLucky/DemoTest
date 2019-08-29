package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * drawableLeft与文本一起居中显示
 * @author jack
 */
public class DrawableCenterTextView extends TextView {
    /**
     * 绘制图片数组
     */
    private Drawable[] mDrawables;
    /**
     * 左侧图片
     */
    private Drawable mDrawableLeft;
    /**
     * 右侧图片
     */
    private Drawable mDrawableRight;
    /**
     * 文本宽度
     */
    private float mTextWidth;
    /**
     * 文本与图片的距离
     */
    private int mDrawablePadding;
    /**
     * 图片宽度
     */
    private int mDrawableWidth;
    /**
     * 文本加图片的宽度
     */
    private float mBodyWidth;



    public DrawableCenterTextView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
  /*      Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }*/
        canvas.translate((getWidth() - mBodyWidth) / 2, 0);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measure();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 测量textView的大小
     */
    public void measure(){
        mDrawables = getCompoundDrawables();
        if (mDrawables != null) {
            mDrawableLeft = mDrawables[0];
            if (mDrawableLeft != null) {
                mTextWidth = getPaint().measureText(getText().toString());
                mDrawablePadding = getCompoundDrawablePadding();
                mDrawableWidth = 0;
                mDrawableWidth = mDrawableLeft.getIntrinsicWidth();
                mBodyWidth = mTextWidth + mDrawableWidth + mDrawablePadding;
            }
            mDrawableRight = mDrawables[2];
            if(mDrawableRight != null){
                mTextWidth = getPaint().measureText(getText().toString());
                mDrawablePadding = getCompoundDrawablePadding();
                mDrawableWidth = 0;
                mDrawableWidth = mDrawableRight.getIntrinsicWidth();
                mBodyWidth = mTextWidth - mDrawableWidth - mDrawablePadding;
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        measure();
    }
}