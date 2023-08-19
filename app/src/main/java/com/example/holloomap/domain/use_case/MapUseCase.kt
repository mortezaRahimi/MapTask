package com.example.holloomap.domain.use_case


data class MapUseCase(
     val getDirection: GetDirection,
     val saveDestination: SaveDestination,
     val getAllDestinations: GetAllDestinations
)
