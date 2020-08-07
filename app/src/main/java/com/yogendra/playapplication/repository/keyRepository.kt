package com.yogendra.playapplication.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yogendra.playapplication.ProgressStatus
import com.yogendra.playapplication.data.Allkeys
import com.yogendra.playapplication.data.Result
import com.yogendra.playapplication.datasource.local.AllKeysDao
import com.yogendra.playapplication.datasource.remote.KeysRemoteDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeysRepository @Inject constructor(
    private val remoteDataSource: KeysRemoteDataSource,
    private val keysDao: AllKeysDao
) {
    val keyDownloadStatus: MutableLiveData<String> = MutableLiveData()

    fun downloadTopstories(scope: CoroutineScope) {
        scope.launch(getJobErrorHandler()) {
            keyDownloadStatus.postValue(ProgressStatus.LOADING.toString())

            val response = remoteDataSource.fetchKeys()
            if (response.status == Result.Status.SUCCESS) {
                val results = response.data!!

                val alltopStories = results//.getList()
                alltopStories.isNotEmpty().let {
                    alltopStories.forEachIndexed { index, s ->
                        keysDao.insertAll(listOf(Allkeys(index,key_value = "$s")))
                    }
                }
                alltopStories.isEmpty().let {
                    keyDownloadStatus.postValue("No stories found")
                }
                keyDownloadStatus.postValue(ProgressStatus.COMPLTED.toString())


            } else if (response.status == Result.Status.ERROR) {
                postError(response.message!!)
            }

        }

    }


    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        postError(e.message ?: e.toString())
    }

    private fun postError(message: String) {
        Log.e("KeysRepository", "An error happened: $message")
        // TODO network error handling
        if (message.contains("Unable to resolve host")) {
            keyDownloadStatus.postValue(ProgressStatus.NO_NETWORK.toString())
        } else
            keyDownloadStatus.postValue(ProgressStatus.ERROR.toString())
        keyDownloadStatus.postValue(message)
    }

}
