package com.johnny.gank

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

import android.app.Activity
import android.app.Application
import android.util.Log
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.johnny.gank.di.component.AppComponent
import com.johnny.gank.di.component.DaggerAppComponent
import com.johnny.gank.di.module.AppModule
import com.johnny.gank.util.AppUtil
import com.orhanobut.logger.Logger
import com.orhanobut.logger.Settings
import com.squareup.leakcanary.LeakCanary
import com.umeng.analytics.MobclickAgent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
class GankApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        MobclickAgent.openActivityDurationTrack(false)
        MobclickAgent.enableEncrypt(true)
        FeedbackAPI.initAnnoy(this, getString(R.string.ali_app_key))
        AppUtil.init(this)
        Logger.initialize(
                Settings()
                        .isShowMethodLink(true)
                        .isShowThreadInfo(false)
                        .setMethodOffset(0)
                        .setLogPriority(if (BuildConfig.DEBUG) Log.VERBOSE else Log.ASSERT)
        )
        initInjector()
        LeakCanary.install(this)
    }

    override fun activityInjector() = dispatchingActivityInjector

    private fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        appComponent.inject(this)
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

}
