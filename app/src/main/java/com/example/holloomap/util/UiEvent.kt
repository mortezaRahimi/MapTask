package com.example.holloomap.util

import com.google.android.gms.maps.model.LatLng

sealed class UiEvent {
    data class ShowSnackbar(val message: UiText) : UiEvent()

    data class UserLocationDetected(val userLocation: LatLng) : UiEvent()
}
