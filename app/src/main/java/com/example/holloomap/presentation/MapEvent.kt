package com.example.holloomap.presentation

import com.example.holloomap.data.local.MarkerInfo
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState

sealed class MapEvent {

    data class OnUserLocationDetected(val marker: MarkerState) : MapEvent()

    data class OnDestinationMarkerAdded(val marker: MarkerState) : MapEvent()

    data class OnNewMarkerAdded(val marker: MarkerState) : MapEvent()

    data class OnSaveMarkerInfo(val marker: MarkerInfo) : MapEvent()
}