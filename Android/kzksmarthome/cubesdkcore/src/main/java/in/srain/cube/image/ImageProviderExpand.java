package in.srain.cube.image;

import in.srain.cube.image.iface.ImageMemoryCache;
import in.srain.cube.image.iface.ImageResizer;
import in.srain.cube.util.CLog;
import in.srain.cube.util.Debug;
import in.srain.cube.util.ImageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

public class ImageProviderExpand extends ImageProvider {

    private static final String REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static Pattern sPattern = Pattern.compile(REGEX);

    public ImageProviderExpand(Context context, ImageMemoryCache memoryCache, ImageDiskCacheProvider fileProvider) {
        super(context, memoryCache, fileProvider);
    }

    @Override
    public Bitmap fetchBitmapData(ImageLoader imageLoader, ImageTask imageTask, ImageResizer imageResizer) {

        if (!isUrl(imageTask.mOriginUrl)) {
            return getLocalImage(imageTask, imageResizer);
        } else {
            return super.fetchBitmapData(imageLoader, imageTask, imageResizer);
        }
    }

    @Override
    public FileInputStream customFetchDiskCache(ImageLoader imageLoader, ImageTask imageTask, ImageResizer imageResizer) {

        String url = imageTask.getIdentityUrl();
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        FileInputStream inputStream = null;
        int lastPoint = url.lastIndexOf(".");// like: aa_180x180.jpg
        int lastLine = url.lastIndexOf("_");
        ImageReuseInfo reuseInfo = imageTask.getImageReuseInfo();
        if (lastPoint > 0 && lastLine > 0 && lastLine < lastPoint) {
            url = url.substring(0, lastLine) + url.substring(lastPoint, url.length());
            final String key = ImageTask.generateFileCacheKeyForReuse(url, reuseInfo.getIdentitySize());
            inputStream = mDiskCacheProvider.getInputStream(key);

            if (inputStream != null) {
                if (Debug.DEBUG_IMAGE) {
                    Log.d(TAG, String.format("%s custom reuse size: %s, %s", imageTask, url, key));
                }
            } else {
                if (Debug.DEBUG_IMAGE) {
                    Log.d(TAG, String.format("%s custom reuse fail: %s, %s", imageTask, url, key));
                }
            }
        }
        return inputStream;
    }

    /**
     * 加载本地图片
     * 
     * @param imageTask
     * @param imageResizer
     * @return
     */
    public Bitmap getLocalImage(ImageTask imageTask, ImageResizer imageResizer) {
        Bitmap bitmap = null;
        File file = new File(imageTask.mOriginUrl);
        FileInputStream inputStream = null;
        try {
            int agree = ImageUtil.readPictureDegree(imageTask.mOriginUrl);
            CLog.d(TAG, "image[%s] agree[%s]", imageTask.mOriginUrl, agree);
            inputStream = new FileInputStream(file);
            bitmap = decodeSampledBitmapFromDescriptor(inputStream.getFD(), imageTask, imageResizer, agree);
        } catch (IOException e) {
            CLog.e(TAG, "%s decode bitmap fail. %s, %s, %s", imageTask, imageTask.mOriginUrl, imageResizer.getRemoteUrl(imageTask), e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    CLog.e(TAG, "%s close fail. %s, %s, %s", imageTask, imageTask.mOriginUrl, imageResizer.getRemoteUrl(imageTask), e);
                }
            }
        }
        return bitmap;
    }

    public static boolean isUrl(String url) {
        Matcher matcher = sPattern.matcher(url);
        return matcher.matches();
    }
}
