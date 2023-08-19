package com.example.holloomap.data.repository

import com.example.holloomap.data.remote.MapApiService
import com.example.mapdirection.data.remote.model.DirectionResponses

class MapRepositoryImpl(
    private val api: MapApiService
) : MapRepository {

    override suspend fun getDirection(
        from: String,
        to: String,
        apiKey: String
    ): Result<DirectionResponses> {

        return try {
            val directionResponse = api.getDirectionCall(from, to, apiKey)
            Result.success(directionResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

}