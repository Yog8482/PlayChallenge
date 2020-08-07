package com.yogendra.playapplication.datasource.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.yogendra.playapplication.ProgressStatus
import com.yogendra.playapplication.data.Itemdetail
import com.yogendra.playapplication.data.Result
import com.yogendra.playapplication.datasource.local.AllKeysDao
import com.yogendra.playapplication.datasource.local.ItemDetailsDao
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItemDetailsPagedDataSource @Inject constructor(
    private val dataSource: ItemDetailsRemoteDataSource,
    private val keysdao: AllKeysDao,
    private val detailsdao: ItemDetailsDao,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Itemdetail>() {

    private val progressLiveStatus: MutableLiveData<String> = MutableLiveData()
    var initialkey = keysdao.getNextPageStartkey()

    fun getProgressLiveStatus(): MutableLiveData<String> {
        return progressLiveStatus
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Itemdetail>) {
        val page = params.key
        fetchData(params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Itemdetail>) {
        val page = params.key
        fetchData(params.requestedLoadSize) {
            callback.onResult(it, page - 1)
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Itemdetail>
    ) {

        fetchData(params.requestedLoadSize) {
            callback.onResult(it, null, keysdao.getNextPageStartkey()?.toInt())
        }
    }


    private fun fetchData(pagesize: Int, callback: (List<Itemdetail>) -> Unit) {


        progressLiveStatus.postValue(ProgressStatus.LOADING.toString())

        val newItems: MutableList<Itemdetail> = mutableListOf<Itemdetail>()
        val listofkeys = keysdao.getNextBatchOfKeys(pagesize, "$initialkey")
        //Fetch details for each key and after succcess, update next page start to that key_value row id
        listofkeys.isNotEmpty().let {
            listofkeys.forEachIndexed { index, allkeys ->
                var ispageEnd = false
                if (index == listofkeys.size - 1)
                    ispageEnd = true

                val key = allkeys.key_value

                scope.launch(getJobErrorHandler()) {

                    val response = dataSource.fetchDetailsForKey(key)
                    if (response.status == Result.Status.SUCCESS) {
                        val results = response.data!!
                        detailsdao.insertItem(results)
                        newItems.add(results)
                        val rowid = keysdao.getRowidBykeyvalue(key)
                        keysdao.updateNextPageAllkeys("$rowid")
                        if (ispageEnd) {
                            progressLiveStatus.postValue(ProgressStatus.COMPLTED.toString())
                            callback(newItems)

                        }

                    } else if (response.status == Result.Status.ERROR) {
                        postError(response.message!!)
                    }

                }
            }

        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        postError(e.message ?: e.toString())
    }

    private fun postError(message: String) {
        Log.e("ItemDetailsPagedDS", "An error happened: $message")
        // TODO network error handling
        if (message.contains("Unable to resolve host")) {
            progressLiveStatus.postValue(ProgressStatus.NO_NETWORK.toString())
        } else
            progressLiveStatus.postValue(ProgressStatus.ERROR.toString())
        progressLiveStatus.postValue(message)
    }

}