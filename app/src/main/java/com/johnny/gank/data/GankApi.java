package com.johnny.gank.data;
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

/**
 * @author Johnny Shieh
 * @version 1.0
 */
public interface GankApi {

    String BASE_URL = "http://gank.io/api/";

    // The published date history
    String DATE_HISTORY = "day/history";

    String Query_BASE_URL = "http://gank.io/api/search/query";

    // The category gank data, such as, http://gank.io/api/data/Android/10/1
    // The gank data of one day, such as, http://gank.io/api/day/年/月/日

}
