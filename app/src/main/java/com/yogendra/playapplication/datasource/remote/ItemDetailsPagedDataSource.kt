package com.yogendra.playapplication.datasource.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.yogendra.playapplication.ProgressStatus
import com.yogendra.playapplication.data.Itemdetail
import com.yogendra.playapplication.data.Result
import com.yogendra.playapplication.datasource.local.AllKeysDao
import com.yogendra.playapplication.datasource.local.ItemDetailsDao
import kotlinx.coroutines.*
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

//        scope.launch {
        fetchData(params.requestedLoadSize) {
            callback.onResult(it, page + 1)
//            }
        }


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Itemdetail>) {
        val page = params.key

//        scope.launch {
        fetchData(params.requestedLoadSize) {
            callback.onResult(it, page - 1)
//            }
        }


    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Itemdetail>
    ) {
//        scope.launch {
        fetchData(params.requestedLoadSize) {
            callback.onResult(it, null, 2)//keysdao.getNextPageStartkey()?.toInt()
//            }
        }

    }


    private fun fetchData(pagesize: Int, callback: (List<Itemdetail>) -> Unit) {


        progressLiveStatus.postValue(ProgressStatus.LOADING.toString())

        val listofkeys = keysdao.getNextBatchOfKeys(pagesize, "$initialkey")
        val response: MutableList<Deferred<Result<Itemdetail>>> = mutableListOf()

        //Fetch details for each key and after succcess, update next page start to that key_value row id
        listofkeys.isNotEmpty().let {
            listofkeys.forEachIndexed { index, allkeys ->
                val key = allkeys.key_value
                response.add(scope.async {
                    Log.i("RequestDeferrred", "deferred Request added for key:$key")
                    dataSource.fetchDetailsForKey(key)

                })

            }

            val newItems: MutableList<Itemdetail> = mutableListOf()

            scope.launch(getJobErrorHandler()) {
                response.forEachIndexed { index, deferred ->
                    Log.i("RequestDeferrred", "deferred input index:$index")

                    processEachResponse(deferred.await())?.let { it1 ->
                        newItems.add(it1)
                        Log.i("RequestDeferrred", "deferred response added for key:${it1.id}")

                    }
                }

                Log.i("RequestDeferrred", "deferred response final list:$newItems")
                callback(newItems)
                progressLiveStatus.postValue(ProgressStatus.COMPLTED.toString())
            }

        }

        listofkeys.isEmpty().let {
            progressLiveStatus.postValue("No stories found to download details")
        }
    }


    suspend fun processEachResponse(vararg responseObject: Result<Itemdetail>): Itemdetail? {

        responseObject.forEach { response ->
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
        }

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
            progressLiveStatus.postValue(ProgressStatus.ERROR.toString())
        progressLiveStatus.postValue(message)
    }

}