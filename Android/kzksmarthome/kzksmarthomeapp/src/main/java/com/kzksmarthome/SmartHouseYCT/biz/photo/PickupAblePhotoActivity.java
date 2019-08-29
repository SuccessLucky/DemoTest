package com.kzksmarthome.SmartHouseYCT.biz.photo;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseActivity;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageReuseInfo;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.ReusablePhotoView;

public class PickupAblePhotoActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final String PHOTO_LIST_KEY = "photoList";
    public static final String SELECTED_KEY = "selectedList";
    public static final String GO_SUBMIT_KEY = "goSubmit";
    public static final String MAX_KEY = "max";
    public static final String INDEX_KEY = "index";
    public static final String MODE_TYPE = "mode_type";
    public static final int MODE_TYPE_LIST = 1;
    public static final int MODE_TYPE_CURSOR = 2;
    public static ImageReuseInfo previewPhotoReuseInfo = new ImageReuseInfo("local_photo_preview", null);

    @BindView(R.id.viewPager)
    HackyViewPager mViewPager;
    @BindView(R.id.btn_sure_add)
    Button mSureBtn;
    @BindView(R.id.check_btn)
    CheckBox mBoxBtn;
    @BindView(R.id.title_ll)
    RelativeLayout mTitleBar;
    @BindView(R.id.bottom_bar)
    RelativeLayout mBottomBar;

    private int mNodeType;
    private ArrayList<String> mPhotoList;
    private ArrayList<String> mSelectedList;
    private Cursor mCursor;
    private int mIndex;
    private int mMax;
    private SamplePagerAdapter mAdapter;

    @OnClick(R.id.btn_sure_add)
    void sureAdd() {
        if (mSelectedList != null) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(PickupAblePhotoActivity.SELECTED_KEY, mSelectedList);
            intent.putExtra(PickupAblePhotoActivity.GO_SUBMIT_KEY, true);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    @OnClick(R.id.top_back_btn)
    void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_choose_photo);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void finish() {
        if (mSelectedList != null) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(PickupAblePhotoActivity.SELECTED_KEY, mSelectedList);
            intent.putExtra(PickupAblePhotoActivity.GO_SUBMIT_KEY, false);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    private void initViews() {
        Intent i = getIntent();
        if (i == null) {
            finish();
            return;
        }

        mNodeType = i.getIntExtra(MODE_TYPE, -1);
        mMax = i.getIntExtra(MAX_KEY, 0);
        mIndex = i.getIntExtra(INDEX_KEY, 0);
        if (mNodeType == MODE_TYPE_LIST) {
            mPhotoList = i.getStringArrayListExtra(PickupAblePhotoActivity.PHOTO_LIST_KEY);
            mSelectedList = i.getStringArrayListExtra(PickupAblePhotoActivity.SELECTED_KEY);
            if (null == mPhotoList || mPhotoList.size() == 0) {
                finish();
                return;
            }
        } else if (mNodeType == MODE_TYPE_CURSOR) {
            getAlbumsCursor();
            mSelectedList = i.getStringArrayListExtra(PickupAblePhotoActivity.SELECTED_KEY);
        } else {
            finish();
            return;
        }
        if (null == mSelectedList) {
            mSelectedList = new ArrayList<String>();
        }
        if (mNodeType == MODE_TYPE_LIST) {
            mAdapter = new SamplePagerAdapter();
            mViewPager.setAdapter(mAdapter);
            mViewPager.setOnPageChangeListener(this);
            currentItem();
        }
    }

    private void currentItem() {
        if (mIndex <= 0) {
            mIndex = 0;
            onPageSelected(0);
        }
        updateSureBtn();
        mViewPager.setCurrentItem(mIndex);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int pos, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(final int pos) {
        String path = mAdapter.getPathByPos(pos);
        if (null != path) {
            mBoxBtn.setOnCheckedChangeListener(null);
            mBoxBtn.setChecked(mSelectedList.contains(path));
            mBoxBtn.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
    }

    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

            String path = mAdapter.getPathByPos(mViewPager.getCurrentItem());
            if (null != path) {
                if (arg1) {
                    if (!mSelectedList.contains(path)) {
                        if (mSelectedList.size() >= mMax) {
                            SmartHomeApp.showToast(R.string.upload_max_num_tip, mMax);
                            mBoxBtn.setOnCheckedChangeListener(null);
                            mBoxBtn.setChecked(false);
                            mBoxBtn.setOnCheckedChangeListener(mOnCheckedChangeListener);
                        } else {
                            mSelectedList.add(path);
                        }
                    }
                } else {
                    if (mSelectedList.contains(path)) {
                        mSelectedList.remove(path);
                    }
                }
            }
            updateSureBtn();
        }
    };

    /**
     * 获取选中数量
     * 
     * @return
     */
    private int getSelectedCount() {
        int count = 0;
        if (null != mSelectedList) {
            count = mSelectedList.size();
        }
        return count;
    }

    /**
     * 更新确认添加按钮，选中数量
     */
    private void updateSureBtn() {
        int count = getSelectedCount();
        mSureBtn.setText(getString(R.string.upload_add_sure, count));
        if (count > 0) {
            mSureBtn.setEnabled(true);
        } else {
            mSureBtn.setEnabled(false);
        }
    }

    class SamplePagerAdapter extends PagerAdapter implements OnPhotoTapListener {

        private ReusablePhotoView[] mViews;
        private ImageLoader mImageLoader;
        AlphaAnimation mToVisibleAnim, mToGoneAnim;

        private SamplePagerAdapter() {
            mViews = new ReusablePhotoView[4];
          /*  mImageLoader = SmartHomeApp.getInstance().getImageLoader(PickupAblePhotoActivity.this, R.drawable.default_loading_large,
                    FeedBackAddFragment.getImageProviderForAlbums(), AlbumsImageTaskExecutor.getInstance());*/
            mToVisibleAnim = new AlphaAnimation(0, 1);
            mToVisibleAnim.setDuration(300);
            mToVisibleAnim.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    mTitleBar.setVisibility(View.VISIBLE);
                    mBottomBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }
            });

            mToGoneAnim = new AlphaAnimation(1, 0);
            mToGoneAnim.setDuration(300);
            mToGoneAnim.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mTitleBar.setVisibility(View.GONE);
                    mBottomBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getCount() {
            if (mNodeType == MODE_TYPE_LIST) {
                return mPhotoList.size();
            } else if (null != mCursor) {
                return mCursor.getCount();
            } else {
                return 0;
            }
        }

        public String getPathByPos(int position) {
            if (mNodeType == MODE_TYPE_LIST) {
                return mPhotoList.get(position);
            } else if (null != mCursor) {
                mCursor.moveToPosition(position);
                return mCursor.getString(1);
            } else {
                return null;
            }
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            int index = position % 4;
            ReusablePhotoView photoView = mViews[index];
            if (photoView == null) {
                photoView = new ReusablePhotoView(container.getContext());
                // photoView.setBackgroundColor(Color.BLACK);
                photoView.setOnPhotoTapListener(this);
                photoView.setScaleType(ScaleType.FIT_CENTER);
                photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mViews[index] = photoView;
            }
            String path = "";
            if (mNodeType == MODE_TYPE_LIST) {
                path = mPhotoList.get(position);
            } else if (null != mCursor) {
                mCursor.moveToPosition(position);
                path = mCursor.getString(1);
            }
            //photoView.loadImage(mImageLoader, path, previewPhotoReuseInfo);
            Glide
                    .with(PickupAblePhotoActivity.this)
                    .load(path)
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
        public void destroyItem(ViewGroup container, int position, Object object) {
            // container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onPhotoTap(View view, float x, float y) {
            if (mTitleBar.getVisibility() == View.GONE) {
                mTitleBar.startAnimation(mToVisibleAnim);
                mBottomBar.startAnimation(mToVisibleAnim);
            } else {
                mTitleBar.startAnimation(mToGoneAnim);
                mBottomBar.startAnimation(mToGoneAnim);
            }
        }

    }

    /**
     * 获取相册数据库Cursor
     * 
     * @return
     */
    private void getAlbumsCursor() {
        ForegroundTaskExecutor.executeTask(new Runnable() {

            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PickupAblePhotoActivity.this.getContentResolver();
                String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
                final Cursor cursor = mContentResolver.query(imageUri, projection, MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?", new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mCursor = cursor;
                        mAdapter = new SamplePagerAdapter();
                        mViewPager.setAdapter(mAdapter);
                        mViewPager.setOnPageChangeListener(PickupAblePhotoActivity.this);
                        currentItem();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCursor) {
            mCursor.close();
        }
    }
}
