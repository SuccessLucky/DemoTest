package in.srain.cube.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import in.srain.cube.image.drawable.RecyclingBitmapDrawable;
import in.srain.cube.image.iface.ImageMemoryCache;
import in.srain.cube.image.iface.ImageResizer;
import in.srain.cube.util.CLog;
import in.srain.cube.util.Debug;
import in.srain.cube.util.ImageUtil;
import in.srain.cube.util.Version;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class handles disk and memory caching of bitmaps.
 * <p/>
 * Most of the code is taken from the Android best practice of displaying Bitmaps <a
 * href="http://developer.android.com/training/displaying-bitmaps/index.html">Displaying Bitmaps Efficiently</a>.
 * 
 * @author http://www.liaohuqiu.net
 */
public class ImageProvider {

    protected static final String TAG = Debug.DEBUG_IMAGE_LOG_TAG_PROVIDER;

    private static final String MSG_FETCH_BEGIN = "%s fetchBitmapData";
    private static final String MSG_FETCH_BEGIN_IDENTITY_KEY = "%s identityKey: %s";
    private static final String MSG_FETCH_BEGIN_FILE_CACHE_KEY = "%s fileCacheKey: %s";
    private static final String MSG_FETCH_BEGIN_IDENTITY_URL = "%s identityUrl: %s";
    private static final String MSG_FETCH_BEGIN_ORIGIN_URL = "%s originUrl: %s";

    private static final String MSG_FETCH_TRY_REUSE = "%s Disk Cache not hit. Try to reuse";
    private static final String MSG_FETCH_HIT_DISK_CACHE = "%s Disk Cache hit";
    private static final String MSG_FETCH_REUSE_SUCCESS = "%s reuse size: %s";
    private static final String MSG_FETCH_REUSE_FAIL = "%s reuse fail: %s, %s";
    private static final String MSG_FETCH_DOWNLOAD = "%s downloading: %s";
    private static final String MSG_DECODE = "%s decode: %sx%s inSampleSize:%s";

    private ImageMemoryCache mMemoryCache;
    protected ImageDiskCacheProvider mDiskCacheProvider;

    public ImageProvider(Context context, ImageMemoryCache memoryCache, ImageDiskCacheProvider fileProvider) {
        mMemoryCache = memoryCache;
        mDiskCacheProvider = fileProvider;
    }

