package com.example.holloomap.presentation

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holloomap.R
import com.example.holloomap.data.local.entity.DestinationInfo
import com.example.holloomap.domain.use_case.MapUseCase
import com.example.holloomap.domain.use_case.location.LocationTracker
import com.example.holloomap.util.UiEvent
import com.example.holloomap.util.UiText
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

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
                state = state.copy(
                    title = UiText.StringResource(R.string.pin_destination)
                )
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
                    to = event.marker.position.latitude.toString() + "," + event.marker.position.longitude.toString(),
                    isDestinationAdded = true,
                )
                executeGetDirection()
            }

            is MapEvent.OnSaveDestination -> {
                saveDestination(state.destinationMarker.position)
            }

            is MapEvent.OnGetAllDestinations -> {

                viewModelScope.launch {
                    getAllDestinations()
                }
            }

            is MapEvent.OnShowTheListClick -> {
                state = state.copy(
                    listIsExpanded = !state.listIsExpanded,
                    shouldShowAllDestinationsOnMap = false
                )
                viewModelScope.launch {
                    getAllDestinations()
                }
            }

            is MapEvent.OnShowOnTheMapClick -> {
                state = state.copy(
                    listIsExpanded = false,
                    shouldShowAllDestinationsOnMap = true
                )
                viewModelScope.launch {
                    getAllDestinations()
                }
            }
        }
    }

    private suspend fun getAllDestinations() {

        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            val destinations = mapUseCase.getAllDestinations()

            destinations.onEach {
                state = state.copy(
                    allDestinations = it,
                )
            }

        }.launchIn(viewModelScope)
    }

    private fun saveDestination(destinationMarker: LatLng) {

        viewModelScope.launch(Dispatchers.IO) {
            mapUseCase.saveDestination(
                DestinationInfo(
                    lat = destinationMarker.latitude.toString(),
                    lon = destinationMarker.longitude.toString(),
                    uid = Random.nextInt()
                )
            )

            state = state.copy(
                isDestinationAdded = false,
                points = arrayListOf(),
                destinationMarker = MarkerState(),
                shouldShowAllDestinationsOnMap = false,
            )
            _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.destination_saved)))
        }

    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            currentLocation =
                withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                    locationTracker
                        .getCurrentLocation()
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