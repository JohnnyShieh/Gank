package com.johnny.gank.util
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
import android.os.Environment
import android.support.annotation.NonNull

/**
 *
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
object AppUtil {

    lateinit var appContext: Context
        @JvmStatic get
        private set

    /**
     * The method should be call when app create.
     */
    @JvmStatic
    fun init(@NonNull context: Context) {
        appContext = context
    }

    @JvmStatic
    fun getCacheDir(): String {
        if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            val cacheFile = appContext.externalCacheDir
            if(null != cacheFile) {
                return cacheFile.path
            }
        }
        return appContext.cacheDir.path
    }
}
