package com.awesome.apps.feature.detail

import com.awesome.apps.base.BaseViewModel
import com.awesome.apps.network.ApiRepository
import com.awesome.apps.network.CoroutineManager

class DetailPhotoViewModel(
    override val coroutineManager: CoroutineManager,
    override val apiRepository: ApiRepository
) : BaseViewModel() {

}