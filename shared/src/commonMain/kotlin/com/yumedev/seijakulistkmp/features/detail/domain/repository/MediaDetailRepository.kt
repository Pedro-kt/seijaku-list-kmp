package com.yumedev.seijakulistkmp.features.detail.domain.repository

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail

interface MediaDetailRepository {
    suspend fun getAnimeDetail(id: Int): Result<MediaDetail>
    suspend fun getMangaDetail(id: Int): Result<MediaDetail>
}
