package com.yumedev.seijakulistkmp.features.detail.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetAnimeDetailQuery
import com.yumedev.seijakulistkmp.features.detail.domain.model.Character
import com.yumedev.seijakulistkmp.features.detail.domain.model.Episode
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType

fun GetAnimeDetailQuery.Media.toMediaDetail(): MediaDetail {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val nativeTitle = title?.native
    val coverUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium

    val mainStudio = studios?.edges
        ?.filter { it?.isMain == true }
        ?.mapNotNull { it?.node?.name }
        ?.firstOrNull()

    val startDateStr = startDate?.let { date ->
        buildString {
            date.month?.let { month ->
                append(getMonthName(month))
                append(" ")
            }
            date.year?.let { append(it) }
        }.takeIf { it.isNotBlank() }
    }

    val endDateStr = endDate?.let { date ->
        buildString {
            date.month?.let { month ->
                append(getMonthName(month))
                append(" ")
            }
            date.year?.let { append(it) }
        }.takeIf { it.isNotBlank() }
    }

    val mainCharacters = characters?.edges
        ?.filter { it?.role?.rawValue == "MAIN" }
        ?.take(10)
        ?.mapNotNull { edge ->
            edge?.node?.let { node ->
                Character(
                    id = node.id,
                    name = node.name?.full ?: "Unknown",
                    imageUrl = node.image?.large ?: node.image?.medium,
                    role = when (edge.role?.rawValue) {
                        "MAIN" -> "Principal"
                        "SUPPORTING" -> "Secundario"
                        else -> edge.role?.rawValue ?: "Unknown"
                    }
                )
            }
        } ?: emptyList()

    val episodesList = streamingEpisodes?.mapIndexed { index, ep ->
        Episode(
            number = index + 1,
            title = ep?.title ?: "Episode ${index + 1}",
            airDate = null,
            duration = duration,
            rating = null
        )
    } ?: emptyList()

    val rankingPos = rankings
        ?.filter { it?.allTime == true && it.type?.rawValue == "RATED" }
        ?.minByOrNull { it?.rank ?: Int.MAX_VALUE }
        ?.rank

    val popularityPos = rankings
        ?.filter { it?.allTime == true && it.type?.rawValue == "POPULAR" }
        ?.minByOrNull { it?.rank ?: Int.MAX_VALUE }
        ?.rank

    val trailerData = trailer?.let { t ->
        com.yumedev.seijakulistkmp.features.detail.domain.model.Trailer(
            id = t.id ?: "",
            site = t.site ?: "",
            thumbnail = t.thumbnail
        )
    }

    return MediaDetail(
        id = id,
        title = displayTitle,
        titleNative = nativeTitle,
        coverImageUrl = coverUrl,
        bannerImageUrl = bannerImage,
        type = MediaType.ANIME,
        format = format?.rawValue,
        demographic = null,
        year = seasonYear ?: startDate?.year,
        status = status?.rawValue,
        episodes = episodes,
        chapters = null,
        volumes = null,
        startDate = startDateStr,
        endDate = endDateStr,
        serialization = null,
        averageScore = averageScore?.toDouble(),
        rankingPosition = rankingPos,
        popularityPosition = popularityPos,
        totalVotes = favourites,
        description = description?.replace("<br>", "\n")?.replace(Regex("<[^>]*>"), ""),
        author = null,
        artist = null,
        studio = mainStudio,
        license = null,
        genres = genres?.filterNotNull() ?: emptyList(),
        mainCharacters = mainCharacters,
        episodes_list = episodesList.takeIf { it.isNotEmpty() },
        chapters_list = null,
        images = emptyList(),
        trailer = trailerData,
        isFavorite = false,
        isInList = false
    )
}

private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "ene"
        2 -> "feb"
        3 -> "mar"
        4 -> "abr"
        5 -> "may"
        6 -> "jun"
        7 -> "jul"
        8 -> "ago"
        9 -> "sep"
        10 -> "oct"
        11 -> "nov"
        12 -> "dic"
        else -> ""
    }
}
