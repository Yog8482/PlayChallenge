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
            callback.onResult(it, null, 2)
        }

    }


    fun fetchData(pagesize: Int, callback: (List<Itemdetail>) -> Unit) {
        scope.launch(getJobErrorHandler()) {
            progressLiveStatus.postValue("downloading stories details")

            progressLiveStatus.postValue(ProgressStatus.LOADING.toString())

            val listofkeys = keysdao.getNextBatchOfKeys(pagesize, "$initialkey")
            val newItems: MutableList<Itemdetail> = mutableListOf()

//            scope.async {

            listofkeys.isNotEmpty().let {

                listofkeys.forEachIndexed { index, allkeys ->
                    progressLiveStatus.postValue(ProgressStatus.LOADING.toString())

                    val key = allkeys.key_value
                    Log.i("RequestDeferrred", "deferred Request added for key:$key")
                    val result = dataSource.fetchDetailsForKey(key)
                    processEachResponse(result)?.let { it1 ->
                        newItems.add(it1)
                    }


                }

                callback(newItems)
                Log.i("RequestDeferrred", "deferred response final list:$newItems")
                progressLiveStatus.postValue(ProgressStatus.COMPLTED.toString())
                progressLiveStatus.postValue("download complete")

            }

            if(listofkeys.isEmpty()) {
                progressLiveStatus.postValue("No stories found to download details")
            }
//            }.await()

        }

    }

    private fun processEachResponse(response: Result<Itemdetail>): Itemdetail? {

//        responseObject.forEach { response ->
        if (response.status == Result.Status.SUCCESS) {
            response.data?.let { results ->
                detailsdao.insertItem(results)
                val rowid = keysdao.getRowidBykeyvalue(results.id)
                keysdao.updateNextPageAllkeys("$rowid")
                return results
            }

        } else if (response.status == Result.Status.ERROR) {
            postError(response.message!!)
        }
//        }

        return null
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        postError(e.message ?: e.toString())
    }

    private fun postError(message: String) {
        Log.e("ItemDetailsPagedDS", "An error happened: $message")
        // TODO network error handling
        if (message.contains("Unable to connect host")) {
            progressLiveStatus.postValue(ProgressStatus.NO_NETWORK.toString())
        } else
//            progressLiveStatus.postValue(ProgressStatus.ERROR.toString())
            progressLiveStatus.postValue(message)
    }

}