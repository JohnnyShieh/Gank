package com.johnny.gank.store;
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


import com.johnny.rxflux.Store;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class StoreChange {

    public static class AndroidStore extends Store.StoreChangeEvent {}

    public static class FrontEndStore extends Store.StoreChangeEvent {}

    public static class IOSStore extends Store.StoreChangeEvent {}

    public static class PictureStore extends Store.StoreChangeEvent {}

    public static class SearchStore extends Store.StoreChangeEvent {}

    public static class TodayDankStore extends Store.StoreChangeEvent {}

    public static class VideoStore extends Store.StoreChangeEvent {}

    public static class WelfareStore extends Store.StoreChangeEvent {}
}
