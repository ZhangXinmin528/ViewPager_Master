package com.example.bannerlibrary.holder;

import android.content.Context;
import android.view.View;

/**
 * Created by ZhangXinmin on 2017/6/20.
 * Copyright (c) 2017 . All rights reserved.
 */

public interface BaseViewHolder<T> {

    /**
     * Create Banner's item view.
     *
     * @param context
     * @return
     */
    View createBannerView(Context context);

    /**
     * Bind date with Banner's item.
     *
     * @param context
     * @param position
     * @param t
     */
    void onBind(Context context, int position, T t);
}
