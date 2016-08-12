package com.johnny.gank.ui.activity;
/*
 * Copyright (C) 2016 Johnny Shieh Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.johnny.gank.R;
import com.johnny.gank.di.component.DaggerMainActivityComponent;
import com.johnny.gank.di.component.MainActivityComponent;
import com.johnny.gank.di.module.ActivityModule;
import com.johnny.gank.ui.fragment.AndroidFragment;
import com.johnny.gank.ui.fragment.FrontEndFragment;
import com.johnny.gank.ui.fragment.IOSFragment;
import com.johnny.gank.ui.fragment.TodayGankFragment;
import com.johnny.gank.ui.fragment.VideoFragment;
import com.johnny.gank.ui.fragment.WelfareFragment;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

/**
 * @author Johnny Shieh
 * @version 1.0
 */
public class MainActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivityComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInjector();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(R.id.fragment_container, TodayGankFragment.newInstance(), TodayGankFragment.TAG);
    }

    private void initInjector() {
        mComponent = DaggerMainActivityComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(new ActivityModule(this))
            .build();
    }

    public MainActivityComponent getMainActivityComponent() {
        return mComponent;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(SearchActivity.newIntent(this));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_today:
                if(null == getFragmentManager().findFragmentByTag(TodayGankFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, TodayGankFragment.newInstance(), TodayGankFragment.TAG);
                    setTitle(R.string.app_name);
                }
                break;
            case R.id.nav_welfare:
                if(null == getFragmentManager().findFragmentByTag(WelfareFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, WelfareFragment.newInstance(), WelfareFragment.TAG);
                    setTitle(R.string.nav_welfare);
                }
                break;
            case R.id.nav_android:
                if(null == getFragmentManager().findFragmentByTag(AndroidFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, AndroidFragment.newInstance(), AndroidFragment.TAG);
                    setTitle(R.string.nav_android);
                }
                break;
            case R.id.nav_ios:
                if(null == getFragmentManager().findFragmentByTag(IOSFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, IOSFragment.newInstance(), IOSFragment.TAG);
                    setTitle(R.string.nav_ios);
                }
                break;
            case R.id.nav_front_end:
                if(null == getFragmentManager().findFragmentByTag(FrontEndFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, FrontEndFragment.newInstance(), FrontEndFragment.TAG);
                    setTitle(R.string.nav_front_end);
                }
                break;
            case R.id.nav_video:
                if(null == getFragmentManager().findFragmentByTag(VideoFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, VideoFragment.newInstance(), VideoFragment.TAG);
                    setTitle(R.string.nav_video);
                }
                break;
            case R.id.nav_about:
                startActivity(AboutActivity.newIntent(this));
                break;
            case R.id.nav_feedback:
                startFeedbackActivity();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startFeedbackActivity() {
        HashMap<String, String> uiCustomInfoMap = new HashMap<>();
        uiCustomInfoMap.put("enableAudio", "0");
        uiCustomInfoMap.put("themeColor", "#00acc1");
        uiCustomInfoMap.put("hideLoginSuccess", "true");
        uiCustomInfoMap.put("pageTitle", getString(R.string.nav_feedback));
        FeedbackAPI.setUICustomInfo(uiCustomInfoMap);
        FeedbackAPI.setCustomContact("Contact", true);
        FeedbackAPI.openFeedbackActivity(this);
    }
}
