package com.yumedev.seijakulistkmp.features.search.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.*
import org.jetbrains.compose.resources.StringResource
import seijakulistkmp.shared.generated.resources.*

enum class MoodFilter(
    val titleRes: StringResource,
    val descriptionRes: StringResource,
    val icon: ImageVector,
    val genres: List<String>
) {
    CRY(
        titleRes = Res.string.mood_cry_title,
        descriptionRes = Res.string.mood_cry_description,
        icon = TablerIcons.Outlined.CloudRain,
        genres = listOf("Drama", "Romance")
    ),
    ADRENALINE(
        titleRes = Res.string.mood_adrenaline_title,
        descriptionRes = Res.string.mood_adrenaline_description,
        icon = TablerIcons.Outlined.Bolt,
        genres = listOf("Action", "Sports", "Adventure")
    ),
    CALM(
        titleRes = Res.string.mood_calm_title,
        descriptionRes = Res.string.mood_calm_description,
        icon = TablerIcons.Outlined.Plant,
        genres = listOf("Slice of Life", "Iyashikei")
    ),
    LAUGH(
        titleRes = Res.string.mood_laugh_title,
        descriptionRes = Res.string.mood_laugh_description,
        icon = TablerIcons.Outlined.MoodHappy,
        genres = listOf("Comedy", "Slice of Life")
    ),
    MIND_BREAKING(
        titleRes = Res.string.mood_mind_breaking_title,
        descriptionRes = Res.string.mood_mind_breaking_description,
        icon = TablerIcons.Outlined.Bulb,
        genres = listOf("Psychological", "Mystery", "Thriller")
    ),
    MARATHON(
        titleRes = Res.string.mood_marathon_title,
        descriptionRes = Res.string.mood_marathon_description,
        icon = TablerIcons.Outlined.Infinity,
        genres = listOf()
    )
}
