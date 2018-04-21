package com.example.ciacho.aishengdemo.app;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ciacho.aishengdemo.R;
import com.example.ciacho.aishengdemo.adapter.MyPagerAdapter;
import com.example.ciacho.aishengdemo.entity.TabEntity;
import forum_moudel.Fragement.ForumFragment;
import message_moudel.Fragment.MessageFragment;
import shop_moudel.Fragment.ShopFragment;
import com.example.ciacho.aishengdemo.utils.ViewFindUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"论坛", "商城", "消息",};
    private int[] mIconUnselectIds = {
            R.drawable.ic_forum_normal,R.drawable.ic_shop_normal,R.drawable.ic_message_normal};
    private int[] mIconSelectIds = {
            R.drawable.ic_forum_press,R.drawable.ic_shop_press,R.drawable.ic_message_press};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private ViewPager mViewPager;
    private CommonTabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        initFragement();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.vp_2);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),mFragments,mTitles));
        mTabLayout = ViewFindUtils.find(mDecorView, R.id.tl_2);
        initView();

    }

    private void initFragement() {
        mFragments.add(ForumFragment.getInstance());
        mFragments.add(ShopFragment.getInstance());
        mFragments.add(MessageFragment.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.personal:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void initView() {
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);
    }
}
