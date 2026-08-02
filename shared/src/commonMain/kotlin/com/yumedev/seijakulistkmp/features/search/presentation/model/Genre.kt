package com.yumedev.seijakulistkmp.features.search.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.*
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

enum class Genre(val icon: ImageVector) {
    ACTION(TablerIcons.Outlined.Sword),
    ADVENTURE(TablerIcons.Outlined.Map),
    COMEDY(TablerIcons.Outlined.MoodSmile),
    DRAMA(TablerIcons.Outlined.Mask),
    FANTASY(TablerIcons.Outlined.Wand),
    ROMANCE(TablerIcons.Outlined.Heart),
    SCI_FI(TablerIcons.Outlined.Rocket),
    SLICE_OF_LIFE(TablerIcons.Outlined.Coffee),
    SUSPENSE(TablerIcons.Outlined.Eye),
    SPORTS(TablerIcons.Outlined.BallFootball),
    HORROR(TablerIcons.Outlined.Ghost),
    PSYCHOLOGICAL(TablerIcons.Outlined.Brain);

    companion object {
        fun all(): List<Genre> = entries
    }
}

@Composable
fun Genre.toLabel(): String = when (this) {
    Genre.ACTION -> stringResource(Res.string.genre_action)
    Genre.ADVENTURE -> stringResource(Res.string.genre_adventure)
    Genre.COMEDY -> stringResource(Res.string.genre_comedy)
    Genre.DRAMA -> stringResource(Res.string.genre_drama)
    Genre.FANTASY -> stringResource(Res.string.genre_fantasy)
    Genre.ROMANCE -> stringResource(Res.string.genre_romance)
    Genre.SCI_FI -> stringResource(Res.string.genre_sci_fi)
    Genre.SLICE_OF_LIFE -> stringResource(Res.string.genre_slice_of_life)
    Genre.SUSPENSE -> stringResource(Res.string.genre_suspense)
    Genre.SPORTS -> stringResource(Res.string.genre_sports)
    Genre.HORROR -> stringResource(Res.string.genre_horror)
    Genre.PSYCHOLOGICAL -> stringResource(Res.string.genre_psychological)
}

fun Genre.toApiValue(): String = when (this) {
    Genre.ACTION -> "Action"
    Genre.ADVENTURE -> "Adventure"
    Genre.COMEDY -> "Comedy"
    Genre.DRAMA -> "Drama"
    Genre.FANTASY -> "Fantasy"
    Genre.ROMANCE -> "Romance"
    Genre.SCI_FI -> "Sci-Fi"
    Genre.SLICE_OF_LIFE -> "Slice of Life"
    Genre.SUSPENSE -> "Thriller"
    Genre.SPORTS -> "Sports"
    Genre.HORROR -> "Horror"
    Genre.PSYCHOLOGICAL -> "Psychological"
}
