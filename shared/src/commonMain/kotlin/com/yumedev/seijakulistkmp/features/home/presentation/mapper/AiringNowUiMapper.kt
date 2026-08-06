package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.model.AiringNowAnime
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem

suspend fun AiringNowAnime.toAnimeCardItem(formatter: MediaStringFormatter): AnimeCardItem {
    return AnimeCardItem(
        id = id,
        title = title,
        coverImageUrl = coverImageUrl,
        rating = formatter.formatRating(averageScore),
        format = formatter.formatMediaFormat(format),
        episodes = formatter.formatEpisodes(episodes),
        genres = genres.take(2)
    )
}
