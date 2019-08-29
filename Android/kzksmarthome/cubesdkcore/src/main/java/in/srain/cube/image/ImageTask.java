package in.srain.cube.image;

import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import in.srain.cube.image.iface.ImageLoadHandler;
import in.srain.cube.util.CLog;
import in.srain.cube.util.Debug;
import in.srain.cube.util.Encrypt;

import java.lang.ref.WeakReference;

/**
 * A wrapper of the related information used in loading a bitmap
 * 
 * @author http://www.liaohuqiu.net
 */
public class ImageTask {

    protected static final String LOG_TAG = Debug.DEBUG_IMAGE_LOG_TAG_TASK;
    private static final Object sPoolSync = new Object();
    private static ImageTask sTop;
    private static int sPoolSize = 0;
    private static final int MAX_POOL_SIZE = 20;
    private static boolean USE_POOL = false;

    private static int sId = 0;

    private final static String SIZE_SP = "_";

    // 0000 0111
    private static final int ERROR_CODE_MASK = 0x07;

    // error code, max 0x07
    public final static int ERROR_NETWORK = 0x01;
    public final static int ERROR_BAD_FORMAT = 0x02;
    /**
     * bits: 1 error-code 2 error-code 3 error-code 4 loading 5 pre-load
     */
    private final static int STATUS_LOADING = 0x01 << 3;
    private final static int STATUS_PRE_LOAD = 0x02 << 3;

    private int mFlag = 0;
    protected int mId = 0;

    // the origin request url for the image.
    protected String mOriginUrl;

    // In some situations, we may store the same image in some different servers. So the same image will related to some different urls.
    private String mIdentityUrl;

    // The key related to the image this ImageTask is requesting.
    private String mIdentityKey;

    // cache for toString();
    private String mStr;

    protected Point mRequestSize = new Point();
    protected Point mBitmapOriginSize = new Point();
    protected ImageReuseInfo mReuseInfo;

    public ImageViewHolder mFirstImageViewHolder;
    protected ImageTaskStatistics mImageTaskStatistics;

    ImageTask next;

    private boolean mHasRecycled = false;

    /**
     * 照片审核标志
     */
    public static final String BLUR_FALG = "_blur_flag";

    public boolean hasLoadThumb() {
        return mHasLoadThumb;
    }

    public void setHasLoadThumb(boolean hasLoadThumb) {
        this.mHasLoadThumb = hasLoadThumb;
    }

    private boolean mHasLoadThumb = false;

    protected void clearForRecycle() {
        mHasRecycled = true;
        mFlag = 0;

        mOriginUrl = null;
        mIdentityUrl = null;
        mIdentityKey = null;
        mStr = null;
        mRequestSize.set(0, 0);
        mBitmapOriginSize.set(0, 0);

        mReuseInfo = null;
        mFirstImageViewHolder = null;
        mImageTaskStatistics = null;
        mHasLoadThumb = false;
    }

    public static ImageTask obtain() {
        if (!USE_POOL) {
            return null;
        }
        // pop top, make top.next as top
        synchronized (sPoolSync) {
            if (sTop != null) {
                ImageTask m = sTop;
                sTop = m.next;
                m.next = null;
                sPoolSize--;
                m.mHasRecycled = false;
                if (Debug.DEBUG_IMAGE) {
                    CLog.d(LOG_TAG, "%s, obtain reused, pool remain: %d", m, sPoolSize);
                }
                return m;
            }
        }
        return null;
    }

