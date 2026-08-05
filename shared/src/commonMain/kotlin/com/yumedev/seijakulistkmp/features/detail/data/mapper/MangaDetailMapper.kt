package com.yumedev.seijakulistkmp.features.detail.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetMangaDetailQuery
import com.yumedev.seijakulistkmp.features.detail.domain.model.*

fun GetMangaDetailQuery.Media.toMediaDetail(): MediaDetail {
    val displayTitle = title?.english ?: title?.romaji ?: title?.native ?: "Unknown"
    val nativeTitle = title?.native
    val coverUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium

    val author = staff?.edges
        ?.filter { it?.role?.contains("Story", ignoreCase = true) == true ||
                   it?.role?.contains("Original Creator", ignoreCase = true) == true }
        ?.mapNotNull { it?.node?.name?.full }
        ?.firstOrNull()

    val artist = staff?.edges
        ?.filter { it?.role?.contains("Art", ignoreCase = true) == true }
        ?.mapNotNull { it?.node?.name?.full }
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
                    role = edge.role?.rawValue ?: "UNKNOWN"
                )
            }
        } ?: emptyList()

    val rankingPos = rankings
        ?.filter { it?.allTime == true && it.type?.rawValue == "RATED" }
        ?.minByOrNull { it?.rank ?: Int.MAX_VALUE }
        ?.rank

    val popularityPos = rankings
        ?.filter { it?.allTime == true && it.type?.rawValue == "POPULAR" }
        ?.minByOrNull { it?.rank ?: Int.MAX_VALUE }
        ?.rank

    val demographic = tags
        ?.filter { it?.rank != null && it.rank!! > 60 }
        ?.mapNotNull { it?.name }
        ?.firstOrNull { tag ->
            tag in listOf("Shounen", "Seinen", "Shoujo", "Josei", "Kids")
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

    return MediaDetail(
        id = id,
        title = displayTitle,
        titleNative = nativeTitle,
        coverImageUrl = coverUrl,
        bannerImageUrl = bannerImage,
        type = MediaType.MANGA,
        format = format?.rawValue,
        demographic = demographic,
        year = startDate?.year,
        status = status?.rawValue,
        episodes = null,
        chapters = chapters,
        volumes = volumes,
        startDate = startDateStr,
        endDate = endDateStr,
        serialization = null,
        averageScore = averageScore?.toDouble(),
        rankingPosition = rankingPos,
        popularityPosition = popularityPos,
        totalVotes = favourites,
        description = description?.replace("<br>", "\n")?.replace(Regex("<[^>]*>"), ""),
        author = author,
        artist = artist,
        studio = null,
        license = null,
        genres = genres?.filterNotNull() ?: emptyList(),
        mainCharacters = mainCharacters,
        episodes_list = null,
        chapters_list = null,
        images = emptyList(),
        trailer = null,
        externalLinks = externalLinksList,
        nextAiringEpisode = null,
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
