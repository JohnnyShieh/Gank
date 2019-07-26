package com.johnny.gank.di

import com.johnny.gank.main.GankStore
import com.johnny.gank.search.SearchStore
import com.johnny.gank.main.TodayGankStore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 *
 * Created on 2019-07-25
 */
val storeModule = module {

    viewModel { GankStore() }

    viewModel { TodayGankStore() }

    viewModel { SearchStore() }
}