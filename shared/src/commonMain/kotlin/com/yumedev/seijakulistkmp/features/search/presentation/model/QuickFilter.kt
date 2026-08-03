package com.yumedev.seijakulistkmp.features.search.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

sealed class QuickFilter {
    data object CurrentSeason : QuickFilter()
    data object AiringToday : QuickFilter()
    data object Top100 : QuickFilter()
    data object Random : QuickFilter()

    companion object {
        fun all(): List<QuickFilter> = listOf(
            CurrentSeason,
            AiringToday,
            Top100,
            Random
        )
    }
}

@Composable
fun QuickFilter.toLabel(): String = when (this) {
    QuickFilter.CurrentSeason -> stringResource(Res.string.quick_filter_current_season)
    QuickFilter.AiringToday -> stringResource(Res.string.quick_filter_airing_today)
    QuickFilter.Top100 -> stringResource(Res.string.quick_filter_top_100)
    QuickFilter.Random -> stringResource(Res.string.quick_filter_random)
}
