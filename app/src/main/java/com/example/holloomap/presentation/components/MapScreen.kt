package com.example.holloomap.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.holloomap.presentation.MapViewModel
import com.example.holloomap.util.UiEvent
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {

    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(35.715298, 51.404343), 10f)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }

                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMapView(
            scaffoldState = scaffoldState,
            cameraPositionState = cameraPositionState,
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(),
            uiSettings = MapUiSettings(),
        )

    }

}