package com.yumedev.seijakulistkmp.features.detail.presentation

import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry

data class DetailState(
    val mediaDetail: MediaDetail? = null,
    val listEntry: MediaListEntry? = null,
    val isLoading: Boolean = false,
    val error: ErrorType? = null
)
