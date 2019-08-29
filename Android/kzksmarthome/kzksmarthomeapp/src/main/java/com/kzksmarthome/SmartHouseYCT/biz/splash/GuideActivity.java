package com.kzksmarthome.SmartHouseYCT.biz.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.login.LoginFragment;
import com.kzksmarthome.SmartHouseYCT.biz.photo.HackyViewPager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * 
 * @Description: 新用户引导页
 * @Copyright: Copyright (c) 2015
 * @version: 1.0.0.0
 * @author: jack
 * @createDate 2015-4-30
 * 
 */
public class GuideActivity extends Activity implements OnPageChangeListener {
    /**
     * 引导页滑动页
     */
    @BindView(R.id.guide_viewpager)
    HackyViewPager mGuideViewPager;
    /**
     * 视图内容
     */
    @BindView(R.id.guide_ll)
    ViewGroup mGuideLayout;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] mTipIVs;
    /**
     * 图片内容
     */
    private ArrayList<Integer> mPhotoList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    /**
     * 初始化视图控件
     */
    private void initView() {
        int size = mPhotoList.size();
        mGuideViewPager.setOffscreenPageLimit(size);
        mGuideViewPager.setOnPageChangeListener(this);
        mGuideViewPager.setAdapter(new ImageAdapter(this));
        // 将点点加入到ViewGroup中
        mTipIVs = new ImageView[size];
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(this);
            int poptWidth = (int) getResources().getDimensionPixelSize(R.dimen.project_popt_width);
            imageView.setLayoutParams(new LayoutParams(poptWidth, poptWidth));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.all_img_dot_pr);
            } else {
                imageView.setBackgroundResource(R.drawable.all_img_dot_un);
            }
            mTipIVs[i] = imageView;
            LayoutParams layoutParams = new LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = poptWidth;
            layoutParams.rightMargin = poptWidth;
            mGuideLayout.addView(imageView, layoutParams);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
       /* mPhotoList = new ArrayList<Integer>(4);
        mPhotoList.add(R.drawable.zhuce_bg);
        mPhotoList.add(R.drawable.zhuce_bg);
        mPhotoList.add(R.drawable.zhuce_bg);
        mPhotoList.add(R.drawable.zhuce_bg);*/
    }

    private class ImageAdapter extends PagerAdapter {
        private LayoutInflater mInflater;
        private int mChildCount = 0;

        ImageAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = mInflater.inflate(R.layout.guide_viewpage_layout, view, false);
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.item_image);
            Button guideBtn = (Button) imageLayout.findViewById(R.id.guid_button);
            imageView.setBackgroundResource(mPhotoList.get(position));
            if (getCount() == (position + 1)) {
                guideBtn.setVisibility(View.VISIBLE);
                guideBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(GuideActivity.this, LoginFragment.class);
                        startActivity(intent);
                        GuideActivity.this.finish();
                    }
                });
            } else {
                guideBtn.setVisibility(View.GONE);
            }
            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int index, float OffsetPercentage, int OffsetPosition) {

    }

    @Override
    public void onPageSelected(int index) {
        setImageBackground(index % mPhotoList.size());
    }

    /**
     * 设置选中的tip的背景
     * 
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < mTipIVs.length; i++) {
            if (i == selectItems) {
                mTipIVs[i].setBackgroundResource(R.drawable.all_img_dot_pr);
            } else {
                mTipIVs[i].setBackgroundResource(R.drawable.all_img_dot_un);
            }

        }
    }
}
