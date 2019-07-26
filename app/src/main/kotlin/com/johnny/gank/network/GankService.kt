package com.johnny.gank.network;

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

import com.johnny.gank.model.response.DayData
import com.johnny.gank.model.response.GankData
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * The Http Api of Gank
 *
 * @author Johnny Shieh
 * @version 1.0
 */
interface GankService {

    @GET("today")
    fun getTodayGank(): Single<DayData>

    @GET("data/{category}/{pageCount}/{page}")
    fun getGank(@Path("category") category: String, @Path("pageCount") pageCount: Int, @Path("page") page: Int): Observable<GankData>

    @GET("search/query/{queryText}/category/all/count/{count}/page/{page}")
    fun queryGank(@Path("queryText") queryText: String, @Path("count") count: Int, @Path("page") page: Int): Observable<GankData>

}
