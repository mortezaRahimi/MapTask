package com.example.holloomap.presentation

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holloomap.domain.use_case.location.LocationTracker
import com.example.holloomap.domain.use_case.MapUseCase
import com.example.holloomap.util.UiEvent
import com.example.holloomap.util.UiText
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
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
    private var currentLocation by mutableStateOf<Location?>(null)


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: MapEvent) {

        when (event) {
            is MapEvent.OnPermissionGranted -> {
                getCurrentLocation()
            }

            is MapEvent.OnUserLocationDetected -> {
                    state = state.copy(
                        originMarkerState = event.marker,
                        markerPoints = arrayListOf(event.marker.position),
                        from = event.marker.position.latitude.toString() + "," + event.marker.position.longitude.toString(),
                    )
            }

            is MapEvent.OnDestinationMarkerAdded -> {
                state = state.copy(
                    destinationMarker = event.marker,
                    markerPoints = arrayListOf(state.markerPoints[0], event.marker.position),
                    to = event.marker.position.latitude.toString() + "," + event.marker.position.longitude.toString()
                )
                executeGetDirection()
            }

            else -> {}
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            currentLocation =
                withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                    locationTracker
                        .getCurrentLocation(
                            onDisabled = { null },
                            onEnabled = { null })
                }

            if (currentLocation != null) {
                onEvent(
                    MapEvent.OnUserLocationDetected(
                        MarkerState(
                            LatLng(
                                currentLocation?.latitude ?: 0.0,
                                currentLocation?.longitude ?: 0.0
                            )
                        )
                    )
                )
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
    }

    private fun executeGetDirection() {
        viewModelScope.launch {
            mapUseCase.getDirection(state.from, state.to, state.apiKey)
                .onSuccess { directions ->
                    val shape = directions.routes?.get(0)?.overviewPolyline?.points

                    state = state.copy(
                        points = PolyUtil.decode(shape) as List<LatLng>,
                    )

                }
                .onFailure {
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.DynamicString(it.message.toString())
                        )
                    )
                }

        }
    }


}