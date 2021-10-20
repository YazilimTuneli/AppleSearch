package com.enes.appleapi.model

import com.google.gson.annotations.SerializedName

data class Search(
    @JvmField
    @SerializedName("resultCount")
    var resultCount: Int? = null,
    @JvmField
    @SerializedName("results")
    var results: List<Results>? ) : BaseModel()