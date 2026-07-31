package com.yumedev.seijakulistkmp.features.anime.data.mapper

import com.yumedev.seijakulistkmp.core.domain.model.Pagination
import com.yumedev.seijakulistkmp.core.domain.model.PaginatedData
import com.yumedev.seijakulistkmp.features.anime.data.dto.*
import com.yumedev.seijakulistkmp.features.anime.domain.model.*

/**
 * Extension functions to map DTOs to Domain models
 */

fun AnimeDto.toDomain(): Anime = Anime(
    id = id,
    title = title.toDomain(),
    coverImage = coverImage.toDomain(),
    format = AnimeFormat.fromString(format),
    status = AnimeStatus.fromString(status),
    episodes = episodes,
    season = AnimeSeason.fromString(season),
    seasonYear = seasonYear,
    averageScore = averageScore,
    popularity = popularity,
    genres = genres,
    description = description,
    bannerImage = bannerImage,
    isAdult = isAdult
)

fun AnimeTitleDto.toDomain(): AnimeTitle = AnimeTitle(
    romaji = romaji,
    english = english,
    native = native
)

fun AnimeCoverImageDto.toDomain(): AnimeCoverImage = AnimeCoverImage(
    extraLarge = extraLarge,
    large = large,
    medium = medium,
    color = color
)

fun PageInfoDto.toPagination(): Pagination = Pagination(
    currentPage = currentPage,
    hasNextPage = hasNextPage,
    lastPage = lastPage,
    perPage = perPage,
    total = total
)

fun PagedAnimeResponseDto.toDomain(): PaginatedData<Anime> = PaginatedData(
    data = page.media.map { it.toDomain() },
    pagination = page.pageInfo.toPagination()
)

fun SingleAnimeResponseDto.toDomain(): Anime = media.toDomain()
