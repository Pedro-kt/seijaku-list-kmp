package com.yumedev.seijakulistkmp.features.home.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetManhwaMangaQuery
import com.yumedev.seijakulistkmp.features.home.domain.model.ManhwaManga

fun GetManhwaMangaQuery.Medium.toManhwaManga(): ManhwaManga {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val coverUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium

    return ManhwaManga(
        id = id,
        title = displayTitle,
        coverImageUrl = coverUrl,
        averageScore = averageScore,
        format = format?.rawValue,
        chapters = chapters,
        volumes = volumes,
        genres = genres?.filterNotNull() ?: emptyList(),
        countryOfOrigin = countryOfOrigin?.toString()
    )
}
