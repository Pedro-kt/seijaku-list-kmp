package com.yumedev.seijakulistkmp.features.home.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetPublishingMangaQuery
import com.yumedev.seijakulistkmp.features.home.domain.model.PublishingManga

fun GetPublishingMangaQuery.Medium.toPublishingManga(): PublishingManga {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val coverUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium

    return PublishingManga(
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
