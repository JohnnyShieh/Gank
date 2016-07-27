package com.johnny.gank.data.entity;
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

import java.util.Date;
import java.util.Objects;

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
public class Gank {

    public String _id;
    public Date createdAt;
    public String desc;
    public Date publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public String who;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gank gank = (Gank) o;
        return Objects.equals(_id, gank._id) &&
            Objects.equals(type, gank.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, type);
    }

}
