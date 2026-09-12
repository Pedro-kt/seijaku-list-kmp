package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.data.remote.graphql.GetAnimeHomeDataQuery
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem

suspend fun GetAnimeHomeDataQuery.Medium.toFeaturedMediaItem(formatter: MediaStringFormatter): FeaturedMediaItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return FeaturedMediaItem(
        id = id,
        title = displayTitle,
        coverImageUrl = bannerImage ?: (coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium),
        rating = formatter.formatRating(averageScore),
        status = status?.name,
        metadata = formatter.buildAnimeMetadata(seasonYear, format?.name, episodes)
    )
}

suspend fun GetAnimeHomeDataQuery.Medium1.toAnimeCardItem(formatter: MediaStringFormatter): AnimeCardItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return AnimeCardItem(
        id = id,
        title = displayTitle,
        coverImageUrl = coverImage?.large ?: coverImage?.medium,
        rating = formatter.formatRating(averageScore),
        format = format?.name,
        episodes = episodes?.toString(),
        genres = genres?.filterNotNull() ?: emptyList()
    )
}

suspend fun GetAnimeHomeDataQuery.Medium2.toAnimeCardItem(formatter: MediaStringFormatter): AnimeCardItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return AnimeCardItem(
        id = id,
        title = displayTitle,
        coverImageUrl = coverImage?.large ?: coverImage?.medium,
        rating = formatter.formatRating(averageScore),
        format = format?.name,
        episodes = episodes?.toString(),
        genres = genres?.filterNotNull() ?: emptyList()
    )
}

suspend fun GetAnimeHomeDataQuery.Medium3.toAnimeCardItem(formatter: MediaStringFormatter): AnimeCardItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return AnimeCardItem(
        id = id,
        title = displayTitle,
        coverImageUrl = coverImage?.large ?: coverImage?.medium,
        rating = formatter.formatRating(averageScore),
        format = format?.name,
        episodes = episodes?.toString(),
        genres = genres?.filterNotNull() ?: emptyList()
    )
}
