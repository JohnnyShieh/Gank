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

import android.util.ArrayMap;

/**
 * Object class that hold the type of action and the data we want to attach to it
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class Action {

    static final String ERROR_PREFIX = "error_";

    private final String type;
    private final ArrayMap<String, Object> data;

    private final String errorType;

    Action(String type, ArrayMap<String, Object> data) {
        this.type = type;
        this.data = data;
        this.errorType = getErrorType(type);
    }

    public static Builder type(String type) {
        return new Builder().with(type);
    }

    public String getType() {
        return type;
    }

    public ArrayMap<String, Object> getData() {
        return data;
    }

    @SuppressWarnings("unchecked") public <T> T get(String key) {
        return (T) data.get(key);
    }

    public String getErrorType() {
        return errorType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;

        Action action = (Action) o;

        if (!type.equals(action.type)) return false;
        return !(data != null ? !data.equals(action.data) : action.data != null);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Action{" +
            "type='" + type + '\'' +
            ", data=" + data +
            '}';
    }

    static String getErrorType (String type) {
        return ERROR_PREFIX + type;
    }

    public static class Builder {

        private String type;
        private ArrayMap<String, Object> data;

        Builder with(String type) {
            if (type == null) {
                throw new IllegalArgumentException("Type may not be null.");
            }
            this.type = type;
            this.data = new ArrayMap<>();
            return this;
        }

        public Builder bundle(String key, Object value) {
            if (key == null) {
                throw new IllegalArgumentException("Key may not be null.");
            }

            if (value == null) {
                throw new IllegalArgumentException("Value may not be null.");
            }
            data.put(key, value);
            return this;
        }

        public Action build() {
            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("At least one key is required.");
            }
            return new Action(type, data);
        }
    }
}
