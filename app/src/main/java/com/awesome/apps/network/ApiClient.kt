package com.awesome.apps.network

import android.content.Context
import com.awesome.apps.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private var retrofit: Retrofit? = null

    companion object {
        fun instance(context: Context): Retrofit {
            return ApiClient().getClient(context)
        }
    }

    /**
     * Set base url API
     */
    private fun baseUrl(): String {
        return BuildConfig.BASE_URL
    }

    /**
     * Call service with retrofit
     */
    private fun getClient(context: Context): Retrofit {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        val requestInterceptor = Interceptor { chain: Interceptor.Chain ->
            val newReq = chain.request().newBuilder()
                .addHeader("Authorization", BuildConfig.API_KEY)
            chain.proceed(newReq.build())
        }

        val okhttpBuilder = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(requestInterceptor)
        if (BuildConfig.DEBUG) {
            okhttpBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl())
            .client(okhttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit!!
    }

}