package com.johnny.gank.domain;
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
import com.johnny.gank.data.response.DateData;
import com.johnny.gank.data.response.DayData;
import com.johnny.gank.data.response.GankData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * The Http Api of Gank
 *
 * @author Johnny Shieh
 * @version 1.0
 */
public interface GankService {

    @GET(GankApi.DATE_HISTORY)
    Call<DateData> getDateHistory();

    @GET("data/{category}/{pageCount}/{page}")
    Call<GankData> getGank(@Path("category") String category, @Path("pageCount") int pageCount, @Path("page") int page);

    @GET("day/{year}/{month}/{day}")
    Call<DayData> getDayGank(@Path("year") int year, @Path("month") int month, @Path("day") int day);
}