    /**
     * Return the byte usage per pixel of a bitmap based on its configuration.
     * 
     * @param config The bitmap configuration.
     * @return The byte usage per pixel.
     */
    private static int getBytesPerPixel(Config config) {
        if (config == Config.ARGB_8888) {
            return 4;
        } else if (config == Config.RGB_565) {
            return 2;
        } else if (config == Config.ARGB_4444) {
            return 2;
        } else if (config == Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    /**
     * Get the size in bytes of a bitmap in a BitmapDrawable. Note that from Android 4.4 (KitKat) onward this returns the allocated memory
     * size of the bitmap which can be larger than the actual bitmap data byte count (in the case it was re-used).
     * 
     * @param value
     * @return size in bytes
     */
    @TargetApi(19)
    // @TargetApi(VERSION_CODES.KITKAT)
    public static int getBitmapSize(BitmapDrawable value) {
        if (null == value) {
            return 0;
        }
        Bitmap bitmap = value.getBitmap();

        // From KitKat onward use getAllocationByteCount() as allocated bytes can potentially be
        // larger than bitmap byte count.
        if (Version.hasKitKat()) {
            return bitmap.getAllocationByteCount();
        }

        if (Version.hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }

        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Create a BitmapDrawable which can be managed in ImageProvider
     * 
     * @param resources
     * @param bitmap
     * @return
     */
    public BitmapDrawable createBitmapDrawable(Resources resources, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        BitmapDrawable drawable = null;
        if (bitmap != null) {
            if (Version.hasHoneycomb()) {
                // Running on Honeycomb or newer, so wrap in a standard BitmapDrawable
                drawable = new BitmapDrawable(resources, bitmap);
            } else {
                // Running on Gingerbread or older, so wrap in a RecyclingBitmapDrawable
                // which will recycle automatically
                drawable = new RecyclingBitmapDrawable(resources, bitmap);
            }
        }
        return drawable;
    }

    /**
     * Get from memory cache.
     */
    public BitmapDrawable getBitmapFromMemCache(ImageTask imageTask) {
        BitmapDrawable memValue = null;

        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(imageTask.getIdentityKey());
        }

        return memValue;
    }

    /**
     * Get from memory cache.
     */
    public BitmapDrawable getBitmapFromMemCache(String identityKey) {
        BitmapDrawable memValue = null;

        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(identityKey);
        }

        return memValue;
    }

    public void addBitmapToMemCache(String key, BitmapDrawable drawable) {

        // If the API level is lower than 11, do not use memory cache
        if (key == null || drawable == null || !Version.hasHoneycomb()) {
            return;
        }

        // Add to memory cache
        if (mMemoryCache != null) {
            mMemoryCache.set(key, drawable);
        }
    }

    public void cancelTask(ImageTask task) {
    }

    /**
     * Get Bitmap. If not exist in file cache, will try to re-use the file cache of the other sizes.
     * <p/>
     * If no file cache can be used, download then save to file.
     */
    public Bitmap fetchBitmapData(ImageLoader imageLoader, ImageTask imageTask, ImageResizer imageResizer) {
        Bitmap bitmap = null;
        if (mDiskCacheProvider != null) {
            FileInputStream inputStream = null;

            String fileCacheKey = imageTask.getFileCacheKey();
            ImageReuseInfo reuseInfo = imageTask.getImageReuseInfo();

            if (Debug.DEBUG_IMAGE) {
                Log.e("fetch--key--",fileCacheKey);
                Log.d(TAG, String.format(MSG_FETCH_BEGIN, imageTask));
                Log.d(TAG, String.format(MSG_FETCH_BEGIN_IDENTITY_KEY, imageTask, imageTask.getIdentityKey()));
                Log.d(TAG, String.format(MSG_FETCH_BEGIN_FILE_CACHE_KEY, imageTask, fileCacheKey));
                Log.d(TAG, String.format(MSG_FETCH_BEGIN_ORIGIN_URL, imageTask, imageTask.getOriginUrl()));
                Log.d(TAG, String.format(MSG_FETCH_BEGIN_IDENTITY_URL, imageTask, imageTask.getIdentityUrl()));
            }

            // read from file cache
            inputStream = mDiskCacheProvider.getInputStream(fileCacheKey);

            // try to reuse
            if (inputStream == null) {
                if (reuseInfo != null && reuseInfo.getReuseSizeList() != null && reuseInfo.getReuseSizeList().length > 0) {
                    if (Debug.DEBUG_IMAGE) {
                        Log.d(TAG, String.format(MSG_FETCH_TRY_REUSE, imageTask));
                    }

                    final String[] sizeKeyList = reuseInfo.getReuseSizeList();
                    for (int i = 0; i < sizeKeyList.length; i++) {
                        String size = sizeKeyList[i];
                        final String key = imageTask.generateFileCacheKeyForReuse(size);
                        inputStream = mDiskCacheProvider.getInputStream(key);

                        if (inputStream != null) {
                            if (Debug.DEBUG_IMAGE) {
                                Log.d(TAG, String.format(MSG_FETCH_REUSE_SUCCESS, imageTask, size));
                            }
                            break;
                        } else {
                            if (Debug.DEBUG_IMAGE) {
                                Log.d(TAG, String.format(MSG_FETCH_REUSE_FAIL, imageTask, size, key));
                            }
                        }
                    }
                }
            } else {
                if (Debug.DEBUG_IMAGE) {
                    Log.e("fetch--file--",fileCacheKey);
                    Log.d(TAG, String.format(MSG_FETCH_HIT_DISK_CACHE, imageTask));
                }
            }

            if (null == inputStream) {
                inputStream = customFetchDiskCache(imageLoader, imageTask, imageResizer);
            }

            if (imageTask.getStatistics() != null) {
                imageTask.getStatistics().afterFileCache(inputStream != null);
            }

            // We've got nothing from file cache
            if (inputStream == null) {
                int retry = ImageLoader.mRetry;
                String url = imageResizer.getRemoteUrl(imageTask);
                if (url.contains(ImageTask.BLUR_FALG)) {
                    url = url.replace(ImageTask.BLUR_FALG, "");
                }
                if (Debug.DEBUG_IMAGE) {
                    Log.d(TAG, String.format(MSG_FETCH_DOWNLOAD, imageTask, url));
                }
                while (retry > 0 && inputStream == null) {
                    if (retry == 1 && ImageLoader.mRetry > 1) {
                        String host = Uri.parse(url).getHost();
                        String ip = ImageLoader.mIpList.get(host);
                        if (!TextUtils.isEmpty(ip)) {
                            url = url.replace(host, ip);
                        }
                    }
                    inputStream = mDiskCacheProvider.downloadAndGetInputStream(fileCacheKey, url);
                    if (retry > 0 && null == inputStream) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    retry--;
                }
                // do {
                // inputStream = mDiskCacheProvider.downloadAndGetInputStream(fileCacheKey, url);
                // if (retry > 0 && null == inputStream) {
                // try {
                // Thread.sleep(100);
                // } catch (InterruptedException e) {
                // Log.e(TAG, "thread sleep exception %s", e);
                // }
                // }
                // } while (retry-- > 0 && inputStream == null);
                if (imageTask.getStatistics() != null) {
                    imageTask.getStatistics().afterDownload();
                }
                if (inputStream == null) {
                    imageTask.setError(ImageTask.ERROR_NETWORK);
                    if (Debug.DEBUG_IMAGE) {
                        CLog.e(TAG, "%s download fail: %s %s", imageTask, fileCacheKey, url);
                    }
                }
            }
            if (inputStream != null) {
                try {
                    bitmap = decodeSampledBitmapFromDescriptor(inputStream.getFD(), imageTask, imageResizer);
                    if (bitmap == null) {
                        imageTask.setError(ImageTask.ERROR_BAD_FORMAT);
                        CLog.e(TAG, "%s decode bitmap fail, bad format. %s, %s", imageTask, fileCacheKey,
                                imageResizer.getRemoteUrl(imageTask));
                    }
                    // add by panrq begin, 2015-02-05, close inputStream
                    inputStream.close();
                    // add by panrq end, 2015-02-05, close inputStream
                } catch (IOException e) {
                    CLog.e(TAG, "%s decode bitmap fail, may be out of memory. %s, %s", imageTask, fileCacheKey,
                            imageResizer.getRemoteUrl(imageTask));
                    e.printStackTrace();
                }
            } else {
                CLog.e(TAG, "%s fetch bitmap fail. %s, %s", imageTask, fileCacheKey, imageResizer.getRemoteUrl(imageTask));
            }
        }
        return bitmap;
    }

    /**
     * 自定义实现缓存计划
     * 
     * @param imageLoader
     * @param imageTask
     * @param imageResizer
     * @return
     */
    public FileInputStream customFetchDiskCache(ImageLoader imageLoader, ImageTask imageTask, ImageResizer imageResizer) {
        return null;
    }

    protected Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, ImageTask imageTask, ImageResizer imageResizer,
            int agree) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Config.RGB_565;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        if (agree % 180 == 90) {
            imageTask.setBitmapOriginSize(options.outHeight, options.outWidth);
        } else {
            imageTask.setBitmapOriginSize(options.outWidth, options.outHeight);
        }

