package com.yumedev.seijakulistkmp.features.character.data.mapper

import com.yumedev.seijakulistkmp.data.remote.graphql.GetCharacterDetailQuery
import com.yumedev.seijakulistkmp.features.character.domain.model.CharacterDetail
import com.yumedev.seijakulistkmp.features.character.domain.model.CharacterMedia
import com.yumedev.seijakulistkmp.features.character.domain.model.VoiceActor
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType

fun GetCharacterDetailQuery.Character.toCharacterDetail(): CharacterDetail {
    val fullName = name?.full ?: "Unknown"
    val nativeName = name?.native
    val alternativeNamesList = name?.alternative?.filterNotNull() ?: emptyList()
    val alternativeSpoilerNamesList = name?.alternativeSpoiler?.filterNotNull() ?: emptyList()

    val imageUrl = image?.large ?: image?.medium

    val dateOfBirthStr = dateOfBirth?.let { date ->
        buildString {
            date.day?.let { append(it) }
            date.month?.let { month ->
                if (isNotEmpty()) append(" ")
                append(getMonthName(month))
            }
            date.year?.let { year ->
                if (isNotEmpty()) append(", ")
                append(year)
            }
        }.takeIf { it.isNotBlank() }
    }

    val allVoiceActors = mutableListOf<VoiceActor>()

    val characterMediaList = media?.edges?.mapNotNull { edge ->
        edge?.node?.let { node ->
            val displayTitle = node.title?.english ?: node.title?.romaji ?: node.title?.native ?: "Unknown"
            val nativeTitle = node.title?.native
            val coverUrl = node.coverImage?.large ?: node.coverImage?.medium

            val mediaType = when (node.type?.rawValue) {
                "ANIME" -> MediaType.ANIME
                "MANGA" -> MediaType.MANGA
                else -> MediaType.ANIME
            }

            val startDateStr = node.startDate?.let { date ->
                buildString {
                    date.month?.let { month ->
                        append(getMonthName(month))
                        append(" ")
                    }
                    date.year?.let { append(it) }
                }.takeIf { it.isNotBlank() }
            }

            val voiceActorsForMedia = edge.voiceActorRoles?.mapNotNull { vaRole ->
                vaRole?.voiceActor?.let { va ->
                    VoiceActor(
                        id = va.id,
                        name = va.name?.full ?: "Unknown",
                        nameNative = va.name?.native,
                        imageUrl = va.image?.large ?: va.image?.medium,
                        language = va.language?.rawValue,
                        roleNotes = vaRole.roleNotes
                    )
                }
            } ?: emptyList()

            allVoiceActors.addAll(voiceActorsForMedia)

            CharacterMedia(
                id = node.id,
                title = displayTitle,
                titleNative = nativeTitle,
                coverImageUrl = coverUrl,
                type = mediaType,
                format = node.format?.rawValue,
                status = node.status?.rawValue,
                averageScore = node.averageScore?.toDouble(),
                popularity = node.popularity,
                startDate = startDateStr,
                characterRole = edge.characterRole?.rawValue ?: "UNKNOWN",
                voiceActorsForMedia = voiceActorsForMedia
            )
        }
    } ?: emptyList()

    val uniqueVoiceActors = allVoiceActors.distinctBy { it.id }

    return CharacterDetail(
        id = id,
        name = fullName,
        nameNative = nativeName,
        alternativeNames = alternativeNamesList,
        alternativeSpoilerNames = alternativeSpoilerNamesList,
        imageUrl = imageUrl,
        description = description?.replace("<br>", "\n")?.replace(Regex("<[^>]*>"), ""),
        gender = gender,
        dateOfBirth = dateOfBirthStr,
        age = age,
        bloodType = bloodType,
        isFavourite = isFavourite,
        favourites = favourites,
        siteUrl = siteUrl,
        media = characterMediaList,
        voiceActors = uniqueVoiceActors
    )
}

private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "ene"
        2 -> "feb"
        3 -> "mar"
        4 -> "abr"
        5 -> "may"
        6 -> "jun"
        7 -> "jul"
        8 -> "ago"
        9 -> "sep"
        10 -> "oct"
        11 -> "nov"
        12 -> "dic"
        else -> ""
    }
}
