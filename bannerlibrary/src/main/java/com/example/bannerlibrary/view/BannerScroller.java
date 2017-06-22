package com.example.bannerlibrary.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by ZhangXinmin on 2017/6/19.
 * Copyright (c) 2017 . All rights reserved.
 * 调整Banner的滑动速度。
 */

public class BannerScroller extends Scroller {
    private int mScrollDuration = 800;//默认600
    private boolean mIsUseDefaultDuration = false;

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mIsUseDefaultDuration ? duration : mScrollDuration);
    }

    public int getScrollDuration() {
        return mScrollDuration;
    }

    public void setScrollDuration(int mScrollDuration) {
        this.mScrollDuration = mScrollDuration;
    }

    public boolean ismIsUseDefaultDuration() {
        return mIsUseDefaultDuration;
    }

    /**
     * 设置是否使用ViewPager的默认轮播速度
     *
     * @param mIsUseDefaultDuration
     */
    public void setIsUseDefaultDuration(boolean mIsUseDefaultDuration) {
        this.mIsUseDefaultDuration = mIsUseDefaultDuration;
    }

}
