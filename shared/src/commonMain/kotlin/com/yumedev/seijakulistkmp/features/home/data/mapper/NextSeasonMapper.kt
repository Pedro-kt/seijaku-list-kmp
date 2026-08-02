package com.yumedev.seijakulistkmp.features.home.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetNextSeasonAnimeQuery
import com.yumedev.seijakulistkmp.features.home.domain.model.NextSeasonAnime

fun GetNextSeasonAnimeQuery.Medium.toNextSeasonAnime(): NextSeasonAnime {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val coverUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium

    return NextSeasonAnime(
        id = id,
        title = displayTitle,
        coverImageUrl = coverUrl,
        averageScore = averageScore,
        format = format?.rawValue,
        episodes = episodes,
        genres = genres?.filterNotNull() ?: emptyList()
    )
}
