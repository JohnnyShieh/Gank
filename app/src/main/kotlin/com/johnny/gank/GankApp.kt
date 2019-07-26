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

import android.app.Application
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.johnny.gank.di.actionCreatorModule
import com.johnny.gank.di.appModule
import com.johnny.gank.di.storeModule
import com.johnny.gank.util.AppHolder
import com.johnny.rxflux.RxFlux
import com.squareup.leakcanary.LeakCanary
import com.umeng.analytics.MobclickAgent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
class GankApp : Application() {

    override fun onCreate() {
        super.onCreate()

        MobclickAgent.openActivityDurationTrack(false)
        MobclickAgent.enableEncrypt(true)
        FeedbackAPI.initAnnoy(this, getString(R.string.ali_app_key))
        AppHolder.init(this)
        RxFlux.enableRxFluxLog(BuildConfig.DEBUG)
        initInjector()
        LeakCanary.install(this)
    }

    private fun initInjector() {
        startKoin {
            androidLogger()
            androidContext(this@GankApp)
            modules(listOf(
                appModule,
                actionCreatorModule,
                storeModule
            ))
        }
    }

}
