package com.johnny.gank.rxflux.util;
/*
 * Copyright (C) 2017 Johnny Shieh Open Source Project
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

import com.johnny.gank.rxflux.Action;
import com.johnny.gank.rxflux.Store;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;

/**
 * The Logger to track flux flow
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class Logger {

    private static final String TAG = "RxFlux";

    private static boolean logEnabled = true;

    public static void setLogEnabled(boolean enabled) {
        logEnabled = enabled;
    }

    public static void logRegisterStore(String storeName, String[] actionType) {
        if(logEnabled) {
            if(null == actionType || actionType.length == 0) {
                Log.d(TAG, "Store " + storeName + " has registered all action");
            }else {
                Log.d(TAG, "Store " + storeName + " has registered action : " + Arrays.toString(actionType));
            }
        }
    }

    public static void logUnregisterStore(String storeName) {
        if(logEnabled) {
            Log.d(TAG, "Store " + storeName + " has unregistered");
        }
    }

    public static void logPostAction(@NonNull Action action) {
        if(logEnabled) {
            Log.d(TAG, "Post " + action.toString());
        }
    }

    public static void logPostErrorAction(@NonNull Action action, Throwable throwable) {
        if(logEnabled) {
            Log.d(TAG, "Post error action " + action.getType() + " cause message : " + throwable.getMessage());
        }
    }

    public static void logOnAction(String storeName, Action action) {
        if(logEnabled) {
            Log.d(TAG, "Store " + storeName + " onAction " + action.toString());
        }
    }

    public static void logOnError(String storeName, String actionName) {
        if(logEnabled) {
            Log.d(TAG, "Store " + storeName + " onError " + actionName);
        }
    }

    public static void logPostStoreChange(String storeName, Store.StoreChangeEvent event) {
        if(logEnabled) {
            Log.d(TAG, "Store " + storeName + " post " + event.getClass().getSimpleName());
        }
    }

    public static void logPostStoreError(String storeName, Store.StoreChangeEvent event) {
        if(logEnabled) {
            Log.d(TAG, "Store " + storeName + " post error " + event.getClass().getSimpleName());
        }
    }
}
