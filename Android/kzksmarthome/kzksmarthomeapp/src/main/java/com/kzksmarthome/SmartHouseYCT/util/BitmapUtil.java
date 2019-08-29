package com.kzksmarthome.SmartHouseYCT.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kzksmarthome.common.module.log.L;

/**
 * Created with IntelliJ IDEA.
 * User: xiejm
 * Date: 7/15/13
 * Time: 11:30 AM
 */
public class BitmapUtil {
    //拉伸图片（不按比例）以填充View的宽高
    public static final int SCALE_TYPE_FIT_XY = 0;
    
    //按比例拉伸图片，拉伸后图片的高度为View的高度，且显示在View的左边
    public static final int SCALE_TYPE_FIT_START = 1;
    
    //按比例拉升，拉伸后图片的宽度为View的宽度
    public static final int SCALE_TYPE_FIT_Y = 2;
    
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        L.d(">>>> calculateInSampleSize: " + reqWidth + ", " + reqHeight + ", " + width + ", " + height);

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Note: this method may return null
     * @param fileName
     * @param reqWidth
     * @param reqHeight
     * @return a bitmap decoded from the specified file
     */
    public static Bitmap decodeSampledBitmapFromFileName(String fileName, int reqWidth, int reqHeight) {
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(fileName, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            L.d(">>>> decodeSampledBitmapFromFileName inSampleSize: " + options.inSampleSize);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(fileName, options);

        } catch (OutOfMemoryError e) {
            L.w(e);
        }

        return null;
    }

