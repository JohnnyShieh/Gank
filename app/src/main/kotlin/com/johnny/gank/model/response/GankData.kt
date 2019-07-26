@file:JvmName("DayData")

package com.johnny.gank.model.response

import com.google.gson.annotations.SerializedName
import com.johnny.gank.model.entity.Gank

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

data class DayData(
    var category: List<String> = emptyList(),
    var results: Result = Result()
)

data class GankData(var results: List<Gank> = emptyList())

data class Result(
    @SerializedName("Android") var androidList: List<Gank> = emptyList(),
    @SerializedName("iOS") var iosList: List<Gank> = emptyList(),
    @SerializedName("福利") var welfareList: List<Gank> = emptyList(),
    @SerializedName("拓展资源") var extraList: List<Gank> = emptyList(),
    @SerializedName("前端") var frontEndList: List<Gank> = emptyList(),
    @SerializedName("瞎推荐") var casualList: List<Gank> = emptyList(),
    @SerializedName("App") var appList: List<Gank> = emptyList(),
    @SerializedName("休息视频") var videoList: List<Gank> = emptyList()
)