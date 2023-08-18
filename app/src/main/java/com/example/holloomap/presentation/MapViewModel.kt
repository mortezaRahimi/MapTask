package com.example.holloomap.presentation

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holloomap.domain.location.LocationTracker
import com.example.holloomap.domain.use_case.MapUseCase
import com.example.holloomap.util.UiEvent
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapUseCase: MapUseCase,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var state by mutableStateOf(MapState())
    var currentLocation by mutableStateOf<Location?>(null)


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun getCurrentLocation() {

        viewModelScope.launch {
            currentLocation =
                withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                    locationTracker
                        .getCurrentLocation(
                            onDisabled = { null },
                            onEnabled = { null })
                }


            state = state.copy(
                originMarkerState = MarkerState(position = with(currentLocation) {
                    LatLng(
                        this?.latitude ?: 0.0,
                        this?.longitude ?: 0.0
                    )
                })
            )

            _uiEvent.send(
                UiEvent.UserLocationDetected(
                    LatLng(
                        currentLocation?.latitude ?: 0.0,
                        currentLocation?.longitude ?: 0.0
                    )
                )
            )
        }


    }

    fun onEvent(event: MapEvent) {

        when (event) {
            is MapEvent.OnPermissionGranted -> {
                getCurrentLocation()
            }

            else -> {}
        }
    }

}