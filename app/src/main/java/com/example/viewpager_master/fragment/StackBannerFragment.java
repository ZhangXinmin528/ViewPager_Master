package com.example.viewpager_master.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bannerlibrary.holder.BannerHolderCreator;
import com.example.bannerlibrary.holder.BaseViewHolder;
import com.example.bannerlibrary.listener.BannerClickListener;
import com.example.bannerlibrary.view.BannerView;
import com.example.viewpager_master.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ZhangXinmin on 2017/6/22.
 * Copyright (c) 2017 . All rights reserved.
 * 堆叠的Banner
 */

public class StackBannerFragment extends Fragment {
    private static final String TAG = StackBannerFragment.class.getSimpleName();

    private Context mContext;
    private BannerView mBannerView;
    private List<Integer> mDataList;
    private BannerHolderCreator mBannerHolderCreator;

    public static StackBannerFragment newInstance() {
        return new StackBannerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParamsAndValues();
    }


    private void initParamsAndValues() {
        mDataList = new ArrayList<>();
        Integer[] image = new Integer[]{R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5,
                R.drawable.image6, R.drawable.image8};
        mDataList.addAll(Arrays.asList(image));

        mBannerHolderCreator = new BannerHolderCreator() {
            @Override
            public BaseViewHolder createViewHolder() {
                return new BannerHolder();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stack_banner, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mBannerView = view.findViewById(R.id.bannerview);

        //点击事件
        mBannerView.setBannerClickListener(new BannerClickListener() {
            @Override
            public void onBannerItemClick(View view, int position) {
                Toast.makeText(mContext, "点击了" + position + "个图片！", Toast.LENGTH_SHORT).show();
            }
        });

        mBannerView.setPagesDate(mDataList, mBannerHolderCreator);//设置数据

    }

    @Override
    public void onResume() {
        super.onResume();
        mBannerView.startBanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBannerView.pauseBanner();
    }

    class BannerHolder implements BaseViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createBannerView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer integer) {
            mImageView.setImageResource(integer);
        }

    }
}
