package com.johnny.gank.network

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

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class DateDeserializer(vararg patterns: String) : JsonDeserializer<Date> {

    private var mDateFormatList: MutableList<SimpleDateFormat> = arrayListOf()

    init {
        for (pattern in patterns) {
            mDateFormatList.add(SimpleDateFormat(pattern, Locale.getDefault()))
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date? {
        for (dateFormat in mDateFormatList) {
            try {
                return dateFormat.parse(json.asString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return null
    }
}
