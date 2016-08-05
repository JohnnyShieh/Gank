package com.johnny.gank.ui.activity;
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

import com.johnny.gank.R;
import com.johnny.gank.action.ActionType;
import com.johnny.gank.action.PictureActionCreator;
import com.johnny.gank.action.RxError;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.di.component.DaggerPictureActivityComponent;
import com.johnny.gank.di.module.ActivityModule;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.dispatcher.RxViewDispatch;
import com.johnny.gank.stat.StatName;
import com.johnny.gank.store.PictureStore;
import com.johnny.gank.store.RxStoreChange;
import com.johnny.gank.store.WelfareStore;
import com.johnny.gank.ui.adapter.PicturePagerAdapter;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class PictureActivity extends BaseActivity implements RxViewDispatch{

    private static final String EXTRA_URL_SINGLE_PIC = "url_single_pic";
    private static final String EXTRA_PUBLISH_SINGLE_PIC = "publish_single_pic";
    private static final String EXTRA_PAGE_INDEX = "page_index";
    private static final String EXTRA_PIC_ID = "pic_id";

    private static final SimpleDateFormat sDateFormatter = new SimpleDateFormat("yyyy-MM-dd",
        Locale.getDefault());

    @Bind(R.id.view_pager) ViewPager vViewPager;


    private String mInitPicId;      // the pic id of the first picture when enter activity.

    @Inject PicturePagerAdapter mPagerAdapter;
    @Inject PictureStore mStore;
    @Inject PictureActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    public static Intent newIntent(Context context, String url, Date publishAt) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(EXTRA_URL_SINGLE_PIC, url);
        intent.putExtra(EXTRA_PUBLISH_SINGLE_PIC, publishAt);
        return intent;
    }

    public static Intent newIntent(Context context, int pageIndex, String picId) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(EXTRA_PAGE_INDEX, pageIndex);
        intent.putExtra(EXTRA_PIC_ID, picId);
        return intent;
    }

    private void parseIntentAndInitAdapter() {
        Intent intent = getIntent();
        if(null == intent)  return;
        String singlePicUrl = intent.getStringExtra(EXTRA_URL_SINGLE_PIC);
        if(!TextUtils.isEmpty(singlePicUrl)) {
            Date publishAt = (Date) intent.getSerializableExtra(EXTRA_PUBLISH_SINGLE_PIC);
            List<GankNormalItem> itemList = new ArrayList<>(1);
            GankNormalItem item = new GankNormalItem();
            item.url = singlePicUrl;
            item.publishedAt = publishAt;
            itemList.add(item);

            mPagerAdapter.initList(itemList);
            setTitle(sDateFormatter.format(publishAt));
        }else {
            int pageIndex = intent.getIntExtra(EXTRA_PAGE_INDEX, -1);
            String picId = intent.getStringExtra(EXTRA_PIC_ID);
            if(-1 != pageIndex) {
                mInitPicId = picId;
                loadPictureList(pageIndex);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initInjector();
        ButterKnife.bind(this);
        parseIntentAndInitAdapter();
        vViewPager.setAdapter(mPagerAdapter);
        vViewPager.addOnPageChangeListener(mPageChangeListener);

        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
    }

    private void initInjector() {
        DaggerPictureActivityComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(new ActivityModule(this))
            .build()
            .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
        MobclickAgent.onPageStart(StatName.PAGE_PICTURE);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // if unsubscribe is in onDestroy, this activity's onDestroy may delay to new activity's onCreate
        // it will cause that storeChange event can't receive in new activity
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
        MobclickAgent.onPageEnd(StatName.PAGE_PICTURE);
        MobclickAgent.onPause(this);
    }

    private void loadPictureList(int page) {
        mActionCreator.getPictureList(page);
    }

    private int getInitPicPos(List<GankNormalItem> list) {
        int size = list.size();
        for(int i = 0; i < size; i ++) {
            if(TextUtils.equals(list.get(i)._id, mInitPicId)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case PictureStore.ID:
                if(0 == mPagerAdapter.getCount()) {
                    mPagerAdapter.initList(mStore.getPictureList());
                    mPagerAdapter.notifyDataSetChanged();
                    int initPos = getInitPicPos(mStore.getPictureList());
                    vViewPager.setCurrentItem(initPos, false);
                    // when use setCurrentItem(0), onPageSelected would not be called.
                    // so just call it manually.
                    if(0 == initPos) {
                        mPageChangeListener.onPageSelected(0);
                    }
                }else {
                    int addStatus = mPagerAdapter.appendList(mStore.getPage(), mStore.getPictureList());
                    mPagerAdapter.notifyDataSetChanged();
                    if(addStatus == PicturePagerAdapter.ADD_FRONT) {
                        vViewPager.setCurrentItem(vViewPager.getCurrentItem() + mStore.getPictureList().size(), false);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            case ActionType.GET_PICTURE_LIST:
                // TODO
                break;
            default:
                break;
        }
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            GankNormalItem curItem = mPagerAdapter.getItem(position);
            if(null != curItem.publishedAt) {
                setTitle(sDateFormatter.format(curItem.publishedAt));
            }
            // when scroll to the first position.
            if(position == 0) {
                if(curItem.page > 1) {
                    loadPictureList(curItem.page - 1);
                }
                return;
            }
            if(position == mPagerAdapter.getCount() - 1) {
                loadPictureList(curItem.page + 1);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
