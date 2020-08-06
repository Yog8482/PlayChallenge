package com.yogendra.playapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yogendra.playapplication.LoginMockApiCall
import com.yogendra.playapplication.ProgressStatus
import com.yogendra.playapplication.data.Result
import com.yogendra.playapplication.data.requests.LoginRequest
import com.yogendra.playapplication.datasource.remote.LoginRemoteDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepository @Inject constructor(private val remoteDataSource: LoginRemoteDataSource) {

    val progressLoadStatus: LiveData<String>
        get() = _progressLoadStatus
    private val _progressLoadStatus: MutableLiveData<String> = MutableLiveData()

    fun getProgressStatus(): LiveData<String> {
        return progressLoadStatus
    }

    fun invalidateProgressStatus(){
        return  _progressLoadStatus.postValue(ProgressStatus.COMPLTED.toString())
    }

    fun performLogin(coroutineScope: CoroutineScope, mockApiType: LoginMockApiCall, loginRequest: LoginRequest) {
        _progressLoadStatus.postValue(ProgressStatus.LOADING.toString())

        coroutineScope.launch(getJobErrorHandler()) {


            when (mockApiType) {
                LoginMockApiCall.SUCCESS -> {
                    val response = remoteDataSource.userLoginSuccess(loginRequest)
                    if (response.status == Result.Status.SUCCESS) {
                        val results = response.data!!
                        _progressLoadStatus.postValue("Login Success & token is ${results.token}")

                    } else if (response.status == Result.Status.ERROR) {
                        _progressLoadStatus.postValue("Login failed, Please try again")

                        postError(response.message!!)
                    }
                }
                LoginMockApiCall.BAD_REQUEST -> {
                    val response = remoteDataSource.userLoginfail400()
                    if (response.status == Result.Status.SUCCESS) {
                        val results = response.data!!
                        _progressLoadStatus.postValue("Error: ${results.error}, Description: ${results.description}")

                    } else if (response.status == Result.Status.ERROR) {
                        _progressLoadStatus.postValue("Login failed. Error: ${response.data}")
                        postError(response.message!!)
                    }
                }
                LoginMockApiCall.UN_AUTHORIZED -> {
                    val response = remoteDataSource.userLoginfail401(loginRequest)
                    if (response.status == Result.Status.SUCCESS) {
                        val results = response.data!!
                        _progressLoadStatus.postValue("Error: ${results.error}, Description: ${results.description}")

                    } else if (response.status == Result.Status.ERROR) {
                        _progressLoadStatus.postValue("Login failed. Error: ${response.message}")
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
        Log.e("LoginRepository", "An error happened: $message")
        // TODO network error handling
        if (message.contains("Unable to resolve host")) {
            _progressLoadStatus.postValue(ProgressStatus.NO_NETWORK.toString())
        } else
            _progressLoadStatus.postValue(message)
    }
}