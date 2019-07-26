package com.johnny.gank.di

import com.johnny.gank.main.GankActionCreator
import com.johnny.gank.search.QueryActionCreator
import com.johnny.gank.main.TodayGankActionCreator
import org.koin.dsl.module

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 *
 * Created on 2019-07-25
 */
val actionCreatorModule = module {

    factory { GankActionCreator(get()) }

    factory { TodayGankActionCreator(get()) }

    factory { QueryActionCreator(get()) }
}