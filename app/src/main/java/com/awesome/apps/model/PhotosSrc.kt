package com.awesome.apps.model

import com.google.gson.annotations.SerializedName

data class PhotosSrc(

    @SerializedName("original")
    var original: String? = "",

    @SerializedName("large")
    var large: String? = "",

    @SerializedName("medium")
    var medium: String? = "",

    @SerializedName("small")
    var small: String? = "",

    @SerializedName("portrait")
    var portrait: String? = "",

    @SerializedName("landscape")
    var landscape: String? = "",

    @SerializedName("tiny")
    var tiny: String? = ""

)