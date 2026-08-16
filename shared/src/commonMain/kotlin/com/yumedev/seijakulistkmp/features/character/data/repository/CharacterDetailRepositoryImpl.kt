package com.yumedev.seijakulistkmp.features.character.data.repository

import com.apollographql.apollo.ApolloClient
import com.yumedev.seijakulistkmp.data.remote.graphql.GetCharacterDetailQuery
import com.yumedev.seijakulistkmp.features.character.data.mapper.toCharacterDetail
import com.yumedev.seijakulistkmp.features.character.domain.model.CharacterDetail
import com.yumedev.seijakulistkmp.features.character.domain.repository.CharacterDetailRepository

class CharacterDetailRepositoryImpl(
    private val apolloClient: ApolloClient
) : CharacterDetailRepository {

    override suspend fun getCharacterDetail(id: Int): Result<CharacterDetail> {
        return try {
            val response = apolloClient
                .query(GetCharacterDetailQuery(id = id))
                .execute()

            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error"))
            } else {
                val character = response.data?.Character?.toCharacterDetail()
                if (character != null) {
                    Result.success(character)
                } else {
                    Result.failure(Exception("Character not found"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
