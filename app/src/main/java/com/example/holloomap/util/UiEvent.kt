package com.example.holloomap.util

sealed class UiEvent {
    data class ShowSnackbar(val message: UiText) : UiEvent()

    object SimulateDriving : UiEvent()
}
