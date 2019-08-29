package com.kzksmarthome.common.biz.widget;

import com.kzksmarthome.common.module.log.L;

import in.srain.cube.image.CubeImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
/**
 * 正方形的ImageView
 * @author dty
 *
 */
public class SquareCenterImageView extends CubeImageView {
    public SquareCenterImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }
 
    public SquareCenterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public SquareCenterImageView(Context context) {
        super(context);
    }
    
    @Override
    public void setImageBitmap(Bitmap bm) {
        L.d("roamer bm.getWidth(): %s", bm.getWidth());
        L.d("roamer bm.getHeight(): %s", bm.getHeight());
        super.setImageBitmap(bm);
    }
    
    private void clipCenterImage(){
        
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
 
        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        //高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}


