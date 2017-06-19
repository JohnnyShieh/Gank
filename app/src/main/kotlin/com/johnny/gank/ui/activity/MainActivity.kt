package com.johnny.gank.ui.activity
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
import com.johnny.gank.di.component.MainActivityComponent;
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import java.util.HashMap;

/**
 * @author Johnny Shieh
 * @version 1.0
 */
class MainActivity : BaseActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    var component: MainActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initInjector()

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        replaceFragment(R.id.fragment_container, TodayGankFragment.newInstance(), TodayGankFragment.TAG)
    }

    private fun initInjector() {
        component = getAppComponent()
            .mainActivityComponent()
            .activity(this)
            .build()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            startActivity(SearchActivity.newIntent(this))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_today -> {
                if(null == fragmentManager.findFragmentByTag(TodayGankFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, TodayGankFragment.newInstance(), TodayGankFragment.TAG)
                    setTitle(R.string.app_name)
                }
            }
            R.id.nav_welfare -> {
                if(null == fragmentManager.findFragmentByTag(WelfareFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, WelfareFragment.newInstance(), WelfareFragment.TAG)
                    setTitle(R.string.nav_welfare)
                }
            }
            R.id.nav_android -> {
                if(null == fragmentManager.findFragmentByTag(AndroidFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, AndroidFragment.newInstance(), AndroidFragment.TAG)
                    setTitle(R.string.nav_android)
                }
            }
            R.id.nav_ios -> {
                if(null == fragmentManager.findFragmentByTag(IOSFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, IOSFragment.newInstance(), IOSFragment.TAG)
                    setTitle(R.string.nav_ios)
                }
            }
            R.id.nav_front_end -> {
                if(null == fragmentManager.findFragmentByTag(FrontEndFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, FrontEndFragment.newInstance(), FrontEndFragment.TAG)
                    setTitle(R.string.nav_front_end)
                }
            }
            R.id.nav_video -> {
                if(null == fragmentManager.findFragmentByTag(VideoFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, VideoFragment.newInstance(), VideoFragment.TAG)
                    setTitle(R.string.nav_video)
                }
            }
            R.id.nav_about -> { startActivity(AboutActivity.newIntent(this)) }
            R.id.nav_feedback -> { startFeedbackActivity() }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun startFeedbackActivity() {
        val uiCustomInfoMap = mutableMapOf<String, String>()
        uiCustomInfoMap.plus("enableAudio" to "0")
        uiCustomInfoMap.plus("themeColor" to "#00acc1")
        uiCustomInfoMap.plus("hideLoginSuccess" to "true")
        uiCustomInfoMap.plus("pageTitle" to getString(R.string.nav_feedback))
        FeedbackAPI.setUICustomInfo(uiCustomInfoMap)
        FeedbackAPI.setCustomContact("Contact", true)
        FeedbackAPI.openFeedbackActivity(this)
    }
}
