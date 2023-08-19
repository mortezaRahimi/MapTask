package com.example.holloomap.presentation.components

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.holloomap.presentation.MapEvent
import com.example.holloomap.presentation.MapViewModel
import com.example.holloomap.util.UiEvent
import com.example.holloomap.util.isLocationEnabled
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.example.holloomap.R
import com.example.holloomap.util.UiText


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GoogleMapView(
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel,
    properties: MapProperties,
    uiSettings: MapUiSettings,
    scaffoldState: ScaffoldState
) {

    var isMapLoaded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(key1 = true) {

        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }

                is UiEvent.UserLocationDetected -> {
                    if (event.userLocation.latitude != 0.0)
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newCameraPosition(
                                CameraPosition(event.userLocation, 12f, 0f, 0f)
                            ),
                            durationMs = 1000
                        )
                }
                else -> {}
            }
        }
    }

    if (locationPermissionsState.allPermissionsGranted) {

        if (isLocationEnabled(context)) {
            GoogleMap(
                modifier = modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
                onMapLoaded = { isMapLoaded = true },
                onMapClick = {
                    viewModel.onEvent(MapEvent.OnDestinationMarkerAdded(MarkerState(it)))
                }
            ) {

                if (isMapLoaded) {
                    viewModel.onEvent(MapEvent.OnPermissionGranted)

                    Marker(
                        state = MarkerState(position = viewModel.state.originMarkerState.position),
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_BLUE
                        ),
                        snippet = "Your Location",

                        )

                    Marker(
                        state = MarkerState(position = viewModel.state.destinationMarker.position),
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    )

                    Polyline(
                        points = viewModel.state.points,
                        width = 20f,
                        color = Color.Green
                    )


                }
            }

            ActionView(
                title = UiText.DynamicString(viewModel.state.title.asString(context)),
                saveDestination = { viewModel.onEvent(MapEvent.OnSaveDestination) },
                isDestinationAdded = viewModel.state.isDestinationAdded,
                getAllDes = { viewModel.onEvent(MapEvent.OnGetAllDestinations) }
            )


        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = context.getString(R.string.gps_alert),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                )
            }
        }
    } else {
        BeforePermissionView(locationPermissionsState = locationPermissionsState)
    }


}