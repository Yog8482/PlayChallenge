package com.yogendra.playapplication.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yogendra.playapplication.data.Itemdetail
import com.yogendra.playapplication.datasource.local.AllKeysDao
import com.yogendra.playapplication.datasource.local.ItemDetailsDao
import com.yogendra.playapplication.datasource.remote.ItemDetailsRemoteDataSource
import com.yogendra.playapplication.di.CoroutineScopeIO
import com.yogendra.playapplication.interceptor.NetworkConnectionInterceptor
import com.yogendra.playapplication.repository.DetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val context: Context,
    private val repository: DetailsRepository,
    @CoroutineScopeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {

    val articles: LiveData<PagedList<Itemdetail>>
        get() = _articles


    private var _articles = repository.observePagedSets(
        NetworkConnectionInterceptor(context).isInternetAvailable(),
        ioCoroutineScope
    )


    fun loadData(){
        _articles = repository.observePagedSets(
            NetworkConnectionInterceptor(context).isInternetAvailable(),
            ioCoroutineScope
        )
    }



    val progressStatus = repository.getProgressStatus()


    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }

}