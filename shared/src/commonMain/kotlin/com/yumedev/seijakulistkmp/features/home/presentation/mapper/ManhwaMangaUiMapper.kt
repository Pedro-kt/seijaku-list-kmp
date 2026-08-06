package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.model.ManhwaManga
import com.yumedev.seijakulistkmp.features.home.presentation.model.MangaCardItem

suspend fun ManhwaManga.toMangaCardItem(formatter: MediaStringFormatter): MangaCardItem {
    return MangaCardItem(
        id = id,
        title = title,
        coverImageUrl = coverImageUrl,
        rating = formatter.formatRating(averageScore),
        format = formatter.formatMediaFormat(format),
        chapters = formatter.formatChapters(chapters),
        volumes = formatter.formatVolumes(volumes),
        genres = genres.take(2)
    )
}
