package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.home.data.datasource.SectionDataSource
import com.yumedev.seijakulistkmp.features.home.domain.model.SectionType
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem

class GetSectionMediaUseCase(
    private val sectionDataSource: SectionDataSource
) {
    suspend operator fun invoke(
        sectionType: SectionType,
        mediaType: MediaType,
        page: Int,
        perPage: Int = 30
    ): Result<SectionMediaResult> {
        return try {
            sectionDataSource.getSectionMedia(
                sectionType = sectionType,
                mediaType = mediaType,
                page = page,
                perPage = perPage
            ).mapCatching { data ->
                val items = data.Page?.media?.mapNotNull { media ->
                    media?.let {
                        AnimeCardItem(
                            id = it.id,
                            title = it.title?.romaji ?: it.title?.english ?: it.title?.native ?: "",
                            coverImageUrl = it.coverImage?.large,
                            rating = it.averageScore?.let { score -> "$score%" },
                            format = it.format?.name,
                            episodes = when (mediaType) {
                                MediaType.ANIME -> it.episodes?.toString()?.let { eps -> "$eps eps" }
                                MediaType.MANGA -> it.chapters?.toString()?.let { ch -> "$ch ch" }
                            },
                            genres = it.genres?.filterNotNull() ?: emptyList()
                        )
                    }
                } ?: emptyList()

                val pageInfo = data.Page?.pageInfo
                SectionMediaResult(
                    items = items,
                    currentPage = pageInfo?.currentPage ?: page,
                    hasNextPage = pageInfo?.hasNextPage ?: false
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class SectionMediaResult(
    val items: List<AnimeCardItem>,
    val currentPage: Int,
    val hasNextPage: Boolean
)
