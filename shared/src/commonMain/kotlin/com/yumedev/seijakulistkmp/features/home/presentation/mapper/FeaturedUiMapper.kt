package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.features.home.data.dto.FeaturedAnimeDto
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem

fun FeaturedAnimeDto.toFeaturedMediaItem(): FeaturedMediaItem {
    val displayTitle = title.english ?: title.romaji ?: title.native ?: "Unknown"

    val formattedRating = averageScore?.let { score ->
        val rating = score / 10.0
        String.format("%.1f", rating)
    }

    val formattedStatus = when (status) {
        "RELEASING" -> "Airing"
        "FINISHED" -> "Finished"
        "NOT_YET_RELEASED" -> "Not Yet Aired"
        "CANCELLED" -> "Cancelled"
        "HIATUS" -> "Hiatus"
        else -> null
    }

    val metadata = buildList {
        seasonYear?.let { add(it.toString()) }
        when (format) {
            "TV" -> add("TV")
            "TV_SHORT" -> add("TV Short")
            "MOVIE" -> add("Movie")
            "SPECIAL" -> add("Special")
            "OVA" -> add("OVA")
            "ONA" -> add("ONA")
            "MUSIC" -> add("Music")
            else -> null
        }?.let { }
        episodes?.let { add("$it ep") }
    }.joinToString(" · ")

    return FeaturedMediaItem(
        id = id,
        title = displayTitle,
        coverImageUrl = bannerImage ?: (coverImage.extraLarge ?: coverImage.large ?: coverImage.medium),
        rating = formattedRating,
        status = formattedStatus,
        metadata = metadata
    )
}
