package com.awesome.apps.model

import com.google.gson.annotations.SerializedName

data class PhotosGaleryResponse(

    @SerializedName("page")
    var page: Int? = 0,

    @SerializedName("per_page")
    var perPage: Int? = 0,

    @SerializedName("total_results")
    var totalResults: Int? = 0,

    @SerializedName("photos")
    var photos: MutableList<PhotosGallery>? = mutableListOf()

)