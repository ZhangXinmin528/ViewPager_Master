package com.example.viewpager_master.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.viewpager_master.R;
import com.example.viewpager_master.adapter.OriginalAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

/**
 * Created by ZhangXinmin on 2017/6/15.
 * Copyright (c) 2017 . All rights reserved.
 * 展示原始的Banner和ViewPager
 */

public class OrigBannerFragment extends Fragment {
    private static final String TAG = OrigBannerFragment.class.getSimpleName();

    private Context mContext;
    private ViewPager mViewPager;
    private List<View> mViewPagerList;
    private OriginalAdapter mViewPagerAdapter;
    private int mCurrPosition;
    private boolean isPause;
    private BannerTask mBannerTask;
    private TextView mTitleTv;
    //小圆点
    private LinearLayout mPointLayout;
    private List<ImageView> mPointList;

    public static OrigBannerFragment newInstance() {
        return new OrigBannerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        initParamsAndValues();
    }

    private void initParamsAndValues() {

        mViewPagerList = new ArrayList<>();
        mPointList = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_original, container, false);
        initViews(view);

        showViewPager();
        return view;
    }

    private void initViews(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.banner_orignal);
        mPointLayout = (LinearLayout) view.findViewById(R.id.layout_point);
        mTitleTv = (TextView) view.findViewById(R.id.tv_title);
        mTitleTv.setText(R.string.original_banner);
        //监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             *开始滑动或者用户开始拖动；
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "onPageScrolled..position:" + position);
            }

            /**
             *  滑到新的一页
             */
            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected:" + position);
                mCurrPosition = position;
                setPointVisiable(position % mPointList.size());
            }

            /**
             * 改变状态时调用
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "onPageScrollStateChanged:" + state);
                switch (state) {
                    case SCROLL_STATE_IDLE://in an idle, settled state&Fully in view;
                        isPause = false;
                        break;
                    case SCROLL_STATE_SETTLING://in the process of settling to a final position
                        isPause = true;
                        break;

                }
            }
        });
    }

    private void showViewPager() {
        int[] image = new int[]{R.drawable.sightseeing1, R.drawable.sightseeing5, R.drawable.sightseeing6,
                R.drawable.sightseeing4, R.drawable.sightseeing2, R.drawable.sightseeing7};

        initDataList(image, mViewPagerList);
        initPointList(image.length);//小圆点

        mViewPagerAdapter = new OriginalAdapter(mContext, mViewPagerList);
        mViewPager.setAdapter(mViewPagerAdapter);

        setPointVisiable(0);
        mBannerTask = new BannerTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPause) {
            mBannerTask.execute();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBannerTask != null)
            mBannerTask.cancel(true);
    }

    /**
     * 初始化数据
     *
     * @param arr
     * @param list
     */
    private void initDataList(int[] arr, List<View> list) {
        if (arr.length == 0 || null == list) return;
        for (int id : arr) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_banner_item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_item);
            iv.setImageResource(id);
            list.add(view);
        }
    }

    //小圆点样式
    private void initPointList(int count) {
        if (mPointList == null) return;
        for (int i = 0; i < count; i++) {
            ImageView pointIv = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 8;
            params.leftMargin = 8;
            pointIv.setScaleType(ImageView.ScaleType.FIT_XY);//设置样式
            pointIv.setLayoutParams(params);
            mPointList.add(pointIv);
            mPointLayout.addView(pointIv);
        }
    }

    /**
     * 设置点的显示状态
     *
     * @param position
     */
    private void setPointVisiable(int position) {
        if (mPointList.isEmpty()) return;
        int len = mPointList.size();
        for (int i = 0; i < len; i++) {
            ImageView pointIv = mPointList.get(i);
            if (i == position) {
                pointIv.setImageResource(R.drawable.point_selected);
            } else {
                pointIv.setImageResource(R.drawable.point_unselected);
            }
        }
    }

    class BannerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (isCancelled()) break;

                if (!isPause) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (!isPause)
                mViewPager.setCurrentItem(++mCurrPosition);
            super.onProgressUpdate(values);
        }
    }
}
