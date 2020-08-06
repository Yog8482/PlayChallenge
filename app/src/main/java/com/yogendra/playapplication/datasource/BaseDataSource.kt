package com.yogendra.playapplication.datasource

import retrofit2.Response
import  com.yogendra.playapplication.data.*
import com.yogendra.playapplication.utilities.NoInternetException


/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>):Result<T> {
        try {
//            Result.loading(true)
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null)

                    return Result.success(body)
            }
            return error("${response.body()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        if (message.contains("Unable to resolve host")) {
            return Result.error(NoInternetException().message.toString())
        }
        return Result.error(message)
    }

}

