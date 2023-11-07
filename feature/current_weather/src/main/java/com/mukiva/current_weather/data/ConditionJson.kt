package com.mukiva.current_weather.data

import com.google.gson.annotations.SerializedName

data class ConditionJson(

    @SerializedName("text")
    var text: String? = null,
    @SerializedName("icon")
    var icon: String? = null,
    @SerializedName("code")
    var code: Int? = null

)
