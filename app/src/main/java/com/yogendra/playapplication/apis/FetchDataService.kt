package com.yogendra.playapplication.apis


import com.yogendra.playapplication.URL_ENDPOINT_1
import com.yogendra.playapplication.URL_ENDPOINT_2
import com.yogendra.playapplication.data.Itemdetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface FetchDataService {

    @GET(URL_ENDPOINT_1)
    suspend fun getAllKeys(): Response<List<Int>>

    @GET(URL_ENDPOINT_2)
    suspend fun getItemDetails(
        @Path("key") key: String
    ): Response<Itemdetail>


}


