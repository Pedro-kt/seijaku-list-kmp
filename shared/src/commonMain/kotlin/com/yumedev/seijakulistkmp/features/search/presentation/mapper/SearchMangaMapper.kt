package com.yumedev.seijakulistkmp.features.search.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.data.remote.graphql.SearchMangaQuery
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaType
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchResultItem

suspend fun SearchMangaQuery.Medium.toSearchResultItem(
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
        year = startDate?.year,
        type = formattedType,
        status = status?.rawValue,
        episodes = null,
        chapters = chapters,
        volumes = volumes,
        genres = genres?.filterNotNull() ?: emptyList(),
        mediaType = MediaType.MANGA
    )
}

suspend fun List<SearchMangaQuery.Medium?>.toSearchResultItems(
    formatter: MediaStringFormatter
): List<SearchResultItem> {
    return this.filterNotNull().map { it.toSearchResultItem(formatter) }
}
