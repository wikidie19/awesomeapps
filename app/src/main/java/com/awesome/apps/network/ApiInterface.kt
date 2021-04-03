package com.awesome.apps.network

import com.awesome.apps.model.PhotosGaleryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/v1/search")
    suspend fun getListPhoto(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("query") query: String?
    ): PhotosGaleryResponse?

}