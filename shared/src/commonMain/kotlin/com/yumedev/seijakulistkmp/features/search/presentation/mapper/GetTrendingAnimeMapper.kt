package com.yumedev.seijakulistkmp.features.search.presentation.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetTrendingAnimeQuery
import com.yumedev.seijakulistkmp.features.search.presentation.model.TrendingAnime

fun GetTrendingAnimeQuery.Medium.toTrendingAnime(): TrendingAnime {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"

    return TrendingAnime(
        id = id,
        title = displayTitle
    )
}

fun List<GetTrendingAnimeQuery.Medium?>.toTrendingAnimes(): List<TrendingAnime> {
    return this.filterNotNull().map { it.toTrendingAnime() }
}
