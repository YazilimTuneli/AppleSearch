package com.enes.appleapi.model

import com.google.gson.annotations.SerializedName

data class Results(
    @JvmField
    @SerializedName("wrapperType")
    var wrapperType: String? = "",
    @JvmField
    @SerializedName("kind")
    var kind: String? = "",
    @JvmField
    @SerializedName("artistId")
    var artistId: Int? = null,
    @JvmField
    @SerializedName("collectionId")
    var collectionId: Long? = null,
    @JvmField
    @SerializedName("trackId")
    var trackId: Long? = null,
    @JvmField
    @SerializedName("artistName")
    var artistName: String? = "",
    @JvmField
    @SerializedName("collectionName")
    var collectionName: String? = "",
    @JvmField
    @SerializedName("trackName")
    var trackName: String? = "",
    @JvmField
    @SerializedName("collectionCensoredName")
    var collectionCensoredName: String? = "",
    @JvmField
    @SerializedName("trackCensoredName")
    var trackCensoredName: String? = "",
    @JvmField
    @SerializedName("artistViewUrl")
    var artistViewUrl: String? = "", //apple yonlendirme
    @JvmField
    @SerializedName("collectionViewUrl")
    var collectionViewUrl: String? = "", //apple liste
    @JvmField
    @SerializedName("trackViewUrl")
    var trackViewUrl: String? = "", //apple track
    @JvmField
    @SerializedName("previewUrl")
    var previewUrl: String? = "", //sound
    @JvmField
    @SerializedName("artworkUrl100")
    var artworkUrl100: String? = "", //image url
    @JvmField
    @SerializedName("collectionPrice")
    var collectionPrice: Float? = 0.0f, //price
    @JvmField
    @SerializedName("trackPrice")
    var trackPrice: Float? = 0.0f, //price
    @JvmField
    @SerializedName("releaseDate")
    var releaseDate: String? = "", //releaseDate
    @JvmField
    @SerializedName("trackTimeMillis")
    var trackTimeMillis: Long? = 0L, //trackTimeMillis
    @JvmField
    @SerializedName("country")
    var country: String? = "", //country
    @JvmField
    @SerializedName("currency")
    var currency: String? = "", //currency
    @JvmField
    @SerializedName("primaryGenreName")
    var primaryGenreName: String? = "", //primaryGenreName
    @JvmField
    @SerializedName("shortDescription")
    var shortDescription: String? = "", //shortDescription
    @JvmField
    @SerializedName("longDescription")
    var longDescription: String? = "", //longDescription

) : BaseModel()