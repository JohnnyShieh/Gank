# Gank - Android & Kotlin

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/JohnnyShieh/Gank/blob/master/LICENSE)
![SDK](https://img.shields.io/badge/SDK-19%2B-orange.svg)
![Release](https://img.shields.io/badge/release-v1.1-blue.svg)

简洁美观的干货集中营(gank.io)的非官方安卓客户端, 数据来源于[干货集中营](http://gank.io/)

**Description**

每天提供一张精选的妹纸图片, 一个精选的休息视频, 若干精选的Android, ios, web等方面的技术干货

**Screenshots**

![screenshot](/screenshots/today.jpg)   ![screenshot](/screenshots/navigation.jpg)   ![screenshot](/screenshots/welfare.jpg)

![screenshot](/screenshots/picture.jpg)   ![screenshot](/screenshots/android.jpg)   ![screenshot](/screenshots/about.jpg)

**Download**

最近版本是 [v1.2](https://github.com/JohnnyShieh/Gank/releases/download/v1.2/Gank_v1.2_2019-07-26_fir.apk)

# App 设计

**UI**

App基本包含下面几个页面:

* 主页显示今日的一些干货内容, 排版与干货集中营类似

* 三个页面显示Android, ios, web的技术干货, 还有一个页面显示休息视频

* 福利页面显示妹纸图片, 点击可进入浏览大图

* 搜索页面可以搜索干货

* 最后再加上关于和反馈页面

基本上使用原生的控件实现, 首先使用support包中DrawLayout实现侧滑抽屉式导航, 使用Toolbar替换之前的Actionbar, 并在Android 5.0上实现沉浸式状态. 主页和Android分类页等都是用SwipeRefreshLayout + RecyclerView, 点击具体干货进入WebViewActivity使用WebView加载网页.

**Architecture**

项目使用 [RxFlux](https://github.com/JohnnyShieh/RxFlux) 架构实现单向数据流, 因为app基本上只是展示网络获取的数据, 单向数据流可以保证数据获取清晰明了. 

![](/pic/flux-arch.png)

App主要分为UI, Action, Store三层, UI请求数据的时候用ActionCreator通过Web API获取数据并产生Action, Action由Dispatcher传递给Store, 最后UI监听到Store的change event再从store中取数据刷新页面. 这样UI层的逻辑就简单了, 业务逻辑转移到Action层.

其中 Store 是继承自 Google Architecture Component 中的 ViewModel 的，用来接受 action 触发的数据变更，其中的 LiveData 修改后驱动 UI 变更.

更详细的信息，请看 [RxFlux](https://github.com/JohnnyShieh/RxFlux) 的介绍.

Web API获取数据是使用 RxJava + Retrofit + okhttp, 使用 Glide 完成图片加载, 使用 Koin 完成依赖注入.

# Dagger 完全解析

个人分析 Dagger 2 使用与原理的文章

[Dagger 2 完全解析（一），Dagger 2 的基本使用与原理](http://johnnyshieh.me/posts/dagger-basic/)

[Dagger 2 完全解析（二），进阶使用 Lazy、Qualifier、Scope 等](http://johnnyshieh.me/posts/dagger-advance/)

[Dagger 2 完全解析（三），Component 的组织关系与 SubComponent](http://johnnyshieh.me/posts/dagger-subcomponent/)

[Dagger 2 完全解析（四），Android 中使用 Dagger 2](http://johnnyshieh.me/posts/dagger-use-in-android/)

[Dagger 2 完全解析（五），Kotlin 中使用 Dagger 2](http://johnnyshieh.me/posts/dagger-use-in-kotlin/)

最近接触过 [Koin](https://github.com/InsertKoinIO/koin) 依赖注入框架后，立马从 Dagger 2 阵营转投到 Koin 阵营，简单说明下它的优点：对 Kotlin 和 Android 非常友好，容易上手，使用简便. 

# Library

* [RxFlux](https://github.com/JohnnyShieh/RxFlux)

* [RxJava](https://github.com/ReactiveX/RxJava)

* [RxAndroid](https://github.com/ReactiveX/RxAndroid)

* [Retrofit](https://github.com/square/retrofit)

* [Glide](https://github.com/bumptech/glide)

* [Koin](https://github.com/InsertKoinIO/koin)

* [Leakcanary](https://github.com/square/leakcanary)

* [gson](https://github.com/google/gson)

# License


    Copyright 2019 Johnny Shieh Open Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
