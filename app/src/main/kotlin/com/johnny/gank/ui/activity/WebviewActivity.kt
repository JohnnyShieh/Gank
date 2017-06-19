package com.johnny.gank.ui.activity
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

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.webkit.*
import com.johnny.gank.R
import com.johnny.gank.stat.StatName
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_webview.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class WebviewActivity : BaseActivity(),
        SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val EXTRA_URL = "URL"
        const val EXTRA_TITLE = "TITLE"

        @JvmStatic
        fun openUrl(context: Context, url: String, title: String) {
            context.startActivity<WebviewActivity>(EXTRA_URL to url, EXTRA_TITLE to title)
        }
    }

    private var mUrl: String? = null
    private var mTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        refresh_layout.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        setUpWebView()

        if (null != intent) {
            mUrl = intent!!.getStringExtra(EXTRA_URL)
            mTitle = intent!!.getStringExtra(EXTRA_TITLE)
        }

        title = mTitle
        webview.loadUrl(mUrl)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(StatName.PAGE_WEBVIEW)
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        webview.onPause()
        MobclickAgent.onPageEnd(StatName.PAGE_WEBVIEW)
        MobclickAgent.onPause(this)
    }

    private fun setUpWebView() {
        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                refresh_layout.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                refresh_layout.isRefreshing = false
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                refresh_layout.isRefreshing = false
            }
        })
        webview.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress >= 80)  refresh_layout.isRefreshing = false
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_webview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (webview.canGoBack()) {
                    webview.goBack()
                    return true
                }
            }
            R.id.action_share -> {
                sharePage()
                return true
            }
            R.id.action_open_in_browser -> {
                openInBrowser()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sharePage() {
        share(getString(R.string.share_page, webview.title, webview.url))
    }

    private fun openInBrowser() {
        browse(webview.url)
    }

    override fun onRefresh() {
        webview.reload()
    }

    override fun onDestroy() {
        super.onDestroy()
        // After Android 5.1, there has a problem in Webview:
        // if onDetach is called after destroy, AwComponentCallbacks object will be leaked.
        val tmpWebView = webview
        if(null != webview.parent) {
            (webview.parent as ViewGroup).removeView(webview)
        }
        tmpWebView.destroy()
    }
}
