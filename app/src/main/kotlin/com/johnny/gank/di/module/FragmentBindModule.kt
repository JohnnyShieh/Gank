package com.johnny.gank.di.module

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

import com.johnny.gank.di.FragmentScope
import com.johnny.gank.ui.fragment.AndroidFragment
import com.johnny.gank.ui.fragment.FrontEndFragment
import com.johnny.gank.ui.fragment.IOSFragment
import com.johnny.gank.ui.fragment.TodayGankFragment
import com.johnny.gank.ui.fragment.VideoFragment
import com.johnny.gank.ui.fragment.WelfareFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * description

 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
@Module
abstract class FragmentBindModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun androidFragmentInjector(): AndroidFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun frontEndFragmentInjector(): FrontEndFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun iosFragmentInjector(): IOSFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun todayGankFragmentInjector(): TodayGankFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun VideoFragmentInjector(): VideoFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun welfareFragmentInjector(): WelfareFragment
}
