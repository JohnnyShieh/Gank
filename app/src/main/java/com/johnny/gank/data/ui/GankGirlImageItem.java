package com.johnny.gank.data.ui;
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

import com.johnny.gank.data.entity.Gank;

import java.util.Date;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class GankGirlImageItem implements GankItem {

    public String imgUrl;
    public Date publishedAt;

    public static GankGirlImageItem newImageItem(Gank gank) {
        GankGirlImageItem imageItem = new GankGirlImageItem();
        imageItem.imgUrl = gank.url;
        imageItem.publishedAt = gank.publishedAt;
        return imageItem;
    }
}
