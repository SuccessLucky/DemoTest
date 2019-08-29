package in.srain.cube.image;

import android.content.Context;
import android.text.TextUtils;
import in.srain.cube.cache.DiskFileUtils;
import in.srain.cube.image.iface.*;
import in.srain.cube.image.impl.*;

/**
 * Create an {@link ImageLoader}. Here defines two {@link ImageLoader}, one is the default, the other one is the stable. You can use the
 * stable one to load the images which will not change frequently.
 * 
 * @author http://www.liaohuqiu.net
 */
public class ImageLoaderFactory {

    public static int DEFAULT_FILE_CACHE_SIZE_IN_KB = 10 * 1024; // 10M;
    private static String DEFAULT_FILE_CACHE_DIR = "cube-image";
    private static String STABLE_FILE_CACHE_DIR = "cube-image-stable";

    private static ImageProvider sDefaultImageProvider;
    private static ImageProvider sStableImageProvider;

    private static ImageResizer sDefaultImageResizer;
    private static ImageTaskExecutor sDefaultImageTaskExecutor;
    private static ImageLoadHandler sDefaultImageLoadHandler;
    private static ImageMemoryCache sDefaultImageMemoryCache;

    public static int getDefaultMemoryCacheSizeInKB() {
        float percent = 0.2f;
        int memoryCacheSizeInKB = Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
        return memoryCacheSizeInKB;
    }

    /**
     * customize cache
     * 
     * @param context
     * @param memoryCacheSizeInKB How many memory should use. Will not be greater than 50% of free memory
     * @param defaultDiskCacheSizeInKB Default disk cache size.
     */
    public static void customizeCache(Context context, int memoryCacheSizeInKB, int defaultDiskCacheSizeInKB) {
        customizeCache(context, memoryCacheSizeInKB, defaultDiskCacheSizeInKB, 0);
    }

    /**
     * customize cache
     * 
     * @param context
     * @param memoryCacheSizeInKB How many memory should use. Will not be greater than 50% of free memory
     * @param defaultDiskCachePath Default image cache path. Absolute path or a relative path under cache directory. External cache first.
     *            If not specified, using {@link #DEFAULT_FILE_CACHE_DIR} under cache directory.
     * @param defaultDiskCacheSizeInKB Default disk cache size.
     */
    public static void customizeCache(Context context, int memoryCacheSizeInKB, String defaultDiskCachePath, int defaultDiskCacheSizeInKB) {
        customizeCache(context, memoryCacheSizeInKB, defaultDiskCachePath, defaultDiskCacheSizeInKB, null, 0);
    }

    /**
     * customize cache
     * 
     * @param context
     * @param memoryCacheSizeInKB How many memory should use. Will not be greater than 50% of free memory
     * @param defaultDiskCacheSizeInKB Default disk cache size.
     * @param stableDiskCacheSizeInKB Stable disk cache size.
     */
    public static void customizeCache(Context context, int memoryCacheSizeInKB, int defaultDiskCacheSizeInKB, int stableDiskCacheSizeInKB) {
        customizeCache(context, memoryCacheSizeInKB, null, defaultDiskCacheSizeInKB, null, stableDiskCacheSizeInKB);
    }

    /**
     * customize cache
     * 
     * @param context
     * @param memoryCacheSizeInKB How many memory should use. Will not be greater than 50% of free memory
     * @param defaultDiskCachePath Default image cache path. Absolute path or a relative path under cache directory. External cache first.
     *            If not specified, using {@link #DEFAULT_FILE_CACHE_DIR} under cache directory.
     * @param defaultDiskCacheSizeInKB Default disk cache size.
     * @param stableDiskCachePath Path for stable cache directory. Default is {@link #STABLE_FILE_CACHE_DIR}
     * @param stableDiskCacheSizeInKB Stable disk cache size.
     */
    public static void customizeCache(Context context, int memoryCacheSizeInKB, String defaultDiskCachePath, int defaultDiskCacheSizeInKB,
            String stableDiskCachePath, int stableDiskCacheSizeInKB) {

        // init memory cache first
        if (memoryCacheSizeInKB > 0) {
            int maxCacheSizeInKB = Math.round(0.5f * Runtime.getRuntime().maxMemory() / 1024);
            memoryCacheSizeInKB = Math.min(memoryCacheSizeInKB, maxCacheSizeInKB);
            sDefaultImageMemoryCache = new DefaultMemoryCache(memoryCacheSizeInKB);
        }

        if (defaultDiskCacheSizeInKB > 0 && !TextUtils.isEmpty(defaultDiskCachePath)) {
            ImageDiskCacheProvider imageFileProvider = getImageFileProvider(context, defaultDiskCachePath, defaultDiskCacheSizeInKB,
                    DEFAULT_FILE_CACHE_DIR);
            if (imageFileProvider != null) {
                sDefaultImageProvider = new ImageProviderExpand(context, getDefaultImageMemoryCache(), imageFileProvider);
            }
        }

        if (stableDiskCacheSizeInKB > 0 && !TextUtils.isEmpty(stableDiskCachePath)) {
            ImageDiskCacheProvider imageFileProvider = getImageFileProvider(context, stableDiskCachePath, stableDiskCacheSizeInKB,
                    STABLE_FILE_CACHE_DIR);
            if (imageFileProvider != null) {
                sStableImageProvider = new ImageProviderExpand(context, getDefaultImageMemoryCache(), imageFileProvider);
            }
        }
    }

