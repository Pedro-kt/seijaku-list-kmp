package com.yumedev.seijakulistkmp.features.detail.presentation.mapper

import androidx.compose.ui.graphics.Color
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.model.*
import com.yumedev.seijakulistkmp.features.detail.presentation.utils.DetailFormatters

fun MediaDetail.toUiModel(
    strings: DetailStrings,
    primaryColor: Color
): MediaDetailUiModel {

    val statusText = status?.let {
        getStatusText(
            it,
            strings.statusFinished,
            strings.statusAiring,
            strings.statusNotYetAired,
            strings.statusCancelled
        )
    }

    val headerMetadata = DetailFormatters.buildMetadataString(
        year = year,
        format = format,
        chapters = chapters,
        volumes = volumes,
        episodes = episodes,
        statusText = statusText
    )

    val formattedScore = averageScore?.let { DetailFormatters.formatScore(it) }
    val formattedVotes = DetailFormatters.formatVotes(totalVotes, strings.votes)

    val nextAiringText = nextAiringEpisode?.let {
        DetailFormatters.formatNextAiringText(
            episode = it.episode,
            timeUntilAiring = it.timeUntilAiring,
            timeMinutesLabel = strings.timeMinutes,
            timeHoursLabel = strings.timeHours,
            timeDaysLabel = strings.timeDays
        )
    }

    val charactersUi = mainCharacters.map { character ->
        CharacterUiModel(
            id = character.id,
            name = character.name,
            imageUrl = character.imageUrl,
            roleText = when (character.role) {
                "MAIN" -> strings.characterRoleMain
                "SUPPORTING" -> strings.characterRoleSupporting
                else -> character.role
            }
        )
    }

    val externalLinksUi = externalLinks.map { link ->
        val bgColor = link.color?.let { colorString ->
            DetailFormatters.parseHexColor(colorString, primaryColor)
        } ?: primaryColor

        ExternalLinkUiModel(
            id = link.id,
            url = link.url,
            siteName = link.site,
            backgroundColor = bgColor,
            textColor = DetailFormatters.getContrastingTextColor(bgColor),
            iconUrl = link.icon
        )
    }

    val episodesUi = episodes_list?.map { episode ->
        EpisodeUiModel(
            number = episode.number,
            title = episode.title,
            thumbnail = episode.thumbnail,
            url = episode.url,
            site = episode.site,
            airDate = episode.airDate,
            duration = episode.duration,
            rating = episode.rating
        )
    }

    val chaptersUi = chapters_list?.map { chapter ->
        ChapterUiModel(
            number = chapter.number,
            title = chapter.title,
            volume = chapter.volume,
            releaseDate = chapter.releaseDate,
            rating = chapter.rating
        )
    }

    val trailerUi = trailer?.let {
        TrailerUiModel(
            id = it.id,
            site = it.site,
            thumbnail = it.thumbnail
        )
    }

    val informationItems = buildInformationItems(
        type = type,
        format = format,
        volumes = volumes,
        status = statusText,
        chapters = chapters,
        episodes = episodes,
        startDate = startDate,
        endDate = endDate,
        serialization = serialization,
        author = author,
        artist = artist,
        studio = studio,
        demographic = demographic,
        license = license,
        strings = strings
    )

    return MediaDetailUiModel(
        id = id,
        title = title,
        titleNative = titleNative,
        coverImageUrl = coverImageUrl,
        bannerImageUrl = bannerImageUrl,
        type = type,
        headerMetadata = headerMetadata,
        typeLabel = format,
        demographic = demographic,
        year = year,
        formattedScore = formattedScore,
        formattedVotes = formattedVotes,
        rankingText = rankingPosition?.let { "#$it" },
        popularityText = popularityPosition?.let { "#$it" },
        nextAiringText = nextAiringText,
        nextAiringEpisode = nextAiringEpisode?.episode,
        description = description,
        informationItems = informationItems,
        genres = genres,
        characters = charactersUi,
        externalLinks = externalLinksUi,
        episodesList = episodesUi,
        chaptersList = chaptersUi,
        totalEpisodes = episodes,
        totalChapters = chapters,
        images = images,
        trailer = trailerUi,
        isFavorite = isFavorite,
        isInList = isInList
    )
}

private fun getStatusText(
    status: String,
    finishedLabel: String,
    airingLabel: String,
    notYetAiredLabel: String,
    cancelledLabel: String
): String {
    return when (status.uppercase()) {
        "FINISHED" -> finishedLabel
        "RELEASING" -> airingLabel
        "NOT_YET_RELEASED" -> notYetAiredLabel
        "CANCELLED" -> cancelledLabel
        "HIATUS" -> cancelledLabel
        else -> status
    }
}

private fun buildInformationItems(
    type: MediaType,
    format: String?,
    volumes: Int?,
    status: String?,
    chapters: Int?,
    episodes: Int?,
    startDate: String?,
    endDate: String?,
    serialization: String?,
    author: String?,
    artist: String?,
    studio: String?,
    demographic: String?,
    license: String?,
    strings: DetailStrings
): List<InformationItemUiModel> {
    return buildList {
        format?.let {
            add(InformationItemUiModel(strings.infoType, it))
        }

        if (type == MediaType.MANGA) {
            volumes?.let {
                add(InformationItemUiModel(strings.infoVolumes, it.toString()))
            }
        }

        status?.let {
            add(InformationItemUiModel(strings.infoStatus, it))
        }

        when (type) {
            MediaType.MANGA -> chapters?.let {
                add(InformationItemUiModel(strings.infoChapters, it.toString()))
            }
            MediaType.ANIME -> episodes?.let {
                add(InformationItemUiModel(strings.infoEpisodes, it.toString()))
            }
        }

        val publicationText = buildString {
            startDate?.let { append(it) }
            if (startDate != null && endDate != null) append(" - ")
            endDate?.let { append(it) }
        }.takeIf { it.isNotBlank() }

        publicationText?.let {
            add(InformationItemUiModel(strings.infoPublication, it))
        }

        serialization?.let {
            add(InformationItemUiModel(strings.infoSerialization, it))
        }

        author?.let {
            add(InformationItemUiModel(strings.infoAuthor, it))
        }

        artist?.let {
            add(InformationItemUiModel(strings.infoArt, it))
        }

        studio?.let {
            add(InformationItemUiModel(strings.infoStudio, it))
        }

        demographic?.let {
            add(InformationItemUiModel(strings.infoDemographic, it))
        }

        license?.let {
            add(InformationItemUiModel(strings.infoLicense, it))
        }
    }
}
