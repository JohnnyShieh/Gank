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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class DateDeserializer implements JsonDeserializer {

    private List<SimpleDateFormat> mDateFormatList;

    public DateDeserializer(String... patterns) {
        mDateFormatList = new ArrayList<>(patterns.length);
        for(String pattern : patterns) {
            mDateFormatList.add(new SimpleDateFormat(pattern, Locale.US));
        }
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
        for (SimpleDateFormat dateFormat : mDateFormatList) {
            try {
                return dateFormat.parse(json.getAsString());
            }catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
