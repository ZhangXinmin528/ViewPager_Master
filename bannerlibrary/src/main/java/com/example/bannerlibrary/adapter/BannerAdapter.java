package com.example.bannerlibrary.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.bannerlibrary.holder.BannerHolderCreator;
import com.example.bannerlibrary.holder.BaseViewHolder;
import com.example.bannerlibrary.listener.BannerClickListener;

import java.util.List;

/**
 * Created by ZhangXinmin on 2017/6/20.
 * Copyright (c) 2017 . All rights reserved.
 * 适配器
 */

public class BannerAdapter<T> extends PagerAdapter {
    private List<T> mDataList;
    private BannerHolderCreator mHolderCreator;
    private ViewPager mViewPager;
    private boolean canLoop;
    private BannerClickListener mBannerListener;
    private final int mLoopCountFactor = 500;

    public BannerAdapter(List<T> mDataList, BannerHolderCreator mHolderCreator, boolean canLoop) {
        this.mDataList = mDataList;
        this.mHolderCreator = mHolderCreator;
        this.canLoop = canLoop;
    }

    /**
     * 设置监听器
     *
     * @param mBannerListener
     */
    public void setBannerClickListener(BannerClickListener mBannerListener) {
        this.mBannerListener = mBannerListener;
    }

    @Override
    public int getCount() {
        int count = mDataList == null ? 0 : mDataList.size();
        // 如果getCount 的返回值为Integer.MAX_VALUE 的话，那么在setCurrentItem的时候会ANR(除了在onCreate 调用之外)
        return canLoop ? count * mLoopCountFactor : count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(position, container);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        //轮播模式才执行
        if (canLoop) {
            int position = mViewPager.getCurrentItem();
            if (position == getCount() - 1) {
                try {
                    mViewPager.setCurrentItem(0, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 初始化ViewPager,绑定适配器
     *
     * @param vewPager
     */
    public void setmViewPager(ViewPager vewPager) {
        this.mViewPager = vewPager;
        mViewPager.setAdapter(this);
        mViewPager.getAdapter().notifyDataSetChanged();
        int currItem = canLoop ? getStartSelectItem() : 0;
        //设置当前选中的Item
        mViewPager.setCurrentItem(currItem);
    }

    private int getStartSelectItem() {
        // 我们设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
        // 但是要保证这个值与getRealPosition 的 余数为0，因为要从第一页开始显示
        int count = mDataList == null ? 0 : mDataList.size();
        int currentItem = count * mLoopCountFactor / 2;
        if (currentItem % count == 0) {
            return currentItem;
        }
        // 直到找到从0开始的位置
        while (currentItem % count != 0) {
            currentItem++;
        }
        return currentItem;
    }

    private View getView(int position, ViewGroup container) {
        int count = mDataList == null ? 0 : mDataList.size();
        final int realPosition = position % count;
        BaseViewHolder holder = null;
        // create holder
        holder = mHolderCreator.createViewHolder();

        if (holder == null) {
            throw new RuntimeException("can not return a null holder");
        }
        // create View
        View view = holder.createBannerView(container.getContext());

        if (mDataList != null && mDataList.size() > 0) {
            holder.onBind(container.getContext(), realPosition, mDataList.get(realPosition));
        }

        // 添加点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerListener != null) {
                    mBannerListener.onBannerItemClick(v, realPosition);
                }
            }
        });

        return view;
    }


}
