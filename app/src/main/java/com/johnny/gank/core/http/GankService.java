package com.johnny.gank.core.http;
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

import com.johnny.gank.data.GankApi;
import com.johnny.gank.data.entity.Gank;
import com.johnny.gank.data.response.DateData;
import com.johnny.gank.data.response.DayData;
import com.johnny.gank.data.response.GankData;
import com.johnny.gank.util.AppUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * The Http Api of Gank
 *
 * @author Johnny Shieh
 * @version 1.0
 */
public interface GankService {

    @GET(GankApi.DATE_HISTORY)
    Observable<DateData> getDateHistory();

    @GET("data/{category}/{pageCount}/{page}")
    Observable<GankData> getGank(@Path("category") String category, @Path("pageCount") int pageCount, @Path("page") int page);

    @GET("day/{year}/{month}/{day}")
    Observable<DayData> getDayGank(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    class Factory {

        private static OkHttpClient sOkHttpClient;

        private static final int CACHE_MAX_AGE = 12 * 60 * 60;

        static {
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if(request.url().toString().startsWith(GankApi.BASE_URL)) {
                        int maxAge = CACHE_MAX_AGE;
                        Date receiveDate = response.headers().getDate("Date");
                        if(null != receiveDate) {
                            // set expire time to tomorrow.
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(receiveDate);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int min = calendar.get(Calendar.MINUTE);
                            maxAge = 24 * 3600 - hour * 3600 - min * 60;
                        }
                        return response.newBuilder()
                            .header("Cache-Control", "max-age=" + maxAge)
                            .build();
                    }
                    return response;
                }
            };
            File cacheDir = new File(AppUtil.getCacheDir(), "http_reponse");
            sOkHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .cache(new Cache(cacheDir, 10 * 1024 * 1024))
                .build();
        }

        private static final Gson dateGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls().create();

        private static final GankService sGankService = new Retrofit.Builder()
            .client(sOkHttpClient)
            .baseUrl(GankApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(dateGson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
            .create(GankService.class);

        public static GankService getGankService() {
            return Factory.sGankService;
        }
    }

}