    public static Bitmap createRoundCornerBitmap(Bitmap srcBitmap, float roundPixel, int reqWidth, int reqHeight, boolean recycleOrig) {
        if (reqWidth == 0) reqWidth = srcBitmap.getWidth();

        if (reqHeight == 0) reqHeight = srcBitmap.getHeight();

        final Rect srcRect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        final Rect dstRect = new Rect(0, 0, reqWidth, reqHeight);

        L.d(">>>> createRoundCornerBitmap: " + srcBitmap.getWidth() + ", " + srcBitmap.getHeight());

        Bitmap bmp = Bitmap.createBitmap(reqWidth, reqHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint ROUND_CORNER_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawRoundRect(new RectF(dstRect), roundPixel, roundPixel, ROUND_CORNER_PAINT);
        ROUND_CORNER_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(srcBitmap, srcRect, dstRect, ROUND_CORNER_PAINT);

        if (recycleOrig && srcBitmap != null && !srcBitmap.isRecycled()) {
            srcBitmap.recycle();
        }

        return bmp;
    }
    
    /**
     * create scaled bitmap with required width and height
     * 
     * @param srcBitmap
     * @param reqWidth
     * @param reqHeight
     * @param recycleOrig
     * @param scaleType
     * @return
     */
    public static Bitmap createBitmap(Bitmap srcBitmap, int reqWidth, int reqHeight, boolean recycleOrig,int scaleType) {
        int bitmapWidth = srcBitmap.getWidth();
        int bitmapHeight= srcBitmap.getHeight();
        if (reqWidth == 0) reqWidth = bitmapWidth;
        if (reqHeight == 0) reqHeight = bitmapHeight;
        
//        final Rect srcRect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
//        final Rect dstRect = new Rect(0, 0, reqWidth, reqHeight);
        float scaleWidth = 1;
        float scaleHeight= 1;
        if(scaleType == SCALE_TYPE_FIT_START) {
            scaleWidth = (reqWidth / bitmapWidth < reqHeight
                    / bitmapHeight) ? (float)reqWidth / (float)bitmapWidth : (float)reqHeight
                    / (float)bitmapHeight;
            scaleHeight = scaleWidth;
        } else if(scaleType == SCALE_TYPE_FIT_XY) {
            scaleWidth = (float)reqWidth / (float)bitmapWidth;
            scaleHeight = (float)reqHeight / (float)bitmapHeight;
        }
        
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        
        Bitmap resizedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        
        if (recycleOrig) {
            srcBitmap.recycle();
        }
        
        return resizedBitmap;
    }
    
    

    public static Bitmap createMaskedBitmap(Bitmap srcBitmap, int brightness, boolean recycleOrig) {
        Bitmap bmp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);

        L.d("createMaskedBitmap: " + srcBitmap.getWidth() + ", " + srcBitmap.getHeight());

        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] {1, 0, 0, 0, brightness,
                                 0, 1, 0, 0, brightness,// 改变亮度
                                 0, 0, 1, 0, brightness,
                                 0, 0, 0, 1, 0
                                });
        Paint MASK_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        MASK_PAINT.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        canvas.drawBitmap(srcBitmap, 0, 0, MASK_PAINT);

        if (recycleOrig && srcBitmap != null && !srcBitmap.isRecycled()) {
            srcBitmap.recycle();
        }

        return bmp;
    }

    public static Bitmap createRoundCornerAndMaskedBitmap(Bitmap srcBitmap, float roundPixel, int brightness, int reqWidth, int reqHeight, boolean recycleOrig) {
        try {
            if (reqWidth == 0) reqWidth = srcBitmap.getWidth();

            if (reqHeight == 0) reqHeight = srcBitmap.getHeight();

            final Rect srcRect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
            final Rect dstRect = new Rect(0, 0, reqWidth, reqHeight);

            L.d(">>>> createRoundCornerAndMaskedBitmap: " + srcBitmap.getWidth() + ", " + srcBitmap.getHeight());

            Bitmap bmp = Bitmap.createBitmap(reqWidth, reqHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bmp);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            Paint ROUND_CORNER_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawRoundRect(new RectF(dstRect), roundPixel, roundPixel, ROUND_CORNER_PAINT);
            ROUND_CORNER_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(srcBitmap, srcRect, dstRect, ROUND_CORNER_PAINT);
            ColorMatrix cMatrix = new ColorMatrix();
            cMatrix.set(new float[] {1, 0, 0, 0, brightness,
                                     0, 1, 0, 0, brightness,// 改变亮度
                                     0, 0, 1, 0, brightness,
                                     0, 0, 0, 1, 0
                                    });
            Paint MASK_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
            MASK_PAINT.setColorFilter(new ColorMatrixColorFilter(cMatrix));
            canvas.drawBitmap(bmp, 0, 0, MASK_PAINT);

            if (recycleOrig && srcBitmap != null && !srcBitmap.isRecycled()) {
                srcBitmap.recycle();
            }

            return bmp;

        } catch (OutOfMemoryError e) {
            L.e("createRoundCornerAndMaskedBitmap :" + e);
            return srcBitmap;
        }
    }

    public static Bitmap setRotate(Bitmap bitmap, int degree, boolean recycleOrig) {
        try {
            Matrix matrix = new Matrix();
            //设置图像的旋转角度
            matrix.setRotate(degree);
            //旋转图像，并生成新的Bitmap对像
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            if (recycleOrig && bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return newBitmap;

        } catch (OutOfMemoryError e) {
            L.w(e);
        }

        return bitmap;
    }

    /**
     * Bitmap 用于缩放的bitmap
     * @param rect
     *         Display对象
     * @param bitmap
     *          图片
     * @param isResizeWidth
     *          true按宽度拉，false按高度拉

     * @return Bitmap
     */
    public static Bitmap resizeBitmap(Rect rect, Bitmap bitmap, boolean isResizeWidth, boolean recycleOrig) {
        return resizeBitmap(rect.width(), rect.height(), bitmap, isResizeWidth, recycleOrig);
    }
    
    /**
     * Bitmap 用于缩放的bitmap
     * @param w 后的宽度
     * @param h 后的高度
     * @param bitmap
     *          图片
     * @param isResizeWidth
     *          true按宽度拉，false按高度拉

     * @return Bitmap
     */
    public static Bitmap resizeBitmap(int w, int h, Bitmap bitmap, boolean isResizeWidth, boolean recycleOrig) {
        int screenWidth = w;
        int screenHeight = h;

        try {
            if (isResizeWidth) {
                if (screenWidth != bitmap.getWidth()) {
                    float scale = (float)screenWidth / bitmap.getWidth();
                    int height = (int)(bitmap.getHeight() * scale);
                    Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, height, true);

                    if (recycleOrig && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }

                    bitmap = newBitmap;
                }

                // 按宽度拉伸后，高度超出screenHeight，所以要把超出的部分截取掉
                if (bitmap.getHeight() > screenHeight) {
                    int startY = (bitmap.getHeight() - screenHeight) / 2;
                    Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, startY, screenWidth, screenHeight);

                    if (recycleOrig && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }

                    bitmap = newBitmap;
                }

            } else {
                if (screenHeight != bitmap.getHeight()) {  //
                    float scale = (float)screenHeight / bitmap.getHeight();
                    int width = (int)(bitmap.getWidth() * scale);

                    if (width > screenWidth) { //如果拉伸完宽度大于屏幕宽度，则按宽度拉伸
                        float widthScale = (float)screenWidth / bitmap.getWidth();
                        int height = (int)(bitmap.getHeight() * widthScale);
                        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, height, true);

                        if (recycleOrig && widthScale != 1 && !bitmap.isRecycled()) {
                            bitmap.recycle();
                        }

                        bitmap = newBitmap;

                    } else {
                        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, screenHeight, true);

                        if (recycleOrig && !bitmap.isRecycled()) {
                            bitmap.recycle();
                        }

                        bitmap = newBitmap;
                    }
                }
            }

        } catch (OutOfMemoryError e) {
            L.w(e);
        }

        return bitmap;
    }
    
    public static void saveBitmap(Bitmap bitmap, String filePath) {
        File f = new File(filePath);
        try {
            f.createNewFile();
        } catch (IOException e) {
            L.e(e);
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            L.e(e);
        }
        try {
            fOut.close();
        } catch (IOException e) {
            L.e(e);
        }
    }
    
    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }
}
