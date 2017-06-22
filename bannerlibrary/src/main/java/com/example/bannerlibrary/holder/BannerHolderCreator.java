package com.example.bannerlibrary.holder;

/**
 * Created by ZhangXinmin on 2017/6/20.
 * Copyright (c) 2017 . All rights reserved.
 */

public interface BannerHolderCreator<H extends BaseViewHolder> {

    /**
     * 创建ViewHolder
     *
     * @return
     */
    H createViewHolder();
}
