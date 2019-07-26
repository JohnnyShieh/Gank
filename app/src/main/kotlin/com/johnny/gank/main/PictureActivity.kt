package com.johnny.gank.main
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
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.viewpager.widget.ViewPager
import com.johnny.gank.R
import com.johnny.gank.adapter.PicturePagerAdapter
import com.johnny.gank.base.BaseActivity
import com.johnny.gank.model.GankType
import com.johnny.gank.model.StatName
import com.johnny.gank.model.entity.Gank
import com.johnny.gank.model.ui.GankNormalItem
import kotlinx.android.synthetic.main.activity_picture.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class PictureActivity : BaseActivity() {

    companion object {
        const val EXTRA_URL_SINGLE_PIC = "url_single_pic"
        const val EXTRA_PUBLISH_SINGLE_PIC = "publish_single_pic"
        const val EXTRA_PAGE_INDEX = "page_index"
        const val EXTRA_PIC_ID = "pic_id"

        @JvmField
        val sDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

        @JvmStatic
        fun newIntent(context: Context, url: String, publishAt: Date): Intent {
            val intent = Intent(context, PictureActivity::class.java)
            intent.putExtra(EXTRA_URL_SINGLE_PIC, url)
            intent.putExtra(EXTRA_PUBLISH_SINGLE_PIC, publishAt)
            return intent
        }

        @JvmStatic
        fun newIntent(context: Context, pageInex: Int, picId: String, url: String): Intent {
            val intent = Intent(context, PictureActivity::class.java)
            intent.putExtra(EXTRA_PAGE_INDEX, pageInex)
            intent.putExtra(EXTRA_PIC_ID, picId)
            intent.putExtra(EXTRA_URL_SINGLE_PIC, url)
            return intent
        }
    }

    override val pageName = StatName.PAGE_PICTURE

    private var mInitPicId: String? = null

    val mPagerAdapter = PicturePagerAdapter()

    val mStore: GankStore by viewModel()

    val mActionCreator: GankActionCreator by inject()

    val mPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }
        override fun onPageSelected(position: Int) {
            val curItem = mPagerAdapter.getItem(position)
            title = sDateFormatter.format(curItem!!.gank.publishedAt)
            // when scroll to the first position.
            if (position == 0) {
                if (curItem.page > 1) {
                    loadPictureList(curItem.page - 1)
                }
                return
            }
            if(position == mPagerAdapter.count - 1) {
                loadPictureList(curItem.page + 1)
            }
        }
    }

    private fun parseIntentAndInitAdapter() {
        if(null == intent)  return
        val picUrl = intent.getStringExtra(EXTRA_URL_SINGLE_PIC)
        val pageIndex = intent.getIntExtra(EXTRA_PAGE_INDEX, -1)
        if (pageIndex == -1) {
            val publishAt = intent.getSerializableExtra(EXTRA_PUBLISH_SINGLE_PIC) as Date
            val item = GankNormalItem(gank = Gank(url = picUrl.orEmpty(), publishedAt = publishAt))
            mPagerAdapter.initList(arrayListOf(item))
            title = sDateFormatter.format(publishAt)
        } else {
            val picId = intent.getStringExtra(EXTRA_PIC_ID)
            mInitPicId = picId
            loadPictureList(pageIndex)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        parseIntentAndInitAdapter()
        view_pager.adapter = mPagerAdapter
        view_pager.addOnPageChangeListener(mPageChangeListener)

        mStore.gankList.observe(this, androidx.lifecycle.Observer {
            updateList()
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadPictureList(page: Int) {
        mActionCreator.getGankList(mStore, GankType.WELFARE, page)
    }

    private fun getInitPicPos(list: List<GankNormalItem>): Int {
        val size = list.size
        (0 until size).forEach { i ->
            if(TextUtils.equals(list[i].gank._id, mInitPicId)) {
                return i
            }
        }
        return 0
    }

    private fun updateList() {
        if (0 == mPagerAdapter.count) {
            mPagerAdapter.initList(mStore.gankList.value!!)
            mPagerAdapter.notifyDataSetChanged()
            val initPos = getInitPicPos(mStore.gankList.value!!)
            view_pager.setCurrentItem(initPos, false)
            if(0 == initPos) {
                mPageChangeListener.onPageSelected(0)
            }
        } else {
            val addStatus = mPagerAdapter.appendList(mStore.page, mStore.gankList.value!!)
            mPagerAdapter.notifyDataSetChanged()
            if (addStatus == PicturePagerAdapter.ADD_FRONT) view_pager.setCurrentItem(view_pager.currentItem + (mStore.gankList.value?.size ?: 0), false)
        }
    }
}
