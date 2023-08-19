package com.example.holloomap.domain.use_case

import com.example.holloomap.domain.use_case.GetDirection

data class MapUseCase(
     val getDirection: GetDirection,
     val saveDestination: SaveDestination,
     val getAllDestinations: GetAllDestinations
)
