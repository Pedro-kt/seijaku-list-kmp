package com.yumedev.seijakulistkmp.features.main.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.yumedev.seijakulistkmp.features.anime.presentation.list.AnimeListScreenContent
import com.yumedev.seijakulistkmp.features.home.presentation.HomeScreenContent
import com.yumedev.seijakulistkmp.features.manga.presentation.MangaListScreenContent
import com.yumedev.seijakulistkmp.features.profile.presentation.ProfileScreenContent
import com.yumedev.seijakulistkmp.features.search.presentation.SearchScreenContent
import androidx.compose.ui.graphics.vector.ImageVector
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Book
import dev.seyfarth.tablericons.filled.DeviceTv
import dev.seyfarth.tablericons.filled.Home
import dev.seyfarth.tablericons.filled.User
import dev.seyfarth.tablericons.outlined.Book
import dev.seyfarth.tablericons.outlined.DeviceTv
import dev.seyfarth.tablericons.outlined.Home
import dev.seyfarth.tablericons.outlined.Search
import dev.seyfarth.tablericons.outlined.User
import org.jetbrains.compose.resources.stringResource


class MainScreen : Screen {
    @Composable
    override fun Content() {
        MainScreenContent()
    }
}

@Composable
fun MainScreenContent() {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                TabNavigationItems(
                    selectedItem = selectedItem,
                    onItemSelected = { selectedItem = it }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem) {
                BottomNavItem.Home -> HomeScreenContent()
                BottomNavItem.Anime -> AnimeListScreenContent()
                BottomNavItem.Manga -> MangaListScreenContent()
                BottomNavItem.Search -> SearchScreenContent()
                BottomNavItem.Profile -> ProfileScreenContent()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItems(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    data class NavItemIcons(
        val item: BottomNavItem,
        val outlinedIcon: ImageVector,
        val filledIcon: ImageVector
    )

    val items = listOf(
        NavItemIcons(
            item = BottomNavItem.Anime,
            outlinedIcon = TablerIcons.Outlined.DeviceTv,
            filledIcon = TablerIcons.Filled.DeviceTv
        ),
        NavItemIcons(
            item = BottomNavItem.Manga,
            outlinedIcon = TablerIcons.Outlined.Book,
            filledIcon = TablerIcons.Filled.Book
        ),
        NavItemIcons(
            item = BottomNavItem.Home,
            outlinedIcon = TablerIcons.Outlined.Home,
            filledIcon = TablerIcons.Filled.Home
        ),
        NavItemIcons(
            item = BottomNavItem.Search,
            outlinedIcon = TablerIcons.Outlined.Search,
            filledIcon = TablerIcons.Outlined.Search,
        ),
        NavItemIcons(
            item = BottomNavItem.Profile,
            outlinedIcon = TablerIcons.Outlined.User,
            filledIcon = TablerIcons.Filled.User
        )
    )

    items.forEach { navItem ->
        val isSelected = selectedItem == navItem.item
        val title = stringResource(navItem.item.titleRes)

        NavigationBarItem(
            selected = isSelected,
            onClick = { onItemSelected(navItem.item) },
            icon = {
                Icon(
                    imageVector = if (isSelected) navItem.filledIcon else navItem.outlinedIcon,
                    contentDescription = title
                )
            },
            label = {
                Text(text = title)
            },
            alwaysShowLabel = true
        )
    }
}
