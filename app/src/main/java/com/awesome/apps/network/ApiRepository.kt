package com.awesome.apps.network

import android.content.Context
import com.awesome.apps.helper.NetworkHelper
import retrofit2.Retrofit

class ApiRepository(private val context: Context) {

    var apiService: ApiInterface? = null
    var retrofit: Retrofit? = null

    init {
        retrofit = ApiClient.instance(context)
        apiService = retrofit?.create(ApiInterface::class.java)
    }

    /**
     * Check available network connection
     */
    fun isNetworkAvailable(): Boolean {
        return NetworkHelper.isConnected(context)
    }


}