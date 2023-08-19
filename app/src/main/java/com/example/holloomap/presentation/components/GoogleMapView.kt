package com.example.holloomap.presentation.components

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.google.android.gms.maps.model.LatLng


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
                onMapLongClick = {
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
                        title = stringResource(R.string.you_are_here),
                        visible = true
                    )

                    Marker(
                        state = MarkerState(position = viewModel.state.destinationMarker.position),
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                        title = stringResource(R.string.your_destination),
                        visible = true
                    )

                    Polyline(
                        points = viewModel.state.points,
                        width = 20f,
                        color = Color.Green
                    )

                    if (viewModel.state.allDestinations.isNotEmpty() && viewModel.state.shouldShowAllDestinationsOnMap)
                        viewModel.state.allDestinations.forEach { position ->
                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        position.lat.toDouble(),
                                        position.lon.toDouble()
                                    )
                                ),
                                title = position.lat + " " + position.lon,
                                visible = true
                            )
                        }


                }
            }

            ActionView(
                title = UiText.DynamicString(viewModel.state.title.asString(context)),
                saveDestination = { viewModel.onEvent(MapEvent.OnSaveDestination) },
                isDestinationAdded = viewModel.state.isDestinationAdded,
                showOnMap = { viewModel.onEvent(MapEvent.OnShowOnTheMapClick) },
                showTheList = { viewModel.onEvent(MapEvent.OnShowTheListClick) }
            )

            AnimatedVisibility(visible = viewModel.state.listIsExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colors.background
                        )
                        .clickable(enabled = true) {  }
                ) {

                    Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = { viewModel.onEvent(MapEvent.OnShowTheListClick) }) {
                        Text(text = stringResource(R.string.close))
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    viewModel.state.allDestinations.forEach { destinations ->
                        Text(
                            modifier = Modifier
                                .padding(8.dp),
                            text = "Latitude: " + destinations.lat + "\n" + "Longitude: " + destinations.lon,
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.caption,
                        )
                    }
                }
            }


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