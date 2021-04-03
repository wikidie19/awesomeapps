package com.awesome.apps.feature

import com.awesome.apps.feature.detail.DetailPhotoViewModel
import com.awesome.apps.feature.home.HomeViewModel
import com.awesome.apps.network.ApiRepository
import com.awesome.apps.network.CoroutineManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { ApiRepository(androidApplication()) }
    single { CoroutineManager() }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { DetailPhotoViewModel(get(), get()) }

}