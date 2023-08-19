package com.example.holloomap.data.repository

import com.example.holloomap.data.local.entity.DestinationInfo
import com.example.mapdirection.data.remote.model.DirectionResponses
import kotlinx.coroutines.flow.Flow

interface MapRepository {

    suspend fun getDirection(
        from: String,
        to: String,
        apiKey: String
    ): Result<DirectionResponses>

    suspend fun saveDestination(
        destinationInfo: DestinationInfo
    )

     fun getAllDestinations(): Flow<List<DestinationInfo>>
}