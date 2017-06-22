package com.example.bannerlibrary.listener;

import android.view.View;

/**
 * Created by ZhangXinmin on 2017/6/20.
 * Copyright (c) 2017 . All rights reserved.
 * Banner的点击时间
 */

public interface BannerClickListener {
    /**
     * 点击事件
     *
     * @param view
     * @param position
     */
    void onBannerItemClick(View view, int position);
}
