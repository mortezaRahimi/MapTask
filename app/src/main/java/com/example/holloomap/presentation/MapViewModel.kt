package com.example.holloomap.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.holloomap.domain.use_case.MapUseCase
import com.example.holloomap.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapUseCase: MapUseCase,
) : ViewModel() {

    var state by mutableStateOf(MapState())


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: MapEvent) {

    }

}