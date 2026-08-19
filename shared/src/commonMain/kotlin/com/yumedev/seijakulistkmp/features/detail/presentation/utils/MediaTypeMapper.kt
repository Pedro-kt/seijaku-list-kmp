package com.yumedev.seijakulistkmp.features.detail.presentation.utils

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType as DetailMediaType
import com.yumedev.seijakulistkmp.core.domain.model.MediaType as CoreMediaType

fun DetailMediaType.toCore(): CoreMediaType {
    return when (this) {
        DetailMediaType.ANIME -> CoreMediaType.ANIME
        DetailMediaType.MANGA -> CoreMediaType.MANGA
    }
}

fun CoreMediaType.toDetail(): DetailMediaType {
    return when (this) {
        CoreMediaType.ANIME -> DetailMediaType.ANIME
        CoreMediaType.MANGA -> DetailMediaType.MANGA
    }
}
