package com.awesome.apps.model

import com.google.gson.annotations.SerializedName

data class PhotosGallery(
    @SerializedName("id")
    var id: Int? = 0,

    @SerializedName("width")
    var width: Int? = 0,

    @SerializedName("height")
    var height: Int? = 0,

    @SerializedName("url")
    var url: String? = "",

    @SerializedName("photographer")
    var photographer: String? = "",

    @SerializedName("photographer_url")
    var photographerUrl: String? = "",

    @SerializedName("photographer_id")
    var photographerId: Int? = 0,

    @SerializedName("src")
    var src: PhotosSrc? = null,

    @SerializedName("liked")
    var linked: Boolean? = false
)