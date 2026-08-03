package com.yumedev.seijakulistkmp.features.home.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetRecentlyAddedMangaQuery
import com.yumedev.seijakulistkmp.features.home.domain.model.RecentlyAddedManga

fun GetRecentlyAddedMangaQuery.Medium.toRecentlyAddedManga(): RecentlyAddedManga {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val coverUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium

    return RecentlyAddedManga(
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
