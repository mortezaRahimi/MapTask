package com.example.holloomap.presentation

import com.google.maps.android.compose.MarkerState

sealed class MapEvent {

    data class OnUserLocationDetected(val marker: MarkerState) : MapEvent()

    object OnPermissionGranted : MapEvent()

    data class OnDestinationMarkerAdded(val marker: MarkerState) : MapEvent()

    object OnSaveDestination : MapEvent()

    object OnGetAllDestinations: MapEvent()

    object OnShowTheListClick: MapEvent()
    object OnShowOnTheMapClick: MapEvent()
}