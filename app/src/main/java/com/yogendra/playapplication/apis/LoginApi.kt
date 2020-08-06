package com.yogendra.playapplication.apis

import com.yogendra.playapplication.data.requests.LoginRequest
import com.yogendra.playapplication.data.responses.LoginResponseFailure
import com.yogendra.playapplication.data.responses.LoginResponseSuccess
import com.yogendra.playapplication.interceptor.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/v1/200/login")
    suspend fun userLoginSuccess(
        @Body
        loginRequest: LoginRequest
    ): Response<LoginResponseSuccess>

    @POST("/v1/401/login")
    suspend fun userLoginfail401(
        @Body
        loginRequest: LoginRequest
    ): Response<LoginResponseFailure>

    @POST("/v1/400/login")
    suspend fun userLoginfail400(
    ): Response<LoginResponseFailure>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            logging_interceptor: HttpLoggingInterceptor
        ): LoginApi {

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(logging_interceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://256b758e-ed3e-400c-8ece-10dc78851f7a.mock.pstmn.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginApi::class.java)
        }
    }
}