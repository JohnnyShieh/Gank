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

import android.content.Context
import com.google.gson.GsonBuilder
import com.johnny.gank.GankApp
import com.johnny.gank.core.http.DateDeserializer
import com.johnny.gank.core.http.GankService
import com.johnny.gank.data.GankApi
import com.johnny.gank.util.AppUtil
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import javax.inject.Singleton

/**
 * description

 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
@Module
class AppModule(private val mApp: GankApp) {

    init {
        AppUtil.init(mApp)
    }

    @Provides
    @Singleton
    internal fun provideAppContext(): Context {
        return mApp
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addNetworkInterceptor(networkInterceptor)
                .cache(Cache(File(AppUtil.getCacheDir(), "http_reponse"), (10 * 1024 * 1024).toLong()))
                .build()
    }

    @Provides
    @Singleton
    internal fun provideGankService(okHttpClient: OkHttpClient): GankService {
        val dateGson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, DateDeserializer(DATE_PATTERN1, DATE_PATTERN2))
                .serializeNulls()
                .create()
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(GankApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(dateGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(GankService::class.java)
    }

    private val networkInterceptor: Interceptor
        get() = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (request.url().toString().startsWith(GankApi.BASE_URL) && !request.url().toString().startsWith(GankApi.Query_BASE_URL)) {
                return@Interceptor response.newBuilder()
                        .header("Cache-Control", "max-age=" + CACHE_MAX_AGE)
                        .build()
            }
            response
        }

    companion object {

        @JvmField   // 使用该注解使其称为静态属性
        internal val CACHE_MAX_AGE = 60 * 60   // one hour

        @JvmField
        internal val DATE_PATTERN1 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"

        @JvmField
        internal val DATE_PATTERN2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }
}
