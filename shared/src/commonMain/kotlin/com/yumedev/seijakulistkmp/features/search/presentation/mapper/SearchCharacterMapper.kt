package com.yumedev.seijakulistkmp.features.search.presentation.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.SearchCharacterQuery
import com.yumedev.seijakulistkmp.features.search.presentation.model.CharacterResultItem
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaAppearance

fun SearchCharacterQuery.Character.toCharacterResultItem(): CharacterResultItem {
    val characterName = name?.full ?: name?.native ?: "Unknown"
    val characterImage = image?.large ?: image?.medium ?: ""

    val appearances = media?.edges?.zip(media.nodes ?: emptyList())?.map { (edge, node) ->
        MediaAppearance(
            mediaId = node?.id ?: 0,
            mediaTitle = node?.title?.english ?: node?.title?.romaji ?: "Unknown",
            role = edge?.characterRole?.rawValue
        )
    } ?: emptyList()

    return CharacterResultItem(
        id = id,
        name = characterName,
        image = characterImage,
        mediaAppearances = appearances
    )
}

fun List<SearchCharacterQuery.Character?>.toCharacterResultItems(): List<CharacterResultItem> {
    return this.filterNotNull().map { it.toCharacterResultItem() }
}
