package com.example.holloomap.presentation

import com.example.holloomap.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.Job

data class MapState(
    val from: String = "",
    val to: String = "",
    val apiKey: String = "AIzaSyAUC9K_PjKmTkOwdMofAvY8sY7epha0izo",
    val markerPoints: ArrayList<LatLng> = arrayListOf(),
    var points: List<LatLng> = listOf(),
    val originMarkerState: MarkerState = MarkerState(),
    val destinationMarker: MarkerState = MarkerState(),
    val userLocation:MarkerState = MarkerState(),
)