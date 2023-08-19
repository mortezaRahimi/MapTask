package com.example.holloomap.data.repository

import com.example.holloomap.data.local.MapDao
import com.example.holloomap.data.local.entity.DestinationInfo
import com.example.holloomap.data.remote.MapApiService
import com.example.mapdirection.data.remote.model.DirectionResponses
import kotlinx.coroutines.flow.Flow

class MapRepositoryImpl(
    private val api: MapApiService,
    private val dao: MapDao
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

    override suspend fun saveDestination(destinationInfo: DestinationInfo) {
        dao.insertDestination(destinationInfo)
    }

    override fun getAllDestinations(): Flow<List<DestinationInfo>> {
        return dao.getAllDestinations()
    }


}