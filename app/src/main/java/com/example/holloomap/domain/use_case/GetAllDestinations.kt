package com.example.holloomap.domain.use_case

import com.example.holloomap.data.local.entity.DestinationInfo
import com.example.holloomap.data.repository.MapRepository
import kotlinx.coroutines.flow.Flow


class GetAllDestinations(
    private val repository: MapRepository
) {

     operator fun invoke() : Flow<List<DestinationInfo>> {

       return repository.getAllDestinations()
    }

}