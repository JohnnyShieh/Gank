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

import com.johnny.gank.data.GankApi;
import com.johnny.gank.data.response.DateData;
import com.johnny.gank.data.response.DayData;
import com.johnny.gank.data.response.GankData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * The Http Api of Gank
 *
 * @author Johnny Shieh
 * @version 1.0
 */
interface GankService {

    @GET(GankApi.DATE_HISTORY)
    fun getDateHistory(): Observable<DateData>

    @GET("data/{category}/{pageCount}/{page}")
    fun getGank(@Path("category") category: String, @Path("pageCount") pageCount: Int, @Path("page") page: Int): Observable<GankData>

    @GET("day/{year}/{month}/{day}")
    fun getDayGank(@Path("year") year: Int, @Path("month") month: Int, @Path("day") day: Int): Observable<DayData>

    @GET("search/query/{queryText}/category/all/count/{count}/page/{page}")
    fun queryGank(@Path("queryText") queryText: String, @Path("count") count: Int, @Path("page") page: Int): Observable<GankData>

}
