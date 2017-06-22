package com.example.viewpager_master.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ZhangXinmin on 2017/6/15.
 * Copyright (c) 2017 . All rights reserved.
 */

public class OriginalAdapter extends PagerAdapter {
    private Context mContext;
    private List<View> mList;

    public OriginalAdapter(Context mContext, List<View> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList == null)
            return 0;
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mList.get(position % mList.size());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position % mList.size()));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
