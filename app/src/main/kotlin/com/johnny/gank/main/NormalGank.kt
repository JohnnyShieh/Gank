package com.johnny.gank.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.johnny.gank.model.ui.GankNormalItem
import com.johnny.gank.network.GankService
import com.johnny.rxflux.RxFlux
import com.johnny.rxflux.Store
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 *
 * Created on 2019-07-26
 */
private val actionType = RxFlux.newActionType<GankListActionData>("get_normal_gank_list")

private data class GankListActionData(
    val page: Int,
    val list: List<GankNormalItem>
)

@SuppressLint("CheckResult")
class GankActionCreator(
    private val gankService: GankService
) {
    companion object {
        private const val DEFAULT_PAGE_COUNT = 16
    }

    private var requesting = false

    fun getGankList(store: Store, category: String, page: Int) {
        if (requesting) return
        requesting = true
        gankService.getGank(category,
            DEFAULT_PAGE_COUNT, page)
            .filter { it.results.isNotEmpty() }
            .map { GankNormalItem.newGankList(it.results, page) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    requesting = false
                    RxFlux.postAction(
                        actionType, store,
                        GankListActionData(page, it)
                    )
                },
                {
                    requesting = false
                    RxFlux.postError(actionType, store, it)
                }
            )
    }
}

class GankStore : Store() {

    val isSwipeRefreshing = MutableLiveData<Unit>()

    val isLoadingMore = MutableLiveData<Unit>()

    val gankList = MutableLiveData<List<GankNormalItem>>()

    var page = -1
        private set

    init {
        register(
            actionType,
            { value ->
                page = value.page
                gankList.value = value.list
                if (1 == page) {
                    isSwipeRefreshing.value = Unit
                }
                isLoadingMore.value = Unit
            },
            {
                isSwipeRefreshing.value = Unit
                isLoadingMore.value = Unit
            }
        )
    }
}