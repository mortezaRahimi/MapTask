
package com.example.mapdirection.data.remote.model

import com.google.gson.annotations.SerializedName

data class OverviewPolyline(
        @SerializedName("points")
        var points: String?
)