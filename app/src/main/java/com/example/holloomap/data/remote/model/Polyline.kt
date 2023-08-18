
package com.example.mapdirection.data.remote.model

import com.google.gson.annotations.SerializedName

data class Polyline(
        @SerializedName("points")
        var points: String?
)