package com.yumedev.seijakulistkmp.features.home.presentation.mapper

import com.yumedev.seijakulistkmp.features.home.domain.model.FeaturedManga
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem

fun FeaturedManga.toFeaturedMediaItem(): FeaturedMediaItem {
    val formattedRating = averageScore?.let { score ->
        val rating = score / 10.0
        String.format("%.1f", rating)
    }

    val formattedStatus = when (status) {
        "RELEASING" -> "Publishing"
        "FINISHED" -> "Finished"
        "NOT_YET_RELEASED" -> "Not Yet Released"
        "CANCELLED" -> "Cancelled"
        "HIATUS" -> "Hiatus"
        else -> null
    }

    val metadata = buildList {
        when (format) {
            "MANGA" -> add("Manga")
            "LIGHT_NOVEL" -> add("Light Novel")
            "ONE_SHOT" -> add("One Shot")
            else -> null
        }?.let { }
        volumes?.let { add("$it vol") }
        chapters?.let { add("$it ch") }
    }.joinToString(" · ")

    return FeaturedMediaItem(
        id = id,
        title = title,
        coverImageUrl = bannerImageUrl ?: coverImageUrl,
        rating = formattedRating,
        status = formattedStatus,
        metadata = metadata
    )
}
