package com.yumedev.seijakulistkmp.features.tracking.presentation.components

sealed class MediaListCardType {
    data object Compact : MediaListCardType()
    data object Grid : MediaListCardType()
    // Future card types can be added here:
    // data object Detailed : MediaListCardType()
}
