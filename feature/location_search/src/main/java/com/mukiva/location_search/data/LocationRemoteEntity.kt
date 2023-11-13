package com.mukiva.location_search.data

import com.google.gson.annotations.SerializedName

data class LocationRemoteEntity(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("region")
    var region: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lon")
    var lon: Double? = null,
    @SerializedName("url")
    var url: String? = null
)