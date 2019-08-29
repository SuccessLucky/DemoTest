package com.kzksmarthome.SmartHouseYCT.biz.photo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.image.ImageLoader;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.ReusablePhotoView;

@Deprecated
public class PickupAblePhotoViewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final String BUNDLE_PARCELABLE_PHOTO_LIST_KEY = "photoList";

    @BindView(R.id.viewPager)
    HackyViewPager mViewPager;

    @BindView(R.id.btn_sure_add)
    Button mSureBtn;

    @BindView(R.id.check_btn)
    CheckBox mBoxBtn;

    @BindView(R.id.title_ll)
    RelativeLayout mTitle_ll;

    @BindView(R.id.bottom_bar)
    RelativeLayout mBottom_ll;

    List<PhotoInfo> mUrlList;

    @OnClick(R.id.btn_sure_add)
    void sureAdd() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(
                PickupAblePhotoViewActivity.BUNDLE_PARCELABLE_PHOTO_LIST_KEY,
                (ArrayList<PhotoInfo>) mUrlList);
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    @OnClick(R.id.top_back_btn)
    void back() {
        this.finish();
    }

    private int mSelectNum;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_choose_photo);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        Intent i = getIntent();
        if (i == null) {
            return;
        }
        mUrlList = i
                .getParcelableArrayListExtra(PickupAblePhotoViewActivity.BUNDLE_PARCELABLE_PHOTO_LIST_KEY);
        mIndex = i.getIntExtra("index", 0);
        mSelectNum = getSelectNum(mUrlList);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setOnPageChangeListener(this);
        if (mIndex < 0 || mIndex >= mUrlList.size()) {
            mIndex = 0;
        }
        mViewPager.setCurrentItem(mIndex);
        if (mUrlList != null) {
            mBoxBtn.setTag(mIndex);
            if (mUrlList.get(mIndex).mIsChecked == PhotoInfo.CHECKED) {
                mBoxBtn.setChecked(true);
            } else {
                mBoxBtn.setChecked(false);
            }
        }
        mBoxBtn.setOnCheckedChangeListener(mOnCheckedChangeListener);
        updateSureBtn();
        /*
         * mBoxBtn.setOnCheckedChangeListener(mOnCheckedChangeListener); if (mUrlList != null) { if (mUrlList.get(mIndex).mIsChecked ==
         * PhotoInfo.CHECKED) { mBoxBtn.setChecked(true); } } setTag(mBoxBtn , mUrlList.get(mIndex).mUrl.hashCode());
         * mBoxBtn.setOnCheckedChangeListener(new CheckedChangeListener(mIndex));
         * 
         * mSureBtn.setText(getString(R.string.choose_pic, mSelectNum)); if (mSelectNum == 0) { mSureBtn.setEnabled(false); }
         */
    }

    private int getSelectNum(List<PhotoInfo> mUrlList) {
        int selectNum = 0;
        for (Iterator iterator = mUrlList.iterator(); iterator.hasNext();) {
            PhotoInfo photoInfo = (PhotoInfo) iterator.next();
            if (photoInfo.mIsChecked == PhotoInfo.CHECKED) {
                selectNum++;
            }
        }
        return selectNum;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int pos, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    /*
     * private void setTag(final CheckBox v, int position) { Object t = v.getTag(); if (t == null) { v.setTag(new TAG(position)); } else {
     * TAG tag = (TAG) t; tag.pos = position; //tag.key = key; } v.setOnCheckedChangeListener(new OnCheckedChangeListener() { TAG tag =
     * (TAG) v.getTag();
     * 
     * @Override public void onCheckedChanged(CompoundButton arg0, boolean arg1) { if (getSelectNum(mUrlList) < 12) { if
     * (mUrlList.get(tag.pos).mUrl.hashCode() == tag.key) { if (mUrlList.get(tag.pos).mIsChecked == PhotoInfo.CHECKED) {
     * mUrlList.get(tag.pos).mIsChecked = PhotoInfo.UNCHECK; } else { mUrlList.get(tag.pos).mIsChecked = PhotoInfo.CHECKED; }
     * mSureBtn.setText(getString(R.string.choose_pic, getSelectNum(mUrlList))); } //GjjEventBus.getInstance().post(tag); } else {
     * GjjApp.showToast("最多只能选择12张"); }
     * 
     * } });
     * 
     * }
     * 
     * private class TAG { public TAG(int position) { this.pos = position; //this.key = key; }
     * 
     * private int pos; // private int key; }
     */

    @Override
    public void onPageSelected(final int pos) {
        // mPageinfo.setText(pos + 1 + "/" + mImageUrl.length);
        mBoxBtn.setOnCheckedChangeListener(null);
        mBoxBtn.setTag(pos);
        // setTag(mBoxBtn, mUrlList.get(pos).mUrl.hashCode());
        if (mUrlList.get(pos).mIsChecked == PhotoInfo.CHECKED) {
            mBoxBtn.setChecked(true);
        } else {
            mBoxBtn.setChecked(false);
        }
        mBoxBtn.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

            if (null == mBoxBtn.getTag()) {
                return;
            }

            int pos = (Integer) mBoxBtn.getTag();
            if (!arg1) {
                if (mUrlList.get(pos).mIsChecked == PhotoInfo.CHECKED) {
                    mUrlList.get(pos).mIsChecked = PhotoInfo.UNCHECK;
                    mSelectNum--;
                    updateSureBtn();
                }
            } else {
                //if (mSelectNum < FeedBackAddAdapter.MAX_SELECT) {
                    if (mUrlList.get(pos).mIsChecked == PhotoInfo.UNCHECK) {
                        mUrlList.get(pos).mIsChecked = PhotoInfo.CHECKED;
                        mSelectNum++;
                        updateSureBtn();
                   // }
                } else {
                    mBoxBtn.setChecked(false);
                   /* SmartHomeApp.showToast(getString(R.string.upload_max_num_tip,
                            FeedBackAddAdapter.MAX_SELECT));*/
                }
            }
        }

    };

    private void updateSureBtn() {
        mSureBtn.setText(getString(R.string.upload_add_sure, mSelectNum));
        if (mSelectNum > 0) {
            mSureBtn.setEnabled(true);
        } else {
            mSureBtn.setEnabled(false);
        }
    }

    class SamplePagerAdapter extends PagerAdapter implements OnPhotoTapListener {

        private ReusablePhotoView[] mViews;
        private ImageLoader mImageLoader;

        private SamplePagerAdapter() {
            mViews = new ReusablePhotoView[4];
            mImageLoader = SmartHomeApp.getInstance().getImageLoader(PickupAblePhotoViewActivity.this, R.drawable.default_loading_large);
        }

        @Override
        public int getCount() {
            return mUrlList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            int index = position % 4;
            ReusablePhotoView photoView = mViews[index];
            if (photoView == null) {
                photoView = new ReusablePhotoView(container.getContext());
                photoView.setBackgroundColor(Color.BLACK);
                photoView.setOnPhotoTapListener(this);
                photoView.setScaleType(ScaleType.FIT_CENTER);
                photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mViews[index] = photoView;
            }
            photoView.loadImage(mImageLoader, mUrlList.get(position).mUrl);
            container.removeView(photoView);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onPhotoTap(View view, float x, float y) {
            if (mTitle_ll.getVisibility() == View.GONE) {
                AlphaAnimation toVisible = new AlphaAnimation(0, 1);
                toVisible.setDuration(300);
                toVisible.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        mTitle_ll.setVisibility(View.VISIBLE);
                        mBottom_ll.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                mTitle_ll.startAnimation(toVisible);
                mBottom_ll.startAnimation(toVisible);
            } else {
                AlphaAnimation toVisible = new AlphaAnimation(1, 0);
                toVisible.setDuration(300);
                toVisible.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mTitle_ll.setVisibility(View.GONE);
                        mBottom_ll.setVisibility(View.GONE);
                    }
                });
                mTitle_ll.startAnimation(toVisible);
                mBottom_ll.startAnimation(toVisible);
            }
        }

    }

}
