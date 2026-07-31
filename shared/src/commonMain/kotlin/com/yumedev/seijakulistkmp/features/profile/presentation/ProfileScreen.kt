package com.yumedev.seijakulistkmp.features.profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

/**
 * Profile screen - User profile and settings
 */
class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        ProfileScreenContent()
    }
}

@Composable
fun ProfileScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
