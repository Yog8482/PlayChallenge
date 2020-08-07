package com.yogendra.playapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yogendra.playapplication.ProgressStatus
import com.yogendra.playapplication.data.Itemdetail
import com.yogendra.playapplication.data.Result
import com.yogendra.playapplication.datasource.ItemDetailsPageDataSourceFactory
import com.yogendra.playapplication.datasource.local.AllKeysDao
import com.yogendra.playapplication.datasource.local.ItemDetailsDao
import com.yogendra.playapplication.datasource.remote.ItemDetailsPagedDataSource
import com.yogendra.playapplication.datasource.remote.ItemDetailsRemoteDataSource
import com.yogendra.playapplication.datasource.remote.KeysRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepository @Inject constructor(
    private val remoteDataSource: ItemDetailsRemoteDataSource,
    private val keysDataSource: KeysRemoteDataSource,
    private val keysdao: AllKeysDao,
    private val detailsdao: ItemDetailsDao
) {
    var progressLoadStatus: LiveData<String> = MutableLiveData()



    fun observePagedSets(connectivityAvailable: Boolean, coroutineScope: CoroutineScope) =
        if (connectivityAvailable) observeRemotePagedSets(coroutineScope)
        else observeLocalPagedSets()

    private fun observeLocalPagedSets(): LiveData<PagedList<Itemdetail>> {
        val dataSourceFactory =
            detailsdao.getPagedDetails()

        return LivePagedListBuilder(
            dataSourceFactory,
            ItemDetailsPageDataSourceFactory.pagedListConfig()
        ).build()
    }

    private fun observeRemotePagedSets(ioCoroutineScope: CoroutineScope)
            : LiveData<PagedList<Itemdetail>> {
        val dataSourceFactory = ItemDetailsPageDataSourceFactory(
            remoteDataSource, keysdao,
            detailsdao, ioCoroutineScope
        )

        progressLoadStatus = Transformations.switchMap(
            dataSourceFactory.getMutableLiveData(),
            ItemDetailsPagedDataSource::getProgressLiveStatus
        )


        return LivePagedListBuilder(
            dataSourceFactory,
            ItemDetailsPageDataSourceFactory.pagedListConfig()
        ).build()
    }

    fun getProgressStatus(): LiveData<String> {
        return progressLoadStatus
    }
}