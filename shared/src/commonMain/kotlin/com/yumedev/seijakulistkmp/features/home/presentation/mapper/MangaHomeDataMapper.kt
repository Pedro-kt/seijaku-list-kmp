package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.data.remote.graphql.GetMangaHomeDataQuery
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.MangaCardItem

suspend fun GetMangaHomeDataQuery.Medium.toFeaturedMediaItem(formatter: MediaStringFormatter): FeaturedMediaItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return FeaturedMediaItem(
        id = id,
        title = displayTitle,
        coverImageUrl = bannerImage ?: (coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium),
        rating = formatter.formatRating(averageScore),
        status = status?.name,
        metadata = formatter.buildMangaMetadata(startDate?.year, format?.name, volumes, chapters)
    )
}

suspend fun GetMangaHomeDataQuery.Medium1.toMangaCardItem(formatter: MediaStringFormatter): MangaCardItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return MangaCardItem(
        id = id,
        title = displayTitle,
        coverImageUrl = coverImage?.large ?: coverImage?.medium,
        rating = formatter.formatRating(averageScore),
        format = format?.name,
        chapters = chapters?.toString(),
        volumes = volumes?.toString(),
        genres = genres?.filterNotNull() ?: emptyList()
    )
}

suspend fun GetMangaHomeDataQuery.Medium2.toMangaCardItem(formatter: MediaStringFormatter): MangaCardItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return MangaCardItem(
        id = id,
        title = displayTitle,
        coverImageUrl = coverImage?.large ?: coverImage?.medium,
        rating = formatter.formatRating(averageScore),
        format = format?.name,
        chapters = chapters?.toString(),
        volumes = volumes?.toString(),
        genres = genres?.filterNotNull() ?: emptyList()
    )
}

suspend fun GetMangaHomeDataQuery.Medium3.toMangaCardItem(formatter: MediaStringFormatter): MangaCardItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return MangaCardItem(
        id = id,
        title = displayTitle,
        coverImageUrl = coverImage?.large ?: coverImage?.medium,
        rating = formatter.formatRating(averageScore),
        format = format?.name,
        chapters = chapters?.toString(),
        volumes = volumes?.toString(),
        genres = genres?.filterNotNull() ?: emptyList()
    )
}

suspend fun GetMangaHomeDataQuery.Medium4.toMangaCardItem(formatter: MediaStringFormatter): MangaCardItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return MangaCardItem(
        id = id,
        title = displayTitle,
        coverImageUrl = coverImage?.large ?: coverImage?.medium,
        rating = formatter.formatRating(averageScore),
        format = format?.name,
        chapters = chapters?.toString(),
        volumes = volumes?.toString(),
        genres = genres?.filterNotNull() ?: emptyList()
    )
}

suspend fun GetMangaHomeDataQuery.Medium5.toMangaCardItem(formatter: MediaStringFormatter): MangaCardItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: formatter.getUnknownString()

    return MangaCardItem(
        id = id,
        title = displayTitle,
        coverImageUrl = coverImage?.large ?: coverImage?.medium,
        rating = formatter.formatRating(averageScore),
        format = format?.name,
        chapters = chapters?.toString(),
        volumes = volumes?.toString(),
        genres = genres?.filterNotNull() ?: emptyList()
    )
}
