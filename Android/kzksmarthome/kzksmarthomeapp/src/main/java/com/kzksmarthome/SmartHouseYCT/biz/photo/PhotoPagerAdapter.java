package com.kzksmarthome.SmartHouseYCT.biz.photo;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;

import java.util.ArrayList;

import in.srain.cube.app.lifecycle.IComponentContainer;
import in.srain.cube.image.ImageLoader;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.ReusablePhotoView;

/**
 * 
 * @Description:大图浏览适配器
 * @Copyright: Copyright (c) 2015
 * @version: 1.0.0.0
 * @author: len、jack
 * @createDate 2015-6-25
 *
 */
public class PhotoPagerAdapter extends PagerAdapter implements OnPhotoTapListener {

    private ArrayList<PhotoData> photoDataList = null;
    private String[] mImagesUrls = null;
    private ReusablePhotoView[] mViews;
    private Activity mActivity;
    private ImageLoader mImageLoader;

    public PhotoPagerAdapter(Activity act, IComponentContainer container, String[] images) {
        if(photoDataList != null){
            photoDataList = null;
        }
        mActivity = act;
        mImagesUrls = images;
        mViews = new ReusablePhotoView[4];
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader(container, R.drawable.default_loading_large);
    }

    public PhotoPagerAdapter(Activity act, IComponentContainer container, ArrayList<PhotoData> photoDataList) {
        if (mImagesUrls != null) {
            mImagesUrls = null;
        }
        mActivity = act;
        this.photoDataList = photoDataList;
        mViews = new ReusablePhotoView[4];
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader(container, R.drawable.default_loading_large);
    }

    @Override
    public int getCount() {
        if(mImagesUrls!=null){
            return mImagesUrls.length;
        }else if(photoDataList!=null){
            return photoDataList.size();
        }
       return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int index = position % 4;
        ReusablePhotoView photoView = mViews[index];
        if (photoView == null) {
            photoView = new ReusablePhotoView(container.getContext());
            // photoView.setBackgroundColor(Color.BLACK);
            photoView.setOnPhotoTapListener(this);
            photoView.setScaleType(ScaleType.FIT_CENTER);
            photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            mViews[index] = photoView;
        }
        String imageUrl = null;
        if (mImagesUrls != null) {
            imageUrl = mImagesUrls[position];
        } else if (photoDataList != null) {
            imageUrl = photoDataList.get(position).photoUrl;
        }
        //photoView.loadImage(mImageLoader,imageUrl);
        Glide
                .with(mActivity)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.default_img)
                .crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);
        container.removeView(photoView);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        mActivity.finish();
        // mActivity.overridePendingTransition(0, R.anim.fade_out);
    }
}
