package com.yumedev.seijakulistkmp.features.tracking.presentation.components

sealed class MediaListCardType {
    data object Compact : MediaListCardType()
    // Future card types can be added here:
    // data object Grid : MediaListCardType()
    // data object Detailed : MediaListCardType()
}