        // Calculate inSampleSize
        options.inSampleSize = imageResizer.getInSampleSize(imageTask);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        if (Debug.DEBUG_IMAGE) {
            Log.d(TAG, String.format(MSG_DECODE, imageTask, imageTask.getBitmapOriginSize().x, imageTask.getBitmapOriginSize().y,
                    options.inSampleSize));
        }

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        if (agree % 360 != 0) {
            bitmap = ImageUtil.rotaingImageView(agree, bitmap);
        }

        return bitmap;
    }

    protected Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, ImageTask imageTask, ImageResizer imageResizer) {

        return decodeSampledBitmapFromDescriptor(fileDescriptor, imageTask, imageResizer, 0);
    }

    private Bitmap decodeSampledBitmapFromInputStream(InputStream stream, ImageTask imageTask, ImageResizer imageResizer) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // try to decode height and width from InputStream
        BitmapFactory.decodeStream(stream, null, options);

        imageTask.setBitmapOriginSize(options.outWidth, options.outHeight);

        // Calculate inSampleSize
        options.inSampleSize = imageResizer.getInSampleSize(imageTask);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        if (Debug.DEBUG_IMAGE) {
            Log.d(TAG, String.format(MSG_DECODE, imageTask, imageTask.getBitmapOriginSize().x, imageTask.getBitmapOriginSize().y,
                    options.inSampleSize));
        }

        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);

        return bitmap;
    }

    public void flushFileCache() {
        if (null != mDiskCacheProvider) {
            mDiskCacheProvider.flushDiskCacheAsync();
        }
    }

    /**
     * clear the memory cache
     */
    public void clearMemoryCache() {
        if (mMemoryCache != null) {
            mMemoryCache.clear();
        }
    }

    /**
     * clear the disk cache
     */
    public void clearDiskCache() {
        if (null != mDiskCacheProvider) {
            try {
                mDiskCacheProvider.getDiskCache().clear();
            } catch (IOException e) {
            }
        }
    }

    public long getMemoryCacheMaxSpace() {
        return mMemoryCache.getMaxSize();
    }

    public long getMemoryCacheUsedSpace() {
        return mMemoryCache.getUsedSpace();
    }

    /**
     * return the file cache path
     * 
     * @return
     */
    public String getFileCachePath() {
        if (null != mDiskCacheProvider) {
            return mDiskCacheProvider.getDiskCache().getDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * get the used space
     * 
     * @return
     */
    public long getFileCacheUsedSpace() {
        return null != mDiskCacheProvider ? mDiskCacheProvider.getDiskCache().getSize() : 0;
    }

    public long getFileCacheMaxSpace() {
        if (null != mDiskCacheProvider) {
            return mDiskCacheProvider.getDiskCache().getCapacity();
        }
        return 0;
    }

    public ImageDiskCacheProvider getmDiskCacheProvider() {
        return mDiskCacheProvider;
    }
}
