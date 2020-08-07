package com.yogendra.playapplication.datasource.remote

import com.yogendra.playapplication.apis.FetchDataService
import com.yogendra.playapplication.datasource.BaseDataSource
import javax.inject.Inject

class KeysRemoteDataSource @Inject constructor(
    private val service: FetchDataService
) :
    BaseDataSource() {
    suspend fun fetchKeys() = getResult {
        service.getAllKeys()
    }
}

class ItemDetailsRemoteDataSource @Inject constructor(private val service: FetchDataService) :
    BaseDataSource() {
    suspend fun fetchDetailsForKey(key: String) =
        getResult { service.getItemDetails(key) }
}