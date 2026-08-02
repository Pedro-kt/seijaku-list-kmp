package com.yumedev.seijakulistkmp.features.search.presentation.mapper

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.data.remote.graphql.GetAiringTodayAnimeQuery
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaType
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchResultItem

suspend fun GetAiringTodayAnimeQuery.AiringSchedule.toSearchResultItem(
    formatter: MediaStringFormatter
): SearchResultItem? {
    val mediaData = media ?: return null

    val displayTitle = mediaData.title?.english ?: mediaData.title?.romaji ?: mediaData.title?.native ?: "Unknown"
    val coverUrl = mediaData.coverImage?.large ?: mediaData.coverImage?.medium ?: ""

    val formattedType = formatter.formatMediaFormat(mediaData.format?.rawValue) ?: mediaData.format?.rawValue ?: "Unknown"

    return SearchResultItem(
        id = mediaData.id,
        title = displayTitle,
        alternativeTitle = mediaData.title?.native,
        coverImage = coverUrl,
        rating = mediaData.averageScore?.div(10.0),
        year = null,
        type = formattedType,
        status = mediaData.status?.rawValue,
        episodes = mediaData.episodes,
        chapters = null,
        volumes = null,
        genres = mediaData.genres?.filterNotNull() ?: emptyList(),
        mediaType = MediaType.ANIME
    )
}

suspend fun List<GetAiringTodayAnimeQuery.AiringSchedule?>.toSearchResultItems(
    formatter: MediaStringFormatter
): List<SearchResultItem> {
    return this.filterNotNull()
        .mapNotNull { it.toSearchResultItem(formatter) }
        .distinctBy { it.id } // Remove duplicates by anime ID
}
