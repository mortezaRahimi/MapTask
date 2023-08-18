package com.example.holloomap.data.remote

import com.example.holloomap.data.remote.UrlConstant.GET_DIRECTION
import com.example.mapdirection.data.remote.model.DirectionResponses
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApiService {
    @GET(GET_DIRECTION)
    suspend fun getDirectionCall(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): DirectionResponses
}