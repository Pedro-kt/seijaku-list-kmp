package com.yumedev.seijakulistkmp.features.home.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetFeaturedMangaQuery
import com.yumedev.seijakulistkmp.features.home.domain.model.FeaturedManga

fun GetFeaturedMangaQuery.Medium.toFeaturedManga(): FeaturedManga {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val coverUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium

    return FeaturedManga(
        id = id,
        title = displayTitle,
        coverImageUrl = coverUrl,
        bannerImageUrl = bannerImage,
        averageScore = averageScore,
        status = status?.rawValue,
        format = format?.rawValue,
        chapters = chapters,
        volumes = volumes,
        genres = genres?.filterNotNull() ?: emptyList()
    )
}
