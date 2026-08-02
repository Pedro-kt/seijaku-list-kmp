package com.yumedev.seijakulistkmp.features.search.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

enum class MediaStatus {
    ALL,
    AIRING,
    FINISHED,
    NOT_YET_AIRED;

    companion object {
        fun all(): List<MediaStatus> = entries
    }
}

@Composable
fun MediaStatus.toLabel(isAnime: Boolean = true): String = when (this) {
    MediaStatus.ALL -> stringResource(Res.string.filter_status_all)
    MediaStatus.AIRING -> if (isAnime) {
        stringResource(Res.string.filter_status_airing)
    } else {
        stringResource(Res.string.filter_status_publishing)
    }
    MediaStatus.FINISHED -> stringResource(Res.string.filter_status_finished)
    MediaStatus.NOT_YET_AIRED -> stringResource(Res.string.filter_status_upcoming)
}

fun MediaStatus.toApiValue(): String? = when (this) {
    MediaStatus.ALL -> null
    MediaStatus.AIRING -> "RELEASING"
    MediaStatus.FINISHED -> "FINISHED"
    MediaStatus.NOT_YET_AIRED -> "NOT_YET_RELEASED"
}
