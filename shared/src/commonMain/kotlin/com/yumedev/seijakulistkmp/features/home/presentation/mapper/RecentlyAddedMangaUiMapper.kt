package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.model.RecentlyAddedManga
import com.yumedev.seijakulistkmp.features.home.presentation.model.MangaCardItem

suspend fun RecentlyAddedManga.toMangaCardItem(formatter: MediaStringFormatter): MangaCardItem {
    val formattedRating = averageScore?.let { score ->
        val rating = score / 10.0
        String.format("%.1f", rating)
    }

    val formattedFormat = formatter.formatMediaFormat(format)
    val formattedChapters = formatter.formatChapters(chapters)
    val formattedVolumes = formatter.formatVolumes(volumes)

    return MangaCardItem(
        id = id,
        title = title,
        coverImageUrl = coverImageUrl,
        rating = formattedRating,
        format = formattedFormat,
        chapters = formattedChapters,
        volumes = formattedVolumes,
        genres = genres
    )
}
