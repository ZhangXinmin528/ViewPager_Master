package com.example.bannerlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.bannerlibrary.R;
import com.example.bannerlibrary.adapter.BannerAdapter;
import com.example.bannerlibrary.holder.BannerHolderCreator;
import com.example.bannerlibrary.listener.BannerClickListener;
import com.example.bannerlibrary.util.DisplayUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangXinmin on 2017/6/19.
 * Copyright (c) 2017 . All rights reserved.
 */

public class BannerView<T> extends RelativeLayout {
    private static final int OFFSCREEN_PAGES = 4;
    private Context mContext;

    private LinearLayout mPointLayout;//圆点指示器
    private List<ImageView> mPointsList;//圆点数据集合
    private List<T> mDataList;
    private ViewPager mViewPager;
    private BannerScroller mScroller;
    private BannerAdapter mAdapter;
    private Handler mHandler = new Handler();

    private int mPaddingOffset;//相邻两个的距离
    private boolean mIsStartBanner;//是否轮播
    private boolean mIsAutoPlay = true;//是否自动播放
    private int mCurrentItem;//显示当前页
    private int mDelayedTime = 3000;// Banner 切换时间间隔
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private BannerClickListener mBannerClickListener;

    public BannerView(Context context) {
        this(context, null, 0);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initParams(context, attrs);
    }

    /**
     * 初始化参数配置
     *
     * @param context
     * @param attrs
     * @hide
     */
    private void initParams(Context context, AttributeSet attrs) {
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_stack, this, true);
        mPointLayout = (LinearLayout) view.findViewById(R.id.layout_point);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_lib);
        mViewPager.setOffscreenPageLimit(OFFSCREEN_PAGES);//优化加载

        mPointsList = new ArrayList<>();
        mDataList = new ArrayList<>();

        mPaddingOffset = DisplayUtil.dp2px(mContext, 30);

        TypedArray tArray = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mIsStartBanner = tArray.getBoolean(R.styleable.BannerView_isStartBanner, true);

        //初始化Scroller
        initBannerScroller();

    }

    /**
     * 设置Banner的滑动速度
     *
     * @hide
     */
    private void initBannerScroller() {
        Field field = null;
        try {
            field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            mScroller = new BannerScroller(mContext);
            field.set(mViewPager, mScroller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化圆点指示器
     *
     * @hide
     */
    private void initBannerIndicator() {

        mPointLayout.removeAllViews();
        mPointsList.clear();

        //圆点参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 8;
        params.leftMargin = 8;

        int len = mDataList.size();
        for (int i = 0; i < len; i++) {
            ImageView pointIv = new ImageView(mContext);
            pointIv.setScaleType(ImageView.ScaleType.FIT_XY);//设置样式
            pointIv.setLayoutParams(params);
            mPointsList.add(pointIv);
            mPointLayout.addView(pointIv);
        }
    }

    /**
     * 实现轮播效果
     *
     * @hide
     */
    private final Runnable mLoopRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsAutoPlay) {
                mCurrentItem = mViewPager.getCurrentItem();
                mCurrentItem++;
                if (mCurrentItem == mAdapter.getCount() - 1) {
                    mCurrentItem = 0;
                    mViewPager.setCurrentItem(mCurrentItem, false);
                } else {
                    mViewPager.setCurrentItem(mCurrentItem);
                }
            }
            mHandler.postDelayed(this, mDelayedTime);
        }
    };

    /**
     * 开启Banner
     *
     * @see
     */
    public void startBanner() {
        if (mAdapter == null) return;

        if (mIsStartBanner) {
            mIsAutoPlay = true;
            mHandler.postDelayed(mLoopRunnable, mDelayedTime);
        }
    }

    /**
     * 暂停Banner
     */
    public void pauseBanner() {
        mIsAutoPlay = false;
        mHandler.removeCallbacks(mLoopRunnable);
    }

    /**
     * 设置Banner的切换间隔时间
     *
     * @param mDelayedTime
     */
    public void setDelayedTime(int mDelayedTime) {
        this.mDelayedTime = mDelayedTime;
    }

    /**
     * 为ViewPager添加监听器
     *
     * @param mOnPageChangeListener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    /**
     * 为Banner的item添加点击事件
     *
     * @param mBannerClickListener
     */
    public void setBannerClickListener(BannerClickListener mBannerClickListener) {
        this.mBannerClickListener = mBannerClickListener;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setPagesDate(List<T> datas, BannerHolderCreator creator) {
        if (datas == null || creator == null) {
            return;
        }
        mDataList = datas;

        //增加一个逻辑：由于该模式会在一个页面展示前后页面的部分，因此，数据集合的长度至少为3,否则，自动为普通Banner模式
        //不管配置的:open_mz_mode 属性的值是否为true

        if (datas.size() < 3) {
            throw new UnsupportedOperationException("Please add at least 3 items!");
        }
        mViewPager.setPageTransformer(false, new StackTransformer());//开启堆叠模式

        mAdapter = new BannerAdapter(datas, creator, mIsStartBanner);
        mAdapter.setmViewPager(mViewPager);
        mAdapter.setBannerClickListener(mBannerClickListener);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;


                // 切换indicator
                int realSelectPosition = mCurrentItem % mPointsList.size();
                for (int i = 0; i < mDataList.size(); i++) {
                    if (i == realSelectPosition) {
                        mPointsList.get(i).setImageResource(R.drawable.point_selected);
                    } else {
                        mPointsList.get(i).setImageResource(R.drawable.point_unselected);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mIsAutoPlay = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        mIsAutoPlay = true;
                        break;

                }
            }
        });
        if (mOnPageChangeListener != null) {
            mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        }
        //初始化小圆点
        initBannerIndicator();
    }

    /**
     * 设置是否使用ViewPager的默认轮播速度
     *
     * @param isDefaultDuration
     */
    public void setBannerDefultDuration(boolean isDefaultDuration) {
        mScroller.setIsUseDefaultDuration(isDefaultDuration);
    }


    /**
     * 获取
     *
     * @return
     */
    public int getBannerDuration() {
        return mScroller.getScrollDuration();
    }

}
