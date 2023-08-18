package com.example.holloomap.presentation.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.core.content.ContextCompat
import com.example.holloomap.presentation.MapEvent
import com.example.holloomap.presentation.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun GoogleMapView(
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel,
    properties: MapProperties,
    uiSettings: MapUiSettings,
) {

    var isMapLoaded by remember { mutableStateOf(false) }
    val context = LocalContext.current

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

//            Button(onClick = {
//                //check permission and if has  -check location is turned on or not then  get user location
//            }) {
//
//            }

            Marker(
                state = MarkerState(position = viewModel.state.userLocation.position),
                icon = BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE
                ),
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

}