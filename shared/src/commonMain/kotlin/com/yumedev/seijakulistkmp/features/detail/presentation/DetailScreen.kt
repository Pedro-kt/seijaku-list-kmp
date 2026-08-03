package com.yumedev.seijakulistkmp.features.detail.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.detail.domain.model.Character
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.detail.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    mediaDetail: MediaDetail,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onMoreClick: () -> Unit,
    onAddToListClick: () -> Unit,
    onCharacterClick: (Character) -> Unit,
    onSeeAllChaptersClick: () -> Unit,
    onChapterClick: (Int) -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DetailTopBar(
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                onShareClick = onShareClick,
                onMoreClick = onMoreClick,
                isFavorite = mediaDetail.isFavorite
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DetailHeader(
                coverImageUrl = mediaDetail.coverImageUrl,
                type = mediaDetail.format,
                demographic = mediaDetail.demographic,
                title = mediaDetail.title,
                titleNative = mediaDetail.titleNative,
                year = mediaDetail.year,
                format = mediaDetail.format,
                chapters = mediaDetail.chapters,
                volumes = mediaDetail.volumes,
                episodes = mediaDetail.episodes,
                status = mediaDetail.status,
                averageScore = mediaDetail.averageScore,
                totalVotes = mediaDetail.totalVotes,
                rankingPosition = mediaDetail.rankingPosition,
                popularityPosition = mediaDetail.popularityPosition,
                onAddToListClick = onAddToListClick
            )

            DetailSynopsis(
                synopsis = mediaDetail.description
            )

            DetailInformation(
                type = mediaDetail.type,
                format = mediaDetail.format,
                volumes = mediaDetail.volumes,
                status = mediaDetail.status,
                chapters = mediaDetail.chapters,
                episodes = mediaDetail.episodes,
                startDate = mediaDetail.startDate,
                endDate = mediaDetail.endDate,
                serialization = mediaDetail.serialization,
                author = mediaDetail.author,
                artist = mediaDetail.artist,
                studio = mediaDetail.studio,
                demographic = mediaDetail.demographic,
                license = mediaDetail.license
            )

            DetailGenres(
                genres = mediaDetail.genres
            )

            DetailCharacters(
                characters = mediaDetail.mainCharacters,
                onCharacterClick = onCharacterClick
            )

            DetailChaptersList(
                type = mediaDetail.type,
                chapters = mediaDetail.chapters_list,
                episodes = mediaDetail.episodes_list,
                totalCount = mediaDetail.chapters ?: mediaDetail.episodes,
                onSeeAllClick = onSeeAllChaptersClick,
                onItemClick = onChapterClick
            )

            DetailImagesGrid(
                images = mediaDetail.images,
                onImageClick = onImageClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
