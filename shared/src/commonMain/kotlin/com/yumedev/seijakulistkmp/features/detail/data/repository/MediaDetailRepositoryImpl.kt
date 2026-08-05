package com.yumedev.seijakulistkmp.features.detail.data.repository

import com.apollographql.apollo.ApolloClient
import com.yumedev.seijakulistkmp.data.remote.graphql.GetAnimeDetailQuery
import com.yumedev.seijakulistkmp.data.remote.graphql.GetMangaDetailQuery
import com.yumedev.seijakulistkmp.features.detail.data.mapper.toMediaDetail
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.detail.domain.repository.MediaDetailRepository

class MediaDetailRepositoryImpl(
    private val apolloClient: ApolloClient
) : MediaDetailRepository {

    override suspend fun getAnimeDetail(id: Int): Result<MediaDetail> {
        return try {
            val response = apolloClient
                .query(GetAnimeDetailQuery(id = id))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val media = response.data?.Media?.toMediaDetail()
                if (media != null) {
                    Result.success(media)
                } else {
                    Result.failure(Exception("Media not found"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMangaDetail(id: Int): Result<MediaDetail> {
        return try {
            val response = apolloClient
                .query(GetMangaDetailQuery(id = id))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val media = response.data?.Media?.toMediaDetail()
                if (media != null) {
                    Result.success(media)
                } else {
                    Result.failure(Exception("Media not found"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