    private static ImageDiskCacheProvider getImageFileProvider(Context context, String path, int sizeInKB, String fallbackCachePath) {
        if (sizeInKB <= 0) {
            sizeInKB = DEFAULT_FILE_CACHE_SIZE_IN_KB;
        }

        DiskFileUtils.CacheDirInfo dirInfo = DiskFileUtils.getDiskCacheDir(context, path, sizeInKB, fallbackCachePath);

        ImageDiskCacheProvider imageFileProvider = ImageDiskCacheProvider.createLru(dirInfo.realSize, dirInfo.path);

        if (imageFileProvider != null) {
            imageFileProvider.openDiskCacheAsync();
        }
        return imageFileProvider;
    }

    public static ImageLoader createStableImageLoader(Context context) {
        return createInner(context, getStableImageProvider(context), sDefaultImageLoadHandler);
    }

    public static ImageLoader createStableImageLoader(Context context, ImageLoadHandler imageLoadHandler) {
        return createInner(context, getStableImageProvider(context), imageLoadHandler);
    }

    public static ImageLoader create(Context context) {
        return createInner(context, getDefaultImageProvider(context), sDefaultImageLoadHandler);
    }

    public static ImageLoader create(Context context, ImageTaskExecutor imageTaskExecutor) {
        if (null == imageTaskExecutor) {
            return createInner(context, getDefaultImageProvider(context), sDefaultImageTaskExecutor, sDefaultImageLoadHandler);
        } else {
            return createInner(context, getDefaultImageProvider(context), imageTaskExecutor, sDefaultImageLoadHandler);
        }
    }

    public static ImageLoader create(Context context, ImageProvider imageProvider, ImageTaskExecutor imageTaskExecutor) {
        if (null == imageProvider) {
            imageProvider = getDefaultImageProvider(context);
        }
        if (null == imageTaskExecutor) {
            return createInner(context, imageProvider, sDefaultImageTaskExecutor, sDefaultImageLoadHandler);
        } else {
            return createInner(context, imageProvider, imageTaskExecutor, sDefaultImageLoadHandler);
        }
    }

    public static ImageLoader create(Context context, ImageLoadHandler imageLoadHandler) {
        return createInner(context, getDefaultImageProvider(context), imageLoadHandler);
    }

    private static ImageLoader createInner(Context context, ImageProvider imageProvider, ImageLoadHandler imageLoadHandler) {
        return create(context, imageProvider, sDefaultImageTaskExecutor, sDefaultImageResizer, imageLoadHandler);
    }

    private static ImageLoader createInner(Context context, ImageProvider imageProvider, ImageTaskExecutor imageTaskExecutor,
            ImageLoadHandler imageLoadHandler) {
        return create(context, imageProvider, imageTaskExecutor, sDefaultImageResizer, imageLoadHandler);
    }


    private static ImageLoader create(Context context, ImageProvider imageProvider, ImageTaskExecutor imageTaskExecutor,
            ImageResizer imageResizer, ImageLoadHandler imageLoadHandler) {

        if (imageProvider == null) {
            imageProvider = getDefaultImageProvider(context);
        }

        if (imageTaskExecutor == null) {
            imageTaskExecutor = DefaultImageTaskExecutor.getInstance();
        }

        if (imageResizer == null) {
            imageResizer = DefaultImageResizer.getInstance();
        }

        if (imageLoadHandler == null) {
            imageLoadHandler = new DefaultImageLoadHandler(context);
        }
        ImageLoader imageLoader = new ImageLoader(context, imageProvider, imageTaskExecutor, imageResizer, imageLoadHandler);
        return imageLoader;
    }

    public static void setDefaultImageResizer(ImageResizer imageResizer) {
        sDefaultImageResizer = imageResizer;
    }

    public static void setDefaultImageTaskExecutor(ImageTaskExecutor imageTaskExecutor) {
        sDefaultImageTaskExecutor = imageTaskExecutor;
    }

    public static void setDefaultImageLoadHandler(ImageLoadHandler imageLoadHandler) {
        sDefaultImageLoadHandler = imageLoadHandler;
    }

    // public static void setDefaultImageProvider(ImageProvider imageProvider) {
    // sDefaultImageProvider = imageProvider;
    // }

    public static void setStableImageProvider(ImageProvider imageProvider) {
        sStableImageProvider = imageProvider;
    }

    public static ImageProvider getDefaultImageProvider(Context context) {
        if (null == sDefaultImageProvider) {
            ImageDiskCacheProvider imageFileProvider = getImageFileProvider(context, null, 0, DEFAULT_FILE_CACHE_DIR);
            sDefaultImageProvider = new ImageProviderExpand(context, getDefaultImageMemoryCache(), imageFileProvider);
        }
        return sDefaultImageProvider;
    }

    public static ImageProvider getStableImageProvider(Context context) {
        if (null == sStableImageProvider) {
            ImageDiskCacheProvider imageFileProvider = getImageFileProvider(context, null, 0, STABLE_FILE_CACHE_DIR);
            sStableImageProvider = new ImageProviderExpand(context, getDefaultImageMemoryCache(), imageFileProvider);
        }
        return sStableImageProvider;
    }

    private static ImageMemoryCache getDefaultImageMemoryCache() {
        if (sDefaultImageMemoryCache == null) {
            sDefaultImageMemoryCache = new DefaultMemoryCache(getDefaultMemoryCacheSizeInKB());
        }
        return sDefaultImageMemoryCache;
    }

    public static String getDefaultFileCacheDir() {
        return DEFAULT_FILE_CACHE_DIR;
    }
}
