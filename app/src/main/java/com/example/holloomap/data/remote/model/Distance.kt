package com.example.mapdirection.data.remote.model

import com.google.gson.annotations.SerializedName

data class Distance(
        @SerializedName("text")
        var text: String?,
        @SerializedName("value")
        var value: Int?
)