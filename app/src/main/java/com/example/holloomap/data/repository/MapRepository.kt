package com.example.holloomap.data.repository

import com.example.mapdirection.data.remote.model.DirectionResponses

interface MapRepository {

    suspend fun getDirection(
        from: String,
        to: String,
        apiKey: String
    ): Result<DirectionResponses>
}