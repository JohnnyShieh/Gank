package com.johnny.gank.model.entity

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

import java.util.Date

/**
 * 干货数据
 *
 * 示例:
 * {
 * "_id": "5715097267765974f5e27db0",
 * "createdAt": "2016-04-19T00:21:06.420Z",
 * "desc": "\u6c34\u5e73\u8fdb\u5ea6\u6761",
 * "publishedAt": "2016-04-19T12:13:58.869Z",
 * "source": "chrome",
 * "type": "Android",
 * "url": "https://github.com/MasayukiSuda/AnimateHorizontalProgressBar",
 * "used": true,
 * "who": "Jason"
 * }
 *
 * @author Johnny Shieh
 * @version 1.0
 */
data class Gank(
    var _id: String = "",
    var createdAt: Date = Date(),
    var desc: String = "",
    var publishedAt: Date = Date(),
    var source: String = "",
    var type: String = "",
    var url: String = "",
    var used: Boolean = false,
    var who: String = ""
)
