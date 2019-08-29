package in.srain.cube.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.util.ArrayMap;
import android.view.View;

import com.benz.fastblur.Fast2Blur;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import in.srain.cube.app.CubeFragment;
import in.srain.cube.app.lifecycle.LifeCycleComponent;
import in.srain.cube.app.lifecycle.LifeCycleComponentManager;
import in.srain.cube.concurrent.SimpleTask;
import in.srain.cube.image.iface.ImageLoadHandler;
import in.srain.cube.image.iface.ImageLoadProgressHandler;
import in.srain.cube.image.iface.ImageResizer;
import in.srain.cube.image.iface.ImageTaskExecutor;
import in.srain.cube.util.CLog;
import in.srain.cube.util.Debug;

/**
 * @author http://www.liaohuqiu.net
 */
public class ImageLoader implements LifeCycleComponent {

    // for LoadImageTask
    private static final Object sPoolSync = new Object();
    private static LoadImageTask sTopLoadImageTask;
    private static int sPoolSize = 0;
    private static final int MAX_POOL_SIZE = 0;

    private static final String MSG_ATTACK_TO_RUNNING_TASK = "%s attach to running: %s";

    private static final String MSG_TASK_DO_IN_BACKGROUND = "%s, %s LoadImageTask.doInBackground";
    private static final String MSG_TASK_WAITING = "%s, %s LoadImageTask.waiting";
    private static final String MSG_TASK_FINISH = "%s, %s LoadImageTask.onFinish, mExitTasksEarly? %s";
    private static final String MSG_TASK_AFTER_fetchBitmapData = "%s, %s LoadImageTask.afterFetchBitmapData, canceled? %s";
    private static final String MSG_TASK_CANCEL = "%s, %s LoadImageTask.onCancel";
    private static final String MSG_TASK_RECYCLE = "%s, %s LoadImageTask.removeAndRecycle";
    private static final String MSG_HIT_CACHE = "%s hit cache %s %s";


    protected static final boolean DEBUG = Debug.DEBUG_IMAGE;
    protected static final String LOG_TAG = Debug.DEBUG_IMAGE_LOG_TAG;

    protected ImageTaskExecutor mImageTaskExecutor;
    protected ImageResizer mImageResizer;
    protected ImageProvider mImageProvider;
    protected ImageLoadHandler mImageLoadHandler;
    protected ImageLoadProgressHandler mLoadImageLoadProgressHandler;

    protected boolean mPauseWork = false;
    protected boolean mExitTasksEarly = false;

    private final Object mPauseWorkLock = new Object();
    private ConcurrentHashMap<String, LoadImageTask> mLoadWorkList;
    protected Context mContext;

    protected Resources mResources;

    public static final int TASK_ORDER_FIRST_IN_FIRST_OUT = 1;
    public static final int TASK_ORDER_LAST_IN_FIRST_OUT = 2;

    static int mRetry = 2;
    static Map<String, String> mIpList = new ArrayMap<String, String>();

    public static void setRetryCount(int retryCount) {
        if (retryCount <= 0) {
            return;
        }
        mRetry = retryCount;
    }

    public static void setIpList(Map<String, String> ipList) {
        mIpList = ipList;
    }

    public static Map<String, String> getIpList() {
        return mIpList;
    }

    public ImageLoader(Context context, ImageProvider imageProvider, ImageTaskExecutor imageTaskExecutor, ImageResizer imageResizer, ImageLoadHandler imageLoadHandler) {
        mContext = context;
        mResources = context.getResources();
        mImageProvider = imageProvider;
        mImageTaskExecutor = imageTaskExecutor;
        mImageResizer = imageResizer;
        mImageLoadHandler = imageLoadHandler;
        mLoadWorkList = new ConcurrentHashMap<String, LoadImageTask>();
    }

    public void setImageLoadHandler(ImageLoadHandler imageLoadHandler) {
        mImageLoadHandler = imageLoadHandler;
    }

    public ImageLoadHandler getImageLoadHandler() {
        return mImageLoadHandler;
    }

