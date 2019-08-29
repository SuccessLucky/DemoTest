package in.srain.cube.image.impl;

import android.graphics.BitmapFactory;
import in.srain.cube.image.ImageTask;
import in.srain.cube.image.iface.ImageResizer;

/**
 * A default implementation of {@link ImageResizer}
 *
 * @author http://www.liaohuqiu.net
 */
public class DefaultImageResizer implements ImageResizer {

    private static DefaultImageResizer sInstance;

    public static DefaultImageResizer getInstance() {
        if (sInstance == null) {
            sInstance = new DefaultImageResizer();
        }
        return sInstance;
    }

    @Override
    public int getInSampleSize(ImageTask imageTask) {
        int size = calculateInSampleSize(imageTask.getBitmapOriginSize().x, imageTask.getBitmapOriginSize().y, imageTask.getRequestSize().x, imageTask.getRequestSize().y);
        return size;
    }

    @Override
    public String getRemoteUrl(ImageTask imageTask) {
        return imageTask.getRemoteUrl();
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding bitmaps using the decode* methods from {@link BitmapFactory}.
     * <p/>
     * This implementation calculates the closest inSampleSize that is a times of 2 and will result in the final decoded bitmap having a width and height equal to or larger than the requested width and height.
     */
    public static int calculateInSampleSize(int originWidth, int originHeight, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
       
        if (reqHeight <= 0 || reqHeight <= 0 || (originWidth < reqWidth && originHeight < reqHeight) ) {
            return inSampleSize;
        }
        if (originWidth < reqWidth && originHeight < reqHeight) {
            return 1;
        } if (reqWidth > reqHeight && originHeight > originWidth) {
            reqWidth = reqHeight * originWidth / originHeight;
        } else if (reqWidth < reqHeight && originHeight < originWidth) {
            reqHeight = reqWidth * originHeight / originWidth;
        }

        if (originHeight > reqHeight || originWidth > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) originHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) originWidth / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        
//        android.util.Log.d("", "@@@@@@ originWidth x originHeight = " + originWidth + " x " + originHeight);
//        android.util.Log.d("", "@@@@@@ reqWidth x reqHeight = " + reqWidth + " x " + reqHeight);
//        android.util.Log.d("", "DefaultImageResizer inSampleSize = " + inSampleSize + ", originWidth x originHeight = "
//                 + originWidth + "x" + originHeight + ", reqWidth x reqHeight = " + reqWidth + " x " + reqHeight);
        return inSampleSize;
    }
}