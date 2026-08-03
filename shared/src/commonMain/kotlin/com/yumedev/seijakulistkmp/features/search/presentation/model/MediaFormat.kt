package com.yumedev.seijakulistkmp.features.search.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

enum class MediaFormat {
    ALL,
    TV,
    MOVIE,
    OVA,
    SPECIAL,
    MANGA,
    NOVEL,
    ONE_SHOT;

    companion object {
        fun all(): List<MediaFormat> = entries

        fun forAnime(): List<MediaFormat> = listOf(
            ALL,
            TV,
            MOVIE,
            OVA,
            SPECIAL
        )

        fun forManga(): List<MediaFormat> = listOf(
            ALL,
            MANGA,
            NOVEL,
            ONE_SHOT
        )
    }
}

@Composable
fun MediaFormat.toLabel(): String = when (this) {
    MediaFormat.ALL -> stringResource(Res.string.filter_format_all)
    MediaFormat.TV -> stringResource(Res.string.filter_format_tv)
    MediaFormat.MOVIE -> stringResource(Res.string.filter_format_movie)
    MediaFormat.OVA -> stringResource(Res.string.filter_format_ova)
    MediaFormat.SPECIAL -> stringResource(Res.string.filter_format_special)
    MediaFormat.MANGA -> stringResource(Res.string.filter_format_manga)
    MediaFormat.NOVEL -> stringResource(Res.string.filter_format_novel)
    MediaFormat.ONE_SHOT -> stringResource(Res.string.filter_format_one_shot)
}

fun MediaFormat.toApiValue(): String? = when (this) {
    MediaFormat.ALL -> null
    MediaFormat.TV -> "TV"
    MediaFormat.MOVIE -> "MOVIE"
    MediaFormat.OVA -> "OVA"
    MediaFormat.SPECIAL -> "SPECIAL"
    MediaFormat.MANGA -> "MANGA"
    MediaFormat.NOVEL -> "NOVEL"
    MediaFormat.ONE_SHOT -> "ONE_SHOT"
}