    public synchronized void tryToRecycle() {
        if (!USE_POOL) {
            return;
        }
        clearForRecycle();

        // mark top as the next of current, then push current as pop
        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sTop;
                sTop = this;
                sPoolSize++;
                if (Debug.DEBUG_IMAGE) {
                    CLog.d(LOG_TAG, "%s is put to recycle poll, pool size: %d", this, sPoolSize);
                } else {
                    if (Debug.DEBUG_IMAGE) {
                        CLog.d(LOG_TAG, "%s is not recycled, the poll is full: %d", this, sPoolSize);
                    }
                }
            }
        }
    }

    public ImageTask renew() {
        if (Debug.DEBUG_IMAGE) {
            int lastId = mId;
            mId = ++sId;
            CLog.d(LOG_TAG, "%s, renew: %s => %s", this, lastId, mId);
        } else {
            mId = ++sId;
        }
        mStr = null;
        if (ImagePerformanceStatistics.sample(mId)) {
            mImageTaskStatistics = new ImageTaskStatistics();
        }
        return this;
    }

    public ImageTask setOriginUrl(String originUrl) {
        mOriginUrl = originUrl;
        return this;
    }

    public ImageTask setRequestSize(int requestWidth, int requestHeight) {
        mRequestSize.set(requestWidth, requestHeight);
        return this;
    }

    public ImageTask setReuseInfo(ImageReuseInfo imageReuseInfo) {
        mReuseInfo = imageReuseInfo;
        return this;
    }

    /**
     * For accessing the identity url
     * 
     * @return
     */
    public String getIdentityUrl() {
        if (null == mIdentityUrl) {
            mIdentityUrl = generateIdentityUrl(mOriginUrl);
        }
        return mIdentityUrl;
    }

    /**
     * In some situations, we may store the same image in some different servers. So the same image will related to some different urls.
     * <p>
     * Generate the identity url according your situation.
     * <p>
     * {@link #mIdentityUrl}
     * 
     * @return
     */
    protected String generateIdentityUrl(String originUrl) {
        return originUrl;

    }

    /**
     * Generate the identity key.
     * <p>
     * This key should be related to the unique image this task is requesting: the size, the remote url.
     * 
     * @return
     */
    protected String generateIdentityKey() {
        if (mReuseInfo == null) {
            return joinSizeInfoToKey(getIdentityUrl(), mRequestSize.x, mRequestSize.y);
        } else {
            return joinSizeTagToKey(getIdentityUrl(), mReuseInfo.getIdentitySize());
        }
    }

    public boolean isPreLoad() {
        return (mFlag & STATUS_PRE_LOAD) == STATUS_PRE_LOAD;
    }

    public void setIsPreLoad() {
        mFlag = mFlag | STATUS_PRE_LOAD;
    }

    public boolean isLoading() {
        return (mFlag & STATUS_LOADING) != 0;
    }

    /**
     * Check the given url is loading.
     * 
     * @param url
     * @return Identify the given url, if same to {@link #mIdentityUrl} return true.
     */
    public boolean isLoadingThisUrl(String url) {
        return getIdentityUrl().equals(generateIdentityUrl(url));
    }

    /**
     * Bind ImageView with ImageTask
     * 
     * @param imageView
     */
    public void addImageView(CubeImageView imageView) {
        if (null == imageView) {
            return;
        }
        if (null == mFirstImageViewHolder) {
            mFirstImageViewHolder = new ImageViewHolder(imageView);
            return;
        }

        ImageViewHolder holder = mFirstImageViewHolder;
        for (;; holder = holder.mNext) {
            if (holder.contains(imageView)) {
                return;
            }
            if (holder.mNext == null) {
                break;
            }
        }

        ImageViewHolder newHolder = new ImageViewHolder(imageView);
        newHolder.mPrev = holder;
        holder.mNext = newHolder;
    }

    /**
     * Remove the ImageView from ImageTask
     * 
     * @param imageView
     */
    public void removeImageView(CubeImageView imageView) {

        if (null == imageView || null == mFirstImageViewHolder) {
            return;
        }

        ImageViewHolder holder = mFirstImageViewHolder;

        do {
            if (holder.contains(imageView)) {

                // Make sure entry is right.
                if (holder == mFirstImageViewHolder) {
                    mFirstImageViewHolder = holder.mNext;
                }
                if (null != holder.mNext) {
                    holder.mNext.mPrev = holder.mPrev;
                }
                if (null != holder.mPrev) {
                    holder.mPrev.mNext = holder.mNext;
                }
            }

        } while ((holder = holder.mNext) != null);
    }

    /**
     * Check if this ImageTask has any related ImageViews.
     * 
     * @return
     */
    public synchronized boolean stillHasRelatedImageView() {
        if (null == mFirstImageViewHolder || mFirstImageViewHolder.getImageView() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * When loading from network
     * 
     * @param handler
     */
    public void onLoading(ImageLoadHandler handler) {
        mFlag = mFlag | STATUS_LOADING;

        if (null == handler) {
            return;
        }

        if (mFirstImageViewHolder == null) {
            handler.onLoading(this, null);
        } else {
            ImageViewHolder holder = mFirstImageViewHolder;
            do {
                final CubeImageView imageView = holder.getImageView();
                if (null != imageView) {
                    handler.onLoading(this, imageView);
                }
            } while ((holder = holder.mNext) != null);
        }
    }

    /**
     * Will be called when begin load image data from dish or network
     * 
     * @param drawable
     */
    public void onLoadTaskFinish(BitmapDrawable drawable, ImageLoadHandler handler) {

        mFlag &= ~STATUS_LOADING;

        if (null == handler) {
            return;
        }
        int errorCode = mFlag & ERROR_CODE_MASK;
        if (errorCode > 0) {
            onLoadError(errorCode, handler);
            return;
        }

        if (null != mImageTaskStatistics) {
            mImageTaskStatistics.showBegin();
        }
        if (mFirstImageViewHolder == null) {
            handler.onLoadFinish(this, null, drawable);
        } else {
            ImageViewHolder holder = mFirstImageViewHolder;
            do {
                final CubeImageView imageView = holder.getImageView();
                if (null != imageView) {
                    imageView.onLoadFinish();
                    handler.onLoadFinish(this, imageView, drawable);
                }
            } while ((holder = holder.mNext) != null);
        }
        if (null != mImageTaskStatistics) {
            mImageTaskStatistics.showComplete(ImageProvider.getBitmapSize(drawable));
        }
    }

    public void onLoadTaskCancel() {
    }

    public void setError(int errorCode) {
        if (errorCode > ERROR_CODE_MASK) {
            throw new IllegalArgumentException("error code undefined.");
        }

        // clear old error flag
        mFlag = (mFlag & ~ERROR_CODE_MASK);

        // set current error flag
        mFlag |= errorCode;
    }

    private void onLoadError(int reason, ImageLoadHandler handler) {
        if (mFirstImageViewHolder == null) {
            handler.onLoadError(this, null, reason);
        } else {
            ImageViewHolder holder = mFirstImageViewHolder;
            do {
                final CubeImageView imageView = holder.getImageView();
                if (null != imageView) {
                    imageView.onLoadFinish();
                    handler.onLoadError(this, imageView, reason);
                }
            } while ((holder = holder.mNext) != null);
        }
    }

    /**
     * If you have a thumbnail web service which can return multiple size image according the url,
     * <p>
     * you can implements this method to return the specified url according the request size.
     * 
     * @return
     */
    public String getRemoteUrl() {
        return mOriginUrl;
    }

    /**
     * Return the origin request url
     * 
     * @return
     */
    public String getOriginUrl() {
        return mOriginUrl;
    }

    public void setBitmapOriginSize(int width, int height) {
        mBitmapOriginSize.set(width, height);
    }

    public Point getBitmapOriginSize() {
        return mBitmapOriginSize;
    }

    public Point getRequestSize() {
        return mRequestSize;
    }

    /**
     * Return the key which identifies this Image Wrapper object.
     */
    public String getIdentityKey() {
        if (mIdentityKey == null) {
            mIdentityKey = generateIdentityKey();
        }
        return mIdentityKey;
    }

    /**
     * Join the key and the size information.
     * 
     * @param key
     * @param w
     * @param h
     * @return "$key" + "_" + "$w" + "_" + "$h"
     */
    public static String joinSizeInfoToKey(String key, int w, int h) {
        if (w > 0 && h != Integer.MAX_VALUE && h > 0 && h != Integer.MAX_VALUE) {
            return new StringBuilder(key).append(SIZE_SP).append(w).append(SIZE_SP).append(h).toString();
        }
        return key;
    }

    /**
     * Join the tag with the key.
     * 
     * @param key
     * @param tag
     * @return "$key" + "_" + "$tag"
     */
    public static String joinSizeTagToKey(String key, String tag) {
        return new StringBuilder(key).append(SIZE_SP).append(tag).toString();
    }

    /**
     * Return the cache key for file cache.
     * 
     * @return the cache key for file cache.
     */
    public String getFileCacheKey() {
        return Encrypt.md5(getIdentityKey());
    }

    /**
     * @param sizeKey
     * @return
     */
    public String generateFileCacheKeyForReuse(String sizeKey) {
        return Encrypt.md5(joinSizeTagToKey(getIdentityUrl(), sizeKey));
    }

    public static String generateFileCacheKeyForReuse(String url, String sizeKey) {
        return Encrypt.md5(joinSizeTagToKey(url, sizeKey));
    }

    public ImageReuseInfo getImageReuseInfo() {
        return mReuseInfo;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof ImageTask) {
            return ((ImageTask) object).getIdentityKey().equals(getIdentityKey());
        }
        return false;
    }

    @Override
    public String toString() {
        if (mStr == null) {
            mStr = String.format("[ImageTask@%s %s %sx%s %s]", Integer.toHexString(hashCode()), mId, mRequestSize.x, mRequestSize.y,
                    mHasRecycled);
        }
        return mStr;
    }

    public ImageTaskStatistics getStatistics() {
        return mImageTaskStatistics;
    }

    /**
     * A tiny and light linked-list like container to hold all the ImageViews related to the ImageTask.
     */
    protected static class ImageViewHolder {
        private WeakReference<CubeImageView> mImageViewRef;
        private ImageViewHolder mNext;
        private ImageViewHolder mPrev;

        public ImageViewHolder(CubeImageView imageView) {
            mImageViewRef = new WeakReference<CubeImageView>(imageView);
        }

        boolean contains(CubeImageView imageView) {
            return mImageViewRef != null && imageView == mImageViewRef.get();
        }

        CubeImageView getImageView() {
            if (null == mImageViewRef) {
                return null;
            }
            return mImageViewRef.get();
        }
    }
}
