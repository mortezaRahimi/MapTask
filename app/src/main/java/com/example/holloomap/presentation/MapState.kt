package com.example.holloomap.presentation

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.example.holloomap.R
import com.example.holloomap.data.local.entity.DestinationInfo
import com.example.holloomap.domain.use_case.GetAllDestinations
import com.example.holloomap.util.UiText

data class MapState(
    val from: String = "",
    val to: String = "",
    val apiKey: String = "AIzaSyAUC9K_PjKmTkOwdMofAvY8sY7epha0izo",
    val markerPoints: ArrayList<LatLng> = arrayListOf(),
    var points: List<LatLng> = listOf(),
    val destinationMarker: MarkerState = MarkerState(),
    val originMarkerState: MarkerState = MarkerState(),
    val title: UiText = UiText.StringResource(R.string.pin_destination),
    val isDestinationAdded: Boolean = false,
    val shouldShowAllDestinations: Boolean = false,
    val shouldShowAllDestinationsOnMap: Boolean = false,
    val allDestinations: List<DestinationInfo> = arrayListOf(),
    val listIsExpanded: Boolean = false
)
