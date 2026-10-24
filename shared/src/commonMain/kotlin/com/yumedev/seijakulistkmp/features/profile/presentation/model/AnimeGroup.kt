package com.yumedev.seijakulistkmp.features.profile.presentation.model

import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode

data class AnimeGroup(
    val mediaId: Int,
    val animeTitle: String,
    val episodes: List<FavoriteEpisode>
) {
    val episodeCount: Int get() = episodes.size
}