    public void setImageResizer(ImageResizer resizer) {
        mImageResizer = resizer;
    }

    public ImageResizer getImageResizer() {
        return mImageResizer;
    }

    public ImageProvider getImageProvider() {
        return mImageProvider;
    }

    /**
     * Load the image in advance.
     */
    public void preLoadImages(String[] urls) {
        int len = urls.length;
        for (int i = 0; i < len; i++) {
            final ImageTask imageTask = createImageTask(urls[i], 0, 0, null);
            imageTask.setIsPreLoad();
            addImageTask(imageTask, null);
        }
    }

    /**
     * Create an ImageTask.
     * You can override this method to return a customized {@link ImageTask}.
     *
     * @param url
     * @param requestWidth
     * @param requestHeight
     * @param imageReuseInfo
     * @return
     */
    public ImageTask createImageTask(String url, int requestWidth, int requestHeight, ImageReuseInfo imageReuseInfo) {
        ImageTask imageTask = ImageTask.obtain();
        if (imageTask == null) {
            imageTask = new ImageTask();
        }
        imageTask.renew().setOriginUrl(url).setRequestSize(requestWidth, requestHeight).setReuseInfo(imageReuseInfo);
        return imageTask;
    }

    /**
     * Detach the ImageView from the ImageTask.
     *
     * @param imageTask
     * @param imageView
     */
    public void detachImageViewFromImageTask(ImageTask imageTask, CubeImageView imageView) {
        imageTask.removeImageView(imageView);
        if (imageTask.isLoading()) {
            if (!imageTask.isPreLoad() && !imageTask.stillHasRelatedImageView()) {
                LoadImageTask task = mLoadWorkList.get(imageTask.getIdentityKey());
                if (task != null) {
                    task.cancel();
                }
                if (DEBUG) {
                    CLog.d(LOG_TAG, "%s previous work is cancelled.", imageTask);
                }
            }
        }
        if (!imageTask.stillHasRelatedImageView()) {
            imageTask.tryToRecycle();
        }
    }

    /**
     * Add the ImageTask into loading list.
     *
     * @param imageTask
     * @param imageView
     */
    public void addImageTask(ImageTask imageTask, CubeImageView imageView) {
        LoadImageTask runningTask = mLoadWorkList.get(imageTask.getIdentityKey());
        if (runningTask != null) {
            if (imageView != null) {
                if (DEBUG) {
                    CLog.d(LOG_TAG, MSG_ATTACK_TO_RUNNING_TASK, imageTask, runningTask.getImageTask());
                }
                runningTask.getImageTask().addImageView(imageView);
                runningTask.getImageTask().onLoading(mImageLoadHandler);
            }
            return;
        } else {
            imageTask.addImageView(imageView);
        }

        imageTask.onLoading(mImageLoadHandler);

        LoadImageTask loadImageTask = createLoadImageTask(imageTask);
        mLoadWorkList.put(imageTask.getIdentityKey(), loadImageTask);
        mImageTaskExecutor.execute(loadImageTask);
    }

