package com.yumedev.seijakulistkmp.features.home.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetFeaturedAnimeQuery
import com.yumedev.seijakulistkmp.features.home.data.dto.CoverImageDto
import com.yumedev.seijakulistkmp.features.home.data.dto.FeaturedAnimeDto
import com.yumedev.seijakulistkmp.features.home.data.dto.TitleDto

/**
 * Mapper específico para Featured Carousel
 * Convierte respuestas de GraphQL a DTOs del dominio de Home
 */

fun GetFeaturedAnimeQuery.Medium.toDto(): FeaturedAnimeDto {
    return FeaturedAnimeDto(
        id = id,
        title = TitleDto(
            romaji = title?.romaji,
            english = title?.english,
            native = title?.native
        ),
        coverImage = CoverImageDto(
            extraLarge = coverImage?.extraLarge,
            large = coverImage?.large,
            medium = coverImage?.medium,
            color = coverImage?.color
        ),
        bannerImage = bannerImage,
        averageScore = averageScore,
        status = status?.name,
        format = format?.name,
        episodes = episodes,
        season = season?.name,
        seasonYear = seasonYear,
        genres = genres?.filterNotNull() ?: emptyList()
    )
}

fun GetFeaturedAnimeQuery.Data.toFeaturedAnimeList(): List<FeaturedAnimeDto> {
    return Page?.media
        ?.filterNotNull()
        ?.filter { !(it.isAdult ?: false) }
        ?.map { it.toDto() }
        ?: emptyList()
}
