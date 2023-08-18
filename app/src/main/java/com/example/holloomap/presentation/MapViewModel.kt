package com.example.holloomap.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holloomap.domain.use_case.MapUseCase
import com.example.holloomap.util.UiEvent
import com.example.holloomap.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.example.holloomap.R
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapUseCase: MapUseCase,
) : ViewModel() {

    var state by mutableStateOf(MapState())


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: MapEvent) {

        when (event) {
            is MapEvent.OnPermissionGranted -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.granted)))
                }

            }

            else -> {}
        }
    }

}