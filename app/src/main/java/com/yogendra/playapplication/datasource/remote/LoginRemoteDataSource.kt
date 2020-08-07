package com.yogendra.playapplication.datasource.remote

import com.yogendra.playapplication.apis.LoginApi
import com.yogendra.playapplication.data.requests.LoginRequest
import com.yogendra.playapplication.datasource.BaseDataSource
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(private val service: LoginApi) :
    BaseDataSource() {
    suspend fun userLoginSuccess(loginRequest: LoginRequest) =
        getResult { service.userLoginSuccess(loginRequest) }

    suspend fun userLoginfail401(loginRequest: LoginRequest) =
        getResult { service.userLoginfail401(loginRequest) }

    suspend fun userLoginfail400() =
        getResult { service.userLoginfail400() }
}
