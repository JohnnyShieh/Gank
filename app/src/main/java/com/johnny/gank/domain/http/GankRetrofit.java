package com.johnny.gank.domain.http;
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

import com.johnny.gank.data.GankApi;
import com.johnny.gank.domain.GankService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class GankRetrofit {

    private static class ServiceHolder {
        public static final GankService sGankService = new Retrofit.Builder()
            .client(new OkHttpClient())
            .baseUrl(GankApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GankService.class);
    }

    public static GankService getGankService() {
        return ServiceHolder.sGankService;
    }
}
