package in.srain.cube.image;

import in.srain.cube.image.iface.ImageMemoryCache;
import in.srain.cube.image.iface.ImageResizer;
import in.srain.cube.util.CLog;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;

public class ImageProviderForAlbums extends ImageProviderExpand {

    public static final String TAG = ImageProviderForAlbums.class.getSimpleName();

    private static ContentResolver mContentResolver;
    private static String[] thumbProj = new String[] { MediaStore.Images.Thumbnails.DATA };
    private static String[] imageProj = new String[] { MediaStore.Images.Media._ID };
    private static Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static Uri thumbUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    public static LinkedHashMap<String, Long> cacheImageIdMap = new LinkedHashMap<String, Long>() {

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<String, Long> eldest) {
            return size() > 60;
        };
    };
    public static LinkedHashMap<Long, String> cacheThumbUrlMap = new LinkedHashMap<Long, String>() {

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<Long, String> eldest) {
            return size() > 300;
        };
    };

    public ImageProviderForAlbums(Context context, ImageMemoryCache memoryCache, ImageDiskCacheProvider fileProvider) {
        super(context, memoryCache, fileProvider);
    }

    @Override
    public Bitmap fetchBitmapData(ImageLoader imageLoader, ImageTask imageTask, ImageResizer imageResizer) {

        if (!isUrl(imageTask.mOriginUrl)) {
            if (null != imageTask.mReuseInfo && imageTask.mReuseInfo.getIdentitySize().equals("local_photo_preview")) {
                // 预览大图
                return super.fetchBitmapData(imageLoader, imageTask, imageResizer);
            }

            String oriUrl = imageTask.mOriginUrl;
            if (null == mContentResolver) {
                mContentResolver = imageLoader.mContext.getContentResolver();
            }

            // 查找大图imageid缓存
            Long imageId = cacheImageIdMap.get(oriUrl);
            if (null == imageId) {
                // 查找大图imageid
                imageId = getImageUrl(mContentResolver, oriUrl);
                if (imageId < 0) {
                    return null;
                } else {
                    cacheImageIdMap.put(oriUrl, imageId);
                }
            }

            Bitmap bitmap = null;
            // 查找缩略图地址缓存
            String thumbUrl = cacheThumbUrlMap.get(imageId);
            // 查找缩略图地址
            if (TextUtils.isEmpty(thumbUrl)) {
                thumbUrl = getThumbUrl(mContentResolver, imageId);
            }

            // 缩略图
            if (!TextUtils.isEmpty(thumbUrl)) {
                imageTask.mOriginUrl = thumbUrl;
                bitmap = getLocalImage(imageTask, imageResizer);
            }

            imageTask.mOriginUrl = oriUrl;
            if (null == bitmap) {
                // 缩略图获取失败，获取大图
                bitmap = getLocalImage(imageTask, imageResizer);
                if (imageTask.getRequestSize().x * imageTask.getRequestSize().y * 3 > bitmap.getWidth() * bitmap.getHeight()) {
                    // 生成缩略图，只对较大图片生成缩略图
                    bitmap = scaleBitmap(imageTask, bitmap);
                    storeThumbnail(mContentResolver, bitmap, imageId);
                } else {
                    // 小图，缓存原始地址代替缩略图地址
                    cacheThumbUrlMap.put(imageId, oriUrl);
                    bitmap = scaleBitmap(imageTask, bitmap);
                }
            } else {
                cacheThumbUrlMap.put(imageId, thumbUrl);
                bitmap = scaleBitmap(imageTask, bitmap);
            }
            return bitmap;
        } else {
            return super.fetchBitmapData(imageLoader, imageTask, imageResizer);
        }
    }

    /**
     * 按照有效显示区域缩放图片
     * 
     * @param imageTask
     * @param bm
     * @return
     */
    public Bitmap scaleBitmap(ImageTask imageTask, Bitmap bm) {

        int reqW = imageTask.getRequestSize().x;
        int reqH = imageTask.getRequestSize().y;

        int x = 0;
        int y = 0;
        float w = bm.getWidth();
        float h = bm.getHeight();
        Matrix m = new Matrix();

        float scaleW = reqW / w;
        float scaleH = reqH / h;
        float scale = 1;
        boolean isScale = false;

        if (scaleW < scaleH) {
            if (h > reqH || w > reqW) {
                scale = scaleH;
                float newW = reqW / scale;
                x = (int) ((w - newW) / 2);
                w = newW;
                isScale = true;
            }
            if (isScale && h <= reqH) {
                scale = 1;
            }
        } else if (scaleW > scaleH) {
            if (w > reqW || h > reqH) {
                scale = scaleW;
                float newH = reqH / scale;
                y = (int) ((h - newH) / 2);
                h = newH;
                isScale = true;
            }
            if (isScale && w <= reqW) {
                scale = 1;
            }
        }

        if (isScale) {
            m.setScale(scale, scale);
            Bitmap newBm = Bitmap.createBitmap(bm, x, y, (int) w, (int) h, m, true);
            bm.recycle();
            bm = newBm;
        }

        return bm;
    }

    /**
     * 获取缩略图地址
     * 
     * @return
     */
    private String getThumbUrl(ContentResolver mContentResolver, long imageId) {
        CLog.d(TAG, " getThumbUrl imageId[%s]", imageId);
        Cursor cursor = mContentResolver.query(thumbUri, thumbProj, MediaStore.Images.Thumbnails.IMAGE_ID + "=? ",
                new String[] { String.valueOf(imageId) }, MediaStore.Images.Thumbnails.DEFAULT_SORT_ORDER);
        String thumbUrl = null;
        while (cursor.moveToNext()) {
            thumbUrl = cursor.getString(0);
            break;
        }
        cursor.close();
        CLog.d(TAG, " getThumbUrl thumbUrl[%s]", thumbUrl);
        return thumbUrl;
    }

    /**
     * 获取大图id
     * 
     * @param mContentResolver
     * @param path
     * @return
     */
    private long getImageUrl(ContentResolver mContentResolver, String path) {
        CLog.d(TAG, " getImageUrl path[%s]", path);
        Cursor cursor = mContentResolver.query(imageUri, imageProj, MediaStore.Images.Media.DATA + "=? ", new String[] { path },
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        long imageId = -1;
        while (cursor.moveToNext()) {
            imageId = cursor.getInt(0);
            break;
        }
        cursor.close();
        CLog.d(TAG, " getImageUrl imageId[%s]", imageId);
        return imageId;
    }

    private Uri storeThumbnail(ContentResolver cr, Bitmap source, long id) {
        ContentValues values = new ContentValues(4);
        values.put(Images.Thumbnails.KIND, Images.Thumbnails.MINI_KIND);
        values.put(Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(Images.Thumbnails.HEIGHT, source.getHeight());
        values.put(Images.Thumbnails.WIDTH, source.getWidth());

        Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            source.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            CLog.d(TAG, " storeThumbnail imageId[%s], url[%s]", id, url);
            return url;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void clearMemoryCache() {
        super.clearMemoryCache();
        cacheImageIdMap.clear();
        cacheThumbUrlMap.clear();
    }
}
