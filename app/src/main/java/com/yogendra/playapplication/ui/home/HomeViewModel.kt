package com.yogendra.playapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yogendra.playapplication.IS_INTERNET_AVAILABLE
import com.yogendra.playapplication.data.Itemdetail
import com.yogendra.playapplication.di.CoroutineScopeIO
import com.yogendra.playapplication.repository.DetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DetailsRepository,
    @CoroutineScopeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {

//    val articles: LiveData<PagedList<Itemdetail>>
//        get() = _articles

    val articles by lazy {
        repository.observePagedSets(
            IS_INTERNET_AVAILABLE,
            ioCoroutineScope
        )
    }


//    fun loadData(): LiveData<PagedList<Itemdetail>> {
//        return repository.observePagedSets(
//            IS_INTERNET_AVAILABLE,
//            ioCoroutineScope
//        )
//    }


    val progressStatus = repository.getProgressStatus()


    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }

}