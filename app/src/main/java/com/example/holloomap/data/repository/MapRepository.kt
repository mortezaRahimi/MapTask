package com.example.holloomap.data.repository

import com.example.holloomap.data.local.MarkerInfo
import com.example.mapdirection.data.remote.model.DirectionResponses

interface MapRepository {

    suspend fun getDirection(
        from: String,
        to: String,
        apiKey: String
    ): Result<DirectionResponses>

//    suspend  fun getMarkers():List<MarkerInfo>
}