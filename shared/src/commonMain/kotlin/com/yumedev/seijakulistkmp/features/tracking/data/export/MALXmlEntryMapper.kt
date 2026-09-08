package com.yumedev.seijakulistkmp.features.tracking.data.export

import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.data.local.entity.MediaListEntryEntity
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus

fun AnimeEntry.toEntity(): MediaListEntryEntity {
    val status = determineStatus(myStatus, myRewatching)

    return MediaListEntryEntity(
        id = 0,
        mediaId = seriesAnimedbId,
        mediaType = MediaType.ANIME.name,
        status = status.name,
        progress = myWatchedEpisodes,
        progressVolumes = null,
        repeatCount = 0,
        score = parseScore(myScore),
        startDate = parseDateOrNull(myStartDate),
        finishDate = parseDateOrNull(myFinishDate),
        notes = parseNotesOrNull(myTags),
        priority = 0,
        createdAt = myLastUpdated * 1000,
        updatedAt = myLastUpdated * 1000,
        anilistEntryId = null,
        isSynced = false,
        needsSync = false,
        mediaTitle = seriesTitle.trim().takeIf { it.isNotBlank() },
        mediaCoverImage = parseUrlOrNull(seriesImage),
        mediaTotalEpisodes = if (seriesEpisodes == 0) null else seriesEpisodes,
        mediaTotalChapters = null,
        mediaTotalVolumes = null,
        mediaStatus = null
    )
}

fun MangaEntry.toEntity(): MediaListEntryEntity {
    val status = determineStatus(myStatus, myRereading)

    return MediaListEntryEntity(
        id = 0,
        mediaId = seriesMangadbId,
        mediaType = MediaType.MANGA.name,
        status = status.name,
        progress = myReadChapters,
        progressVolumes = if (myReadVolumes == 0) null else myReadVolumes,
        repeatCount = 0,
        score = parseScore(myScore),
        startDate = parseDateOrNull(myStartDate),
        finishDate = parseDateOrNull(myFinishDate),
        notes = parseNotesOrNull(myTags),
        priority = 0,
        createdAt = myLastUpdated * 1000,
        updatedAt = myLastUpdated * 1000,
        anilistEntryId = null,
        isSynced = false,
        needsSync = false,
        mediaTitle = seriesTitle.trim().takeIf { it.isNotBlank() },
        mediaCoverImage = parseUrlOrNull(seriesImage),
        mediaTotalEpisodes = null,
        mediaTotalChapters = if (seriesChapters == 0) null else seriesChapters,
        mediaTotalVolumes = if (seriesVolumes == 0) null else seriesVolumes,
        mediaStatus = null
    )
}

private fun determineStatus(malStatus: String, isRepeating: Int): MediaListStatus {
    return if (isRepeating == 1) {
        MediaListStatus.REPEATING
    } else {
        MediaListStatus.fromMALStatus(malStatus)
    }
}

private fun parseScore(xmlScore: Int): Float? {
    return if (xmlScore == 0) null else xmlScore.toFloat()
}

private fun parseDateOrNull(dateString: String): String? {
    return when {
        dateString.isBlank() -> null
        dateString == "0000-00-00" -> null
        else -> dateString
    }
}

private fun parseNotesOrNull(notes: String): String? {
    return notes.trim().takeIf { it.isNotBlank() }
}

private fun parseUrlOrNull(url: String): String? {
    return url.trim().takeIf { it.isNotBlank() && it.startsWith("http") }
}
