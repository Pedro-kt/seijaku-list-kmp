package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.features.home.domain.model.TopRatedAnime
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem

suspend fun TopRatedAnime.toAnimeCardItem(formatter: MediaStringFormatter): AnimeCardItem {
    val formattedRating = averageScore?.let { score ->
        val rating = score / 10.0
        String.format("%.1f", rating)
    }

    val formattedFormat = formatter.formatMediaFormat(format)
    val formattedEpisodes = formatter.formatEpisodes(episodes)

    return AnimeCardItem(
        id = id,
        title = title,
        coverImageUrl = coverImageUrl,
        rating = formattedRating,
        format = formattedFormat,
        episodes = formattedEpisodes,
        genres = genres
    )
}
