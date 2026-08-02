package com.yumedev.seijakulistkmp.features.search.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

enum class SearchFilter {
    ANIME,
    MANGA,
    CHARACTERS
}

@Composable
fun SearchFilter.toLabel(): String = when (this) {
    SearchFilter.ANIME -> stringResource(Res.string.nav_anime)
    SearchFilter.MANGA -> stringResource(Res.string.nav_manga)
    SearchFilter.CHARACTERS -> stringResource(Res.string.search_filter_characters)
}
