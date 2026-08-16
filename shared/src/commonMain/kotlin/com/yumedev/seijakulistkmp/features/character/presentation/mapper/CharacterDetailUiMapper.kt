package com.yumedev.seijakulistkmp.features.character.presentation.mapper

import com.yumedev.seijakulistkmp.features.character.domain.model.CharacterDetail
import com.yumedev.seijakulistkmp.features.character.presentation.model.CharacterDetailStrings
import com.yumedev.seijakulistkmp.features.character.presentation.model.CharacterDetailUiModel
import com.yumedev.seijakulistkmp.features.character.presentation.model.MediaAppearanceUiModel
import com.yumedev.seijakulistkmp.features.character.presentation.model.VoiceActorUiModel
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType

fun CharacterDetail.toUiModel(strings: CharacterDetailStrings): CharacterDetailUiModel {
    val (cleanedDescription, metadata) = parseDescriptionMetadata(description)

    val mainCharacterRole = media.firstOrNull { it.characterRole == "MAIN" }
    val roleLabel = when {
        mainCharacterRole != null -> strings.mainRole
        media.any { it.characterRole == "SUPPORTING" } -> strings.supportingRole
        else -> null
    }

    val formattedFavorites = favourites?.let { formatNumber(it) }

    val appearancesCount = media.size
    val formattedAppearances = appearancesCount.toString()

    val formattedRanking = favourites?.let { fav ->
        if (fav >= 1000) "#${(fav / 100).coerceAtMost(999)}"
        else null
    }

    val formattedHeight = metadata["height"]?.let { heightValue ->
        val heightMatch = Regex("(\\d+)").find(heightValue)
        heightMatch?.groupValues?.get(1)?.let { "$it ${strings.cmUnit}" }
    }

    val formattedBirthday = metadata["birthday"]?.let { birthday ->
        formatBirthdayFromMetadata(birthday)
    } ?: dateOfBirth

    val formattedAge = metadata["age"] ?: age

    val mediaAppearances = media.map { mediaItem ->
        val typeLabel = when (mediaItem.type) {
            MediaType.ANIME -> when (mediaItem.format?.uppercase()) {
                "MOVIE" -> strings.formatMovie
                else -> strings.formatAnime
            }
            MediaType.MANGA -> strings.formatManga
        }

        val role = when (mediaItem.characterRole) {
            "MAIN" -> strings.mainRole
            "SUPPORTING" -> strings.supportingRole
            else -> ""
        }

        MediaAppearanceUiModel(
            id = mediaItem.id,
            title = mediaItem.title,
            coverImageUrl = mediaItem.coverImageUrl,
            typeLabel = "$typeLabel · $role",
            roleLabel = role
        )
    }

    val voiceActorsUi = voiceActors.distinctBy { it.id }.take(3).map { va ->
        VoiceActorUiModel(
            id = va.id,
            name = va.name,
            imageUrl = va.imageUrl,
            languageLabel = va.language?.let { lang ->
                when (lang.lowercase()) {
                    "japanese" -> "Japonés (original)"
                    "english" -> "English (dub)"
                    "spanish" -> "Español (dub)"
                    else -> lang
                }
            }
        )
    }

    return CharacterDetailUiModel(
        id = id,
        name = name,
        nameNative = nameNative,
        imageUrl = imageUrl,
        roleLabel = roleLabel,
        formattedFavorites = formattedFavorites,
        formattedAppearances = formattedAppearances,
        formattedRanking = formattedRanking,
        age = formattedAge,
        height = formattedHeight,
        birthday = formattedBirthday,
        nickname = metadata["nickname"] ?: alternativeNames.firstOrNull(),
        description = cleanedDescription,
        media = mediaAppearances,
        voiceActors = voiceActorsUi,
        isFavorite = isFavourite
    )
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> {
            val millions = number / 100_000
            "${millions / 10}.${millions % 10}M"
        }
        number >= 1_000 -> {
            val thousands = number / 100
            "${thousands / 10}.${thousands % 10}K"
        }
        else -> number.toString()
    }
}

private fun formatDateOfBirth(dateString: String): String {
    val parts = dateString.split("-")
    if (parts.size != 3) return dateString

    val months = listOf(
        "enero", "febrero", "marzo", "abril", "mayo", "junio",
        "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
    )

    val day = parts[2].toIntOrNull() ?: return dateString
    val month = parts[1].toIntOrNull()?.let { months.getOrNull(it - 1) } ?: return dateString

    return "$day de $month"
}

private fun formatBirthdayFromMetadata(birthday: String): String {
    val monthsEnglish = mapOf(
        "january" to "enero", "february" to "febrero", "march" to "marzo",
        "april" to "abril", "may" to "mayo", "june" to "junio",
        "july" to "julio", "august" to "agosto", "september" to "septiembre",
        "october" to "octubre", "november" to "noviembre", "december" to "diciembre"
    )

    val parts = birthday.trim().split(" ")
    if (parts.size >= 2) {
        val (first, second) = parts[0] to parts[1]

        val monthName = monthsEnglish[first.lowercase()]
        if (monthName != null) {
            val day = second.filter { it.isDigit() }
            return if (day.isNotEmpty()) "$day de $monthName" else birthday
        }

        val monthName2 = monthsEnglish[second.lowercase()]
        if (monthName2 != null) {
            val day = first.filter { it.isDigit() }
            return if (day.isNotEmpty()) "$day de $monthName2" else birthday
        }
    }

    return birthday
}

private fun parseDescriptionMetadata(description: String?): Pair<String?, Map<String, String>> {
    if (description.isNullOrBlank()) return description to emptyMap()

    val metadata = mutableMapOf<String, String>()

    val metadataRegex = Regex("(?:__|\\*\\*)([^_*:]+):(?:__|\\*\\*)?\\s*([^\\n]+)")

    val matches = metadataRegex.findAll(description)

    matches.forEach { match ->
        val label = match.groupValues[1].trim().lowercase()
        val value = match.groupValues[2].trim()

        when {
            label.contains("height", ignoreCase = true) || label.contains("altura", ignoreCase = true) -> {
                metadata["height"] = value
            }
            label.contains("age", ignoreCase = true) || label.contains("edad", ignoreCase = true) -> {
                metadata["age"] = value
            }
            label.contains("birthday", ignoreCase = true) || label.contains("birth", ignoreCase = true) ||
            label.contains("cumpleaños", ignoreCase = true) || label.contains("nacimiento", ignoreCase = true) -> {
                metadata["birthday"] = value
            }
            label.contains("nickname", ignoreCase = true) || label.contains("apodo", ignoreCase = true) ||
            label.contains("alias", ignoreCase = true) -> {
                metadata["nickname"] = value
            }
            label.contains("blood", ignoreCase = true) || label.contains("sangre", ignoreCase = true) -> {
                metadata["bloodType"] = value
            }
        }
    }

    val cleanedDescription = metadataRegex.replace(description, "")
        .trim()
        .replace(Regex("^\\s*\\n+"), "")

    return cleanedDescription.takeIf { it.isNotBlank() } to metadata
}
