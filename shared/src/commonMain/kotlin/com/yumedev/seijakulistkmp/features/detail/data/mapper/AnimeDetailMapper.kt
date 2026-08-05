package com.yumedev.seijakulistkmp.features.detail.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetAnimeDetailQuery
import com.yumedev.seijakulistkmp.features.detail.domain.model.*

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
        ?.mapNotNull { edge ->
            edge?.node?.let { node ->
                Character(
                    id = node.id,
                    name = node.name?.full ?: "Unknown",
                    imageUrl = node.image?.large ?: node.image?.medium,
                    role = edge.role?.rawValue ?: "UNKNOWN"
                )
            }
        } ?: emptyList()

    val episodesList = streamingEpisodes?.mapIndexed { index, ep ->
        Episode(
            number = index + 1,
            title = ep?.title ?: "Episode ${index + 1}",
            thumbnail = ep?.thumbnail,
            url = ep?.url,
            site = ep?.site,
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
        Trailer(
            id = t.id ?: "",
            site = t.site ?: "",
            thumbnail = t.thumbnail
        )
    }

    val externalLinksList = externalLinks?.mapNotNull { link ->
        link?.let {
            ExternalLink(
                id = it.id,
                url = it.url,
                site = it.site ?: "",
                siteId = it.siteId,
                type = it.type?.rawValue,
                language = it.language,
                color = it.color,
                icon = it.icon,
                notes = it.notes,
                isDisabled = it.isDisabled
            )
        }
    }?.filter { it.isDisabled != true } ?: emptyList()

    val nextAiring = nextAiringEpisode?.let {
        NextAiringEpisode(
            episode = it.episode,
            airingAt = it.airingAt.toLong(),
            timeUntilAiring = it.timeUntilAiring.toLong()
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
        externalLinks = externalLinksList,
        nextAiringEpisode = nextAiring,
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
