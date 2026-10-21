package com.yumedev.seijakulistkmp.features.detail.presentation

sealed class DetailEvent {
    data class ShowError(val message: String) : DetailEvent()
    data class ShowSuccess(val message: String) : DetailEvent()
}
