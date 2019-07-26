package com.johnny.gank.main
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

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.google.android.material.navigation.NavigationView
import com.johnny.gank.R
import com.johnny.gank.about.AboutActivity
import com.johnny.gank.search.SearchActivity
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.startActivity

/**
 * @author Johnny Shieh
 * @version 1.0
 */
class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        replaceFragment(R.id.fragment_container, TodayGankFragment(), TodayGankFragment.TAG)
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
                if(null == supportFragmentManager.findFragmentByTag(TodayGankFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, TodayGankFragment(), TodayGankFragment.TAG)
                    setTitle(R.string.app_name)
                }
            }
            R.id.nav_welfare -> {
                if(null == supportFragmentManager.findFragmentByTag(WelfareFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, WelfareFragment(), WelfareFragment.TAG)
                    setTitle(R.string.nav_welfare)
                }
            }
            R.id.nav_android -> {
                if(null == supportFragmentManager.findFragmentByTag(AndroidFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, AndroidFragment(), AndroidFragment.TAG)
                    setTitle(R.string.nav_android)
                }
            }
            R.id.nav_ios -> {
                if(null == supportFragmentManager.findFragmentByTag(IOSFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, IOSFragment(), IOSFragment.TAG)
                    setTitle(R.string.nav_ios)
                }
            }
            R.id.nav_front_end -> {
                if(null == supportFragmentManager.findFragmentByTag(FrontEndFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, FrontEndFragment(), FrontEndFragment.TAG)
                    setTitle(R.string.nav_front_end)
                }
            }
            R.id.nav_video -> {
                if(null == supportFragmentManager.findFragmentByTag(VideoFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, VideoFragment(), VideoFragment.TAG)
                    setTitle(R.string.nav_video)
                }
            }
            R.id.nav_about -> {
                startActivity<AboutActivity>()
            }
            R.id.nav_feedback -> {
                startFeedbackActivity()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(containerViewId: Int, fragment: Fragment, tag: String) {
        if (null == supportFragmentManager.findFragmentByTag(tag)) {
            supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment, tag)
                .commit()
        }
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