    /**
     * Check weather this imageTask has cache Drawable data.
     */
    public boolean queryCache(ImageTask imageTask, CubeImageView imageView) {
        if (null == mImageProvider) {
            return false;
        }
        BitmapDrawable drawable = mImageProvider.getBitmapFromMemCache(imageTask);

        if (imageTask.getStatistics() != null) {
            imageTask.getStatistics().afterMemoryCache(drawable != null);
        }
        if (drawable == null) {
            return false;
        }

        if (DEBUG) {
            CLog.d(LOG_TAG, MSG_HIT_CACHE, imageTask, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            if (drawable.getIntrinsicWidth() == 270) {
                CLog.d(LOG_TAG, MSG_HIT_CACHE, imageTask, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
        }
        imageTask.addImageView(imageView);
        imageTask.onLoadTaskFinish(drawable, mImageLoadHandler);
        return true;
    }

    /**
     * Check weather this imageTask has cache Drawable data.
     */
    public boolean queryThumbCache(String thumbIdentityKey, ImageTask imageTask, CubeImageView imageView) {
        if (null == mImageProvider) {
            return false;
        }
        BitmapDrawable drawable = mImageProvider.getBitmapFromMemCache(thumbIdentityKey);

//        if (imageTask.getStatistics() != null) {
//            imageTask.getStatistics().afterMemoryCache(drawable != null);
//        }
        if (drawable == null) {
            return false;
        }

//        if (DEBUG) {
//            CLog.d(LOG_TAG, MSG_HIT_CACHE, imageTask, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            if (drawable.getIntrinsicWidth() == 270) {
//                CLog.d(LOG_TAG, MSG_HIT_CACHE, imageTask, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            }
//        }
        mImageLoadHandler.onLoadThumbFinish(null, imageView, drawable);
        imageTask.setHasLoadThumb(true);
        return true;
    }

    /**
     * set task executed order: {@link @TASK_ORDER_LAST_IN_FIRST_OUT} or {@link #TASK_ORDER_FIRST_IN_FIRST_OUT}
     *
     * @param order
     */
    @SuppressWarnings({"unused"})
    public void setTaskOrder(int order) {
        if (null != mImageTaskExecutor) {
            mImageTaskExecutor.setTaskOrder(order);
        }
    }

    /**
     * flush un-cached image to disk
     */
    public void flushFileCache() {
        if (mImageProvider != null) {
            mImageProvider.flushFileCache();
        }
    }

    private LoadImageTask createLoadImageTask(ImageTask imageTask) {
        // pop top, make top.next as top
        synchronized (sPoolSync) {
            if (sTopLoadImageTask != null) {
                LoadImageTask m = sTopLoadImageTask;
                m.mNextImageTask = null;
                m.renew(this, imageTask);

                sTopLoadImageTask = m.mNextImageTask;
                sPoolSize--;
                return m;
            }
        }
        return new LoadImageTask().renew(this, imageTask);
    }

    /**
     * Inner class to process the image loading task in background threads.
     * <p>
     * Memory required:
     * Shadow heap size: 24(Parent class, {@link SimpleTask}) + 4 * 3 = 36. Align to 40 bytes.
     * Retained heap size: 40 + 24(AtomicInteger introduced by {@link SimpleTask} = 64 bytes.
     *
     * @author http://www.liaohuqiu.net
     */
    private static class LoadImageTask extends SimpleTask {

        private ImageTask mImageTask;
        private BitmapDrawable mDrawable;
        private LoadImageTask mNextImageTask;
        private ImageLoader mImageLoader;

        public ImageTask getImageTask() {
            return mImageTask;
        }

        public LoadImageTask renew(ImageLoader imageLoader, ImageTask imageTask) {
            mImageLoader = imageLoader;
            mImageTask = imageTask;
            restart();
            return this;
        }

        @Override
        public void doInBackground() {
            if (DEBUG) {
                CLog.d(LOG_TAG, MSG_TASK_DO_IN_BACKGROUND, this, mImageTask);
            }

            if (mImageTask.getStatistics() != null) {
                mImageTask.getStatistics().beginLoad();
            }
            Bitmap bitmap = null;
            // Wait here if work is paused and the task is not cancelled
            synchronized (mImageLoader.mPauseWorkLock) {
                while (mImageLoader.mPauseWork && !isCancelled()) {
                    try {
                        if (DEBUG) {
                            CLog.d(LOG_TAG, MSG_TASK_WAITING, this, mImageTask);
                        }
                        mImageLoader.mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

            // If this task has not been cancelled by another
            // thread and the ImageView that was originally bound to this task is still bound back
            // to this task and our "exit early" flag is not set then try and fetch the bitmap from
            // the cache
            if (!isCancelled() && !mImageLoader.mExitTasksEarly && (mImageTask.isPreLoad() || mImageTask.stillHasRelatedImageView())) {
                try {
                    bitmap = mImageLoader.mImageProvider.fetchBitmapData(mImageLoader, mImageTask, mImageLoader.mImageResizer);
                    if (DEBUG) {
                        CLog.d(LOG_TAG, MSG_TASK_AFTER_fetchBitmapData, this, mImageTask, isCancelled());
                    }
                    if (mImageTask != null && mImageTask.getStatistics() != null) {
                        mImageTask.getStatistics().afterDecode();
                    }

                    //实现毛玻璃
                    if (mImageTask.getRemoteUrl().contains(ImageTask.BLUR_FALG)  && bitmap != null) {
                        mDrawable = doBlur(bitmap, mImageTask.mFirstImageViewHolder.getImageView());
                    } else {
                        mDrawable = mImageLoader.mImageProvider.createBitmapDrawable(mImageLoader.mResources, bitmap);
                    }
                    mImageLoader.mImageProvider.addBitmapToMemCache(mImageTask.getIdentityKey(), mDrawable);
                    if (mImageTask.getStatistics() != null) {
                        mImageTask.getStatistics().afterCreateBitmapDrawable();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * jni实现高斯模糊
         *
         * @param bkg
         * @param view
         * @return
         */
        private BitmapDrawable doBlur(Bitmap bkg, View view) {
            long startMs = System.currentTimeMillis();
            int width = bkg.getWidth();
            int height = bkg.getHeight();
            Bitmap overlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.translate(-view.getLeft(), -view.getTop());
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0, 0, paint);
            try {
                Fast2Blur.build(overlay, 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            BitmapDrawable bitmapDrawable = new BitmapDrawable(mImageLoader.mResources, overlay);
            CLog.w("Blur--JNI--time:",""+(System.currentTimeMillis()-startMs));
            return bitmapDrawable;
        }


        /**
         * 生成毛玻璃图片
         *
         * @param bkg
         * @param view
         * @return
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        private BitmapDrawable blur(Bitmap bkg, View view) {
            long startMs = System.currentTimeMillis();

            float radius = 10;

            Bitmap overlay = Bitmap.createBitmap(180, 180, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(overlay);

            canvas.translate(-view.getLeft(), -view.getTop());
            //canvas.drawBitmap(bkg, 0, 0, null);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0, 0, paint);

            RenderScript rs = RenderScript.create(mImageLoader.mContext);

            Allocation overlayAlloc = Allocation.createFromBitmap(
                    rs, overlay);

            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                    rs, overlayAlloc.getElement());

            blur.setInput(overlayAlloc);

            blur.setRadius(radius);

            blur.forEach(overlayAlloc);

            overlayAlloc.copyTo(overlay);

            BitmapDrawable bitmapDrawable = new BitmapDrawable(mImageLoader.mResources, overlay);
            //overlay.recycle();
            rs.destroy();
            CLog.w("Blur--RenderScript--time:",""+(System.currentTimeMillis()-startMs));
            return bitmapDrawable;
        }


        @Override
        public void onFinish(boolean canceled) {
            if (DEBUG) {
                CLog.d(LOG_TAG, MSG_TASK_FINISH, this, mImageTask, mImageLoader.mExitTasksEarly);
            }
            if (mImageLoader.mExitTasksEarly) {
                return;
            }

            if (!isCancelled() && !mImageLoader.mExitTasksEarly) {
                mImageTask.onLoadTaskFinish(mDrawable, mImageLoader.mImageLoadHandler);
            }

            mImageLoader.mLoadWorkList.remove(mImageTask.getIdentityKey());
        }

        @Override
        public void onCancel() {
            if (DEBUG) {
                CLog.d(LOG_TAG, MSG_TASK_CANCEL, this, mImageTask);
            }
            mImageLoader.getImageProvider().cancelTask(mImageTask);
            mImageTask.onLoadTaskCancel();
            mImageLoader.mLoadWorkList.remove(mImageTask.getIdentityKey());
        }

        private void removeAndRecycle() {
            if (DEBUG) {
                CLog.d(LOG_TAG, MSG_TASK_RECYCLE, this, mImageTask);
            }
            // unlink
            mImageLoader = null;
            mImageTask = null;
            mDrawable = null;

            // mark top as the next of current, then push current as pop
            synchronized (sPoolSync) {
                if (sPoolSize < MAX_POOL_SIZE) {
                    mNextImageTask = sTopLoadImageTask;
                    sTopLoadImageTask = this;
                    sPoolSize++;
                }
            }
        }

        @Override
        public String toString() {
            return "[LoadImageTask" + '@' + Integer.toHexString(hashCode()) + ']';
        }
    }

    private void setPause(boolean pause) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pause;
            if (!pause) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    /**
     * Temporarily hand up work, you can call this when the view is scrolling.
     */
    public void pauseWork() {
        mExitTasksEarly = false;
        setPause(true);
        if (DEBUG) {
            CLog.d(LOG_TAG, "work_status: pauseWork %s", this);
        }
    }

    /**
     * Resume the work
     */
    public void resumeWork() {
        mExitTasksEarly = false;
        setPause(false);
        if (DEBUG) {
            CLog.d(LOG_TAG, "work_status: resumeWork %s", this);
        }
    }

    /**
     * Recover the from the work list
     */
    public void recoverWork() {
        if (DEBUG) {
            CLog.d(LOG_TAG, "work_status: recoverWork %s", this);
        }
        mExitTasksEarly = false;
        setPause(false);
        Iterator<Entry<String, LoadImageTask>> it = (Iterator<Entry<String, LoadImageTask>>) mLoadWorkList.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, LoadImageTask> item = it.next();
            LoadImageTask task = item.getValue();
            task.restart();
            mImageTaskExecutor.execute(task);
        }
    }

    /**
     * Drop all the work, and leave it in the work list.
     */
    public void stopWork() {
        if (DEBUG) {
            CLog.d(LOG_TAG, "work_status: stopWork %s", this);
        }
        mExitTasksEarly = true;
        setPause(false);

        flushFileCache();
    }

    /**
     * Drop all the work, clear the work list.
     */
    public void destroy() {
        if (DEBUG) {
            CLog.d(LOG_TAG, "work_status: destroy %s", this);
        }
        mExitTasksEarly = true;
        setPause(false);

        Iterator<Entry<String, LoadImageTask>> it = (Iterator<Entry<String, LoadImageTask>>) mLoadWorkList.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, LoadImageTask> item = it.next();
            final LoadImageTask task = item.getValue();
            it.remove();
            if (task != null) {
                task.cancel();
            }
        }
        mLoadWorkList.clear();
    }

    /**
     * The UI becomes partially invisible.
     * like {@link android.app.Activity#onPause}
     */
    @Override
    public void onBecomesPartiallyInvisible() {
        pauseWork();
    }

    /**
     * The UI becomes visible from partially invisible.
     * like {@link android.app.Activity#onResume}
     */
    @Override
    public void onBecomesVisible() {
        resumeWork();
    }

    /**
     * The UI becomes totally invisible.
     * like {@link android.app.Activity#onStop}
     */
    @Override
    public void onBecomesTotallyInvisible() {
        stopWork();
    }

    /**
     * The UI becomes visible from totally invisible.
     * like {@link android.app.Activity#onRestart}
     */
    @Override
    public void onBecomesVisibleFromTotallyInvisible() {
        recoverWork();
    }

    /**
     * like {@link android.app.Activity#onDestroy}
     */
    @Override
    public void onDestroy() {
        destroy();
    }

    /**
     * try to attach to {@link in.srain.cube.app.lifecycle.IComponentContainer}
     */
    public ImageLoader tryToAttachToContainer(Object object) {
        tryToAttachToContainer(object, true);
        return this;
    }

    /**
     * try to attach to {@link in.srain.cube.app.lifecycle.IComponentContainer}
     */
    public ImageLoader tryToAttachToContainer(Object object, boolean throwEx) {
        LifeCycleComponentManager.tryAddComponentToContainer(this, object, throwEx);
        return this;
    }

    /**
     * LiefCycle phase will be same to CubeFragment, an will be processed automatically.
     *
     * @param fragment
     * @return
     */
    public ImageLoader attachToCubeFragment(CubeFragment fragment) {
        if (fragment != null) {
            LifeCycleComponentManager.tryAddComponentToContainer(this, fragment);
        }
        return this;
    }
}
