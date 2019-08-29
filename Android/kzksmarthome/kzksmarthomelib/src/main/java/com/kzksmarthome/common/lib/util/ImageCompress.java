package com.kzksmarthome.common.lib.util;

import in.srain.cube.util.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.kzksmarthome.common.module.log.L;

/**
 * 图片压缩工具类
 * 
 */
public class ImageCompress {

    /**
     * 图片压缩参数
     * 
     */
    public static class CompressOptions {

        public static final int DEFAULT_WIDTH = 2000;
        public static final int DEFAULT_HEIGHT = 2000;
        public static final int DEFAULT_SIZE = 500 * 1024;

        public int maxWidth = DEFAULT_WIDTH;
        public int maxHeight = DEFAULT_HEIGHT;
        public int maxSize = DEFAULT_SIZE;

        /**
         * 压缩后图片字节
         */
        public byte[] compressBytes;
        /**
         * 压缩后图片保存的文件
         */
        public File destFile;
        /**
         * 图片压缩格式,默认为jpg格式
         */
        public CompressFormat imgFormat = CompressFormat.JPEG;

        public String uri;

        public int oriFileSize;
        /**
         * 图片原始宽
         */
        public int oriW;
        /**
         * 图片原始高
         */
        public int oriH;
        /**
         * 图片压缩后宽
         */
        public int newW;
        /**
         * 图片压缩后高
         */
        public int newH;
        /**
         * 图片压缩质量
         */
        public int quality;
        /**
         * 缩放
         */
        public float scale;
    }

    public void compressImageFile(CompressOptions compressOptions) {

        String filePath = compressOptions.uri;

        L.d("ImageCompress# filePath[%s]", filePath);

        if (null == filePath) {
            return;
        }

        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            compressOptions.oriFileSize = fis.available();
        } catch (Exception e) {
            return;
        } finally {
            if (null != fis) {
                Util.closeCloseable(fis);
            }
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        compressOptions.oriW = options.outWidth;
        compressOptions.oriH = options.outHeight;

        int agree = ImageUtil.readPictureDegree(filePath);
        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;
        if (agree % 180 == 90) {
            actualWidth = options.outHeight;
            actualHeight = options.outWidth;
        }

        int desiredWidth = getResizedDimension(compressOptions.maxWidth, compressOptions.maxHeight, actualWidth, actualHeight);
        int desiredHeight = getResizedDimension(compressOptions.maxHeight, compressOptions.maxWidth, actualHeight, actualWidth);

        options.inJustDecodeBounds = false;
        options.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);

        L.d("ImageCompress# actualWidth[%s],actualHeight[%s], desiredWidth[%s], desiredHeight[%s], inSampleSize[%s]", actualWidth,
                actualHeight, desiredWidth, desiredHeight, options.inSampleSize);

        Bitmap bitmap = null;
        Bitmap destBitmap = null;
        try {
            destBitmap = BitmapFactory.decodeFile(filePath, options);

            if (agree % 360 != 0) {
                destBitmap = ImageUtil.rotaingImageView(agree, destBitmap);
            }

            if (null == destBitmap) {
                return;
            }

            // If necessary, scale down to the maximal acceptable size.
            if (destBitmap.getWidth() > desiredWidth || destBitmap.getHeight() > desiredHeight) {
                L.d("ImageCompress# createScaledBitmap desiredWidth %s, desiredHeight %s", desiredWidth, desiredHeight);
                bitmap = Bitmap.createScaledBitmap(destBitmap, desiredWidth, desiredHeight, true);
                if (!destBitmap.isRecycled()) {
                    destBitmap.recycle();
                }
            } else {
                bitmap = destBitmap;
            }
            compressOptions.newW = bitmap.getWidth();
            compressOptions.newH = bitmap.getHeight();
            compressOptions.scale = (float) (Math.round((float) compressOptions.newW / compressOptions.oriW * 100)) / 100;
            // compress file if need
            compressFile(compressOptions, bitmap);
            L.d("ImageCompress# filePath[%s], compress complete.........", filePath);
        } catch (OutOfMemoryError e) {
            L.e(e);
        } finally {
            if (null != bitmap && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            System.gc();
        }

    }

    /**
     * compress file from bitmap with compressOptions
     * 
     * @param compressOptions
     * @param bitmap
     */
    private void compressFile(CompressOptions compressOptions, Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 90;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.size() > compressOptions.maxSize) {
            quality = quality - 10;
            baos.reset();
            if (quality <= 0) {
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        compressOptions.quality = quality;
        compressOptions.compressBytes = baos.toByteArray();
        // TODO 以下配合专项测试
        if (L.isUploadTest()) {
            byte[] temp = String.valueOf(System.currentTimeMillis()).getBytes();
            if (null != compressOptions.compressBytes && compressOptions.compressBytes.length >= temp.length) {
                System.arraycopy(temp, 0, compressOptions.compressBytes, compressOptions.compressBytes.length - temp.length, temp.length);
            }
        }

        L.d("ImageCompress# quality[%s], size[%s]", quality, compressOptions.compressBytes.length);

        if (null != compressOptions.destFile) {
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(compressOptions.destFile);
                baos.writeTo(stream);
                baos.flush();

            } catch (Exception e) {
                L.e(e);
            } finally {
                if (stream != null) {
                    Util.closeCloseable(stream);
                }
            }
        }
        baos.reset();
        Util.closeCloseable(baos);
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {

        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }
}