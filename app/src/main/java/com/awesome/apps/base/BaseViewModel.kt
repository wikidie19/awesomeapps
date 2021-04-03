package com.awesome.apps.base

import androidx.lifecycle.ViewModel
import com.awesome.apps.network.ApiRepository
import com.awesome.apps.network.CoroutineManager

abstract class BaseViewModel: ViewModel() {

    abstract val coroutineManager: CoroutineManager
    abstract val apiRepository: ApiRepository

}