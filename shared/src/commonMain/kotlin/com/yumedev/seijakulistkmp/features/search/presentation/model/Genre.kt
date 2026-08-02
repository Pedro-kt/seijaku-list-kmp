package com.yumedev.seijakulistkmp.features.search.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

enum class Genre {
    ACTION,
    ADVENTURE,
    COMEDY,
    DRAMA,
    FANTASY,
    ROMANCE,
    SCI_FI,
    SLICE_OF_LIFE,
    SUSPENSE,
    SPORTS,
    HORROR,
    PSYCHOLOGICAL;

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
