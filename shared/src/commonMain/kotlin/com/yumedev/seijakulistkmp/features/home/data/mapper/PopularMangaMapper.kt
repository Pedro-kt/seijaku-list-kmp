package com.yumedev.seijakulistkmp.features.home.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetPopularMangaQuery
import com.yumedev.seijakulistkmp.features.home.domain.model.PopularManga

fun GetPopularMangaQuery.Medium.toPopularManga(): PopularManga {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val coverUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium

    return PopularManga(
        id = id,
        title = displayTitle,
        coverImageUrl = coverUrl,
        averageScore = averageScore,
        format = format?.rawValue,
        chapters = chapters,
        volumes = volumes,
        genres = genres?.filterNotNull() ?: emptyList()
    )
}
