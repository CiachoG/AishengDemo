package com.example.ciacho.aishengdemo.app;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
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
import modular_chat.chat_main.ChatActivity;
import modular_forum.FragmentForum;
import module_message.Fragment.MessageFragment;
import module_personal.PersonalFragment;
import module_shop.Fragment.ShopFragment;
import com.example.ciacho.aishengdemo.utils.ViewFindUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;


import org.litepal.LitePal;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"论坛", "商城", "消息","我的"};
    private int[] mIconUnselectIds = {
            R.drawable.ic_forum_normal,R.drawable.ic_shop_normal,R.drawable.ic_message_normal,R.drawable.ic_personal_normal};
    private int[] mIconSelectIds = {
            R.drawable.ic_forum_press,R.drawable.ic_shop_press,R.drawable.ic_message_press,R.drawable.ic_personal_press};
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
        LitePal.initialize(this);
        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.vp_2);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),mFragments,mTitles));
        mTabLayout = ViewFindUtils.find(mDecorView, R.id.tl_2);
        initView();

    }

    private void initFragement() {
        mFragments.add(FragmentForum.getInstance());
        mFragments.add(ShopFragment.getInstance());
        mFragments.add(MessageFragment.getInstance());
        mFragments.add(PersonalFragment.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.personal:
                Intent intent =new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
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
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
