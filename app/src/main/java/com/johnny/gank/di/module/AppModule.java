package com.johnny.gank.di.module;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.johnny.gank.GankApp;
import com.johnny.gank.core.http.DateDeserializer;
import com.johnny.gank.core.http.GankService;
import com.johnny.gank.data.GankApi;
import com.johnny.gank.util.AppUtil;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
@Module
public class AppModule {

    private static final int CACHE_MAX_AGE = 60 * 60;   // one hour

    private static final String DATE_PATTERN1 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
    private static final String DATE_PATTERN2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final GankApp mApp;

    public AppModule(GankApp app) {
        mApp = app;
        AppUtil.init(app);
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return mApp;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
            .addNetworkInterceptor(getNetworkInterceptor())
            .cache(new Cache(new File(AppUtil.getCacheDir(), "http_reponse"), 10 * 1024 * 1024))
            .build();
    }

    @Provides
    @Singleton
    GankService provideGankService(OkHttpClient okHttpClient) {
        Gson dateGson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer(DATE_PATTERN1, DATE_PATTERN2))
            .serializeNulls()
            .create();
        return new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(GankApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(dateGson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GankService.class);
    }

    private Interceptor getNetworkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if(request.url().toString().startsWith(GankApi.BASE_URL) && !request.url().toString().startsWith(GankApi.Query_BASE_URL)) {
                    return response.newBuilder()
                        .header("Cache-Control", "max-age=" + CACHE_MAX_AGE)
                        .build();
                }
                return response;
            }
        };
    }
}
