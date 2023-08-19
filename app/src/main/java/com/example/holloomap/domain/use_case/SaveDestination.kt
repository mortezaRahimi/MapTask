package com.example.holloomap.domain.use_case

import com.example.holloomap.data.local.entity.DestinationInfo
import com.example.holloomap.data.repository.MapRepository
import com.example.mapdirection.data.remote.model.DirectionResponses


class SaveDestination(
    private val repository: MapRepository
) {

    suspend operator fun invoke(destinationInfo: DestinationInfo) {

        repository.saveDestination(destinationInfo)
    }

}