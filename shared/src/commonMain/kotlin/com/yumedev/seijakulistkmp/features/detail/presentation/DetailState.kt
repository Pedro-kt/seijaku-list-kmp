package com.yumedev.seijakulistkmp.features.detail.presentation

import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail

data class DetailState(
    val mediaDetail: MediaDetail? = null,
    val isLoading: Boolean = false,
    val error: ErrorType? = null
)
