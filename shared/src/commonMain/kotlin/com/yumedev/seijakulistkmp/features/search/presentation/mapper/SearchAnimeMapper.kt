package com.yumedev.seijakulistkmp.features.search.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.data.remote.graphql.SearchAnimeQuery
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaType
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchResultItem

suspend fun SearchAnimeQuery.Medium.toSearchResultItem(
    formatter: MediaStringFormatter
): SearchResultItem {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val coverUrl = coverImage?.large ?: coverImage?.medium ?: ""

    val formattedType = formatter.formatMediaFormat(format?.rawValue) ?: format?.rawValue ?: "Unknown"

    return SearchResultItem(
        id = id,
        title = displayTitle,
        alternativeTitle = title?.native,
        coverImage = coverUrl,
        rating = averageScore?.div(10.0),
        year = seasonYear,
        type = formattedType,
        status = status?.rawValue,
        episodes = episodes,
        chapters = null,
        volumes = null,
        genres = genres?.filterNotNull() ?: emptyList(),
        mediaType = MediaType.ANIME
    )
}

suspend fun List<SearchAnimeQuery.Medium?>.toSearchResultItems(
    formatter: MediaStringFormatter
): List<SearchResultItem> {
    return this.filterNotNull().map { it.toSearchResultItem(formatter) }
}
