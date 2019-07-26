package com.johnny.gank.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.johnny.gank.network.DateDeserializer
import com.johnny.gank.network.GankService
import com.johnny.gank.model.GankApi
import com.johnny.gank.util.AppUtil
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 *
 * Created on 2019-07-25
 */
val appModule = module {

    single<Call.Factory> { okhttp() }

    single { retrofit(get()) }

    single { get<Retrofit>().create(GankService::class.java) }
}

private fun retrofit(callFactory: Call.Factory) = Retrofit.Builder()
    .callFactory(callFactory)
    .baseUrl(GankApi.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(dateGson))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

private fun okhttp() = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .cache(Cache(File(AppUtil.cachePath, "http_reponse"), (10 * 1024 * 1024).toLong()))
    .build()

private const val CACHE_MAX_AGE = 60 * 60   // one hour
private const val DATE_PATTERN1 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
private const val DATE_PATTERN2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

private val dateGson: Gson
    get() = GsonBuilder()
        .registerTypeAdapter(Date::class.java,
            DateDeserializer(DATE_PATTERN1, DATE_PATTERN2)
        )
        .serializeNulls()
        .create()

private val interceptor: Interceptor
    get() = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .header("Cache-Control", "max-age=" + CACHE_MAX_AGE + ", max-stale=" + CACHE_MAX_AGE * 60)
            .build()
        val response = chain.proceed(request)
        if (request.url().toString().startsWith(GankApi.BASE_URL) && !request.url().toString().startsWith(GankApi.Query_BASE_URL)) {
            return@Interceptor response.newBuilder()
                .header("Cache-Control", "max-stale=$CACHE_MAX_AGE")
                .build()
        }
        response
    }