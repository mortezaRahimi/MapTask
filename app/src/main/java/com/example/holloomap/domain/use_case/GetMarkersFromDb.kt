package com.example.holloomap.domain.use_case

import com.example.holloomap.data.local.MarkerInfo
import com.example.holloomap.data.repository.MapRepository
import com.example.mapdirection.data.remote.model.DirectionResponses

class GetMarkersFromDb(
    private val repository: MapRepository
) {

//    suspend operator fun invoke(): List<MarkerInfo> {
//        return repository.getMarkers()
//    }
}