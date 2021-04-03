package com.awesome.apps.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.awesome.apps.base.BaseViewModel
import com.awesome.apps.model.PhotosGaleryResponse
import com.awesome.apps.network.ApiRepository
import com.awesome.apps.network.CoroutineManager
import com.awesome.apps.network.model.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(
    override val coroutineManager: CoroutineManager,
    override val apiRepository: ApiRepository
) : BaseViewModel() {

    private val _loadingPhoto = MutableLiveData(Resource.Status.NULL)
    val loadingPhoto: LiveData<Resource.Status>
        get() = _loadingPhoto

    private val _dataPhoto = MutableLiveData<PhotosGaleryResponse>()
    val dataPhoto: LiveData<PhotosGaleryResponse>
        get() = _dataPhoto

    fun getDataPhotoGallery(page: Int?, perPage: Int?, query: String?) {
        _loadingPhoto.postValue(Resource.Status.LOADING)
        coroutineManager.ioScope.launch {
            try {
                val request = apiRepository.apiService?.getListPhoto(page, perPage, query)
                if (request != null) {
                    _loadingPhoto.postValue(Resource.Status.SUCCESS)
                    _dataPhoto.postValue(request)
                } else {
                    _loadingPhoto.postValue(Resource.Status.ERROR)
                }
            } catch (e: Exception) {
                _loadingPhoto.postValue(Resource.Status.ERROR)
            }
        }
    }


}