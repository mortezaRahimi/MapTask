package com.example.holloomap.domain.use_case

import com.example.holloomap.data.repository.MapRepository
import com.example.mapdirection.data.remote.model.DirectionResponses


class GetDirection(
    private val repository: MapRepository
) {

    suspend operator fun invoke(
        from: String,
        to: String,
        apiKey: String
    ): Result<DirectionResponses> {
        return repository.getDirection(
            from,
            to,
            apiKey
        )

    }

}