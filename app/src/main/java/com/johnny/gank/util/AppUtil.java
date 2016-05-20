package com.johnny.gank.util;
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

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

/**
 *
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class AppUtil {

    private static Context mAppContext;

    /**
     * The method should be call when app create.
     */
    public static void init(@NonNull Context context) {
        mAppContext = context;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static String getCacheDir() {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File cacheFile = mAppContext.getExternalCacheDir();
            if(null != cacheFile) {
                return cacheFile.getPath();
            }
        }
        return mAppContext.getCacheDir().getPath();
    }
}
