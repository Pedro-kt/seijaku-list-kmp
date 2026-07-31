package com.yumedev.seijakulistkmp.features.main.presentation

import org.jetbrains.compose.resources.StringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.nav_anime
import seijakulistkmp.shared.generated.resources.nav_home
import seijakulistkmp.shared.generated.resources.nav_manga
import seijakulistkmp.shared.generated.resources.nav_profile
import seijakulistkmp.shared.generated.resources.nav_search

sealed class BottomNavItem(
    val route: String,
    val titleRes: StringResource
) {

    data object Anime : BottomNavItem(
        route = "anime",
        titleRes = Res.string.nav_anime
    )

    data object Manga : BottomNavItem(
        route = "manga",
        titleRes = Res.string.nav_manga
    )

    data object Home : BottomNavItem(
        route = "home",
        titleRes = Res.string.nav_home
    )

    data object Search : BottomNavItem(
        route = "search",
        titleRes = Res.string.nav_search
    )

    data object Profile : BottomNavItem(
        route = "profile",
        titleRes = Res.string.nav_profile
    )

    companion object {
        val items: List<BottomNavItem> = listOf(Anime, Manga, Home, Search, Profile)
    }
}
