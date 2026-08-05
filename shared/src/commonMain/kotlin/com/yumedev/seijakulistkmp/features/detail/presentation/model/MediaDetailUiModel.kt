package com.yumedev.seijakulistkmp.features.detail.presentation.model

import androidx.compose.ui.graphics.Color
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType

data class MediaDetailUiModel(
    val id: Int,
    val title: String,
    val titleNative: String?,
    val coverImageUrl: String?,
    val bannerImageUrl: String?,
    val type: MediaType,
    val status: String?,
    val headerMetadata: String,
    val typeLabel: String?,
    val demographic: String?,
    val year: Int?,
    val formattedScore: String?,
    val formattedVotes: String?,
    val rankingText: String?,
    val popularityText: String?,
    val nextAiringText: String?,
    val nextAiringEpisode: Int?,

    val description: String?,

    val informationItems: List<InformationItemUiModel>,

    val genres: List<String>,

    val characters: List<CharacterUiModel>,

    val externalLinks: List<ExternalLinkUiModel>,

    val episodesList: List<EpisodeUiModel>?,
    val chaptersList: List<ChapterUiModel>?,
    val totalEpisodes: Int?,
    val totalChapters: Int?,

    val images: List<String>,

    val trailer: TrailerUiModel?,

    val isFavorite: Boolean = false,
    val isInList: Boolean = false
)

data class InformationItemUiModel(
    val label: String,
    val value: String
)

data class CharacterUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val roleText: String
)

data class ExternalLinkUiModel(
    val id: Int,
    val url: String?,
    val siteName: String,
    val backgroundColor: Color,
    val textColor: Color,
    val iconUrl: String?
)

data class EpisodeUiModel(
    val number: Int,
    val title: String,
    val thumbnail: String?,
    val url: String?,
    val site: String?,
    val airDate: String?,
    val duration: Int?,
    val rating: Double?
)

data class ChapterUiModel(
    val number: Int,
    val title: String,
    val volume: Int?,
    val releaseDate: String?,
    val rating: Double?
)

data class TrailerUiModel(
    val id: String,
    val site: String,
    val thumbnail: String?
)
