package com.example.viewpager_master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.example.viewpager_master.fragment.OrigBannerFragment;
import com.example.viewpager_master.fragment.OrigVPFragment;
import com.example.viewpager_master.fragment.StackBannerFragment;
import com.example.viewpager_master.fragment.StackVPFragment;

public class MainActivity extends AppCompatActivity {


    private Fragment mPreFragment, mCurentFragment;
    private FragmentManager mFragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initParamsAndValues();

    }

    private void initParamsAndValues() {
        mFragManager = getSupportFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_origvp://原生ViewPager
                switchShowItem(OrigVPFragment.class.getSimpleName());
                break;
            case R.id.menu_origbanner://原生Banner
                switchShowItem(OrigBannerFragment.class.getSimpleName());
                break;
            case R.id.menu_stackvp://StackViewPager
                switchShowItem(StackVPFragment.class.getSimpleName());
                break;
            case R.id.menu_stackbanner://StackBanner
                switchShowItem(StackBannerFragment.class.getSimpleName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 切换fragment
     *
     * @param type
     */
    private void switchShowItem(String type) {

        if (TextUtils.isEmpty(type)) return;

        switch (type) {

            case "OrigVPFragment":
                OrigVPFragment origVPFragment = (OrigVPFragment) mFragManager.findFragmentByTag("OrigVPFragment");
                if (origVPFragment == null) origVPFragment = OrigVPFragment.newInstance();

                switchShowFragment(origVPFragment, type);
                break;

            case "OrigBannerFragment":
                OrigBannerFragment origBannerFragment = (OrigBannerFragment) mFragManager.findFragmentByTag("OrigBannerFragment");
                if (origBannerFragment == null)
                    origBannerFragment = OrigBannerFragment.newInstance();

                switchShowFragment(origBannerFragment, type);
                break;
            case "StackBannerFragment":
                StackBannerFragment stackBannerFragment = (StackBannerFragment) mFragManager.findFragmentByTag("StackBannerFragment");
                if (stackBannerFragment == null)
                    stackBannerFragment = StackBannerFragment.newInstance();

                switchShowFragment(stackBannerFragment, type);
                break;
            case "StackVPFragment":
                StackVPFragment stackVPFragment = (StackVPFragment) mFragManager.findFragmentByTag("StackVPFragment");
                if (stackVPFragment == null)
                    stackVPFragment = StackVPFragment.newInstance();

                switchShowFragment(stackVPFragment, type);
                break;
        }
    }

    /**
     * 切换逻辑
     *
     * @param fragment
     */
    private void switchShowFragment(Fragment fragment, String tag) {
        if (fragment == null || TextUtils.isEmpty(tag)) return;

        if (fragment.isAdded()) {
            mFragManager.beginTransaction()
                    .show(fragment)
                    .commit();
        } else {
            mFragManager.beginTransaction()
                    .add(R.id.layout_container, fragment, tag)
                    .commit();
        }

        if (mPreFragment != null) {
            mFragManager.beginTransaction()
                    .hide(mPreFragment)
                    .commit();
        }

        mPreFragment = fragment;
    }
}
