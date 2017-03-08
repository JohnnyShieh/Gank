package com.johnny.gank.rxflux;
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

import android.support.annotation.NonNull;
import android.util.ArrayMap;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class ErrorAction extends Action {

    public static final String KEY_ACTION = "RxError_Action";
    public static final String KEY_THROWABLE = "RxError_Throwable";

    private ErrorAction(String type, ArrayMap<String, Object> data) {
        super(type, data);
    }

    public Action getAction() {
        return (Action) getData().get(KEY_ACTION);
    }

    public Throwable getThrowable() {
        return (Throwable) getData().get(KEY_THROWABLE);
    }

    public static ErrorAction newErrorAction(@NonNull Action action, Throwable throwable) {
        ArrayMap<String, Object> data = new ArrayMap<>(2);
        data.put(KEY_ACTION, action);
        data.put(KEY_THROWABLE, throwable);
        return new ErrorAction(action.getErrorType(), data);
    }
}
