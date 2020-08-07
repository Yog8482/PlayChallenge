package com.yogendra.playapplication.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.yogendra.playapplication.data.Itemdetail
import com.yogendra.playapplication.datasource.local.AllKeysDao
import com.yogendra.playapplication.datasource.local.ItemDetailsDao
import com.yogendra.playapplication.datasource.remote.ItemDetailsPagedDataSource
import com.yogendra.playapplication.datasource.remote.ItemDetailsRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ItemDetailsPageDataSourceFactory @Inject constructor(
    private val dataSource: ItemDetailsRemoteDataSource,
    private val keysdao: AllKeysDao,
    private val detailsdao: ItemDetailsDao,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, Itemdetail>() {

    private val itemsliveData = MutableLiveData<ItemDetailsPagedDataSource>()

    fun getMutableLiveData(): MutableLiveData<ItemDetailsPagedDataSource> {
        return itemsliveData
    }

    override fun create(): DataSource<Int, Itemdetail> {
        val source = ItemDetailsPagedDataSource(dataSource, keysdao, detailsdao, scope)
        itemsliveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 20

        fun pagedListConfig() = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }

}