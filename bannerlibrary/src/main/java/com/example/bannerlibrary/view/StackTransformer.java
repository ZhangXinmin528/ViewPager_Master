package com.example.bannerlibrary.view;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by ZhangXinmin on 2017/6/21.
 * Copyright (c) 2017 . All rights reserved.
 */

public class StackTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9F;

    @Override
    public void transformPage(View page, float position) {

        if (position <= 1) {
            float scale = Math.max(MIN_SCALE, 1 - Math.abs(position));
            page.setScaleY(scale);
        } else {
            page.setScaleY(MIN_SCALE);
        }
    }
}
