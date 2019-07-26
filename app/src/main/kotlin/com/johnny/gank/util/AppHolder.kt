package com.johnny.gank.util

import android.app.Application

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 *
 * Created on 2019-07-26
 */
object AppHolder {
    lateinit var app: Application
        private set

    fun init(application: Application) {
        app = application
    }
}