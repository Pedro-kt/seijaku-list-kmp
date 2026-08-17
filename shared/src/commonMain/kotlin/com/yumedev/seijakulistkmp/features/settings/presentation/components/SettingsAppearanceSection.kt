package com.yumedev.seijakulistkmp.features.settings.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun SettingsAppearanceSection(
    selectedTheme: ThemeMode,
    selectedLanguage: LanguageMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onLanguageSelected: (LanguageMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.settings_appearance),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(Res.string.settings_theme),
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThemeChip(
                    label = stringResource(Res.string.settings_theme_system),
                    selected = selectedTheme == ThemeMode.SYSTEM,
                    onClick = { onThemeSelected(ThemeMode.SYSTEM) }
                )
                ThemeChip(
                    label = stringResource(Res.string.settings_theme_light),
                    selected = selectedTheme == ThemeMode.LIGHT,
                    onClick = { onThemeSelected(ThemeMode.LIGHT) }
                )
                ThemeChip(
                    label = stringResource(Res.string.settings_theme_dark),
                    selected = selectedTheme == ThemeMode.DARK,
                    onClick = { onThemeSelected(ThemeMode.DARK) }
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(Res.string.settings_language),
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThemeChip(
                    label = stringResource(Res.string.settings_language_system),
                    selected = selectedLanguage == LanguageMode.SYSTEM,
                    onClick = { onLanguageSelected(LanguageMode.SYSTEM) }
                )
                ThemeChip(
                    label = stringResource(Res.string.settings_language_english),
                    selected = selectedLanguage == LanguageMode.ENGLISH,
                    onClick = { onLanguageSelected(LanguageMode.ENGLISH) }
                )
                ThemeChip(
                    label = stringResource(Res.string.settings_language_spanish),
                    selected = selectedLanguage == LanguageMode.SPANISH,
                    onClick = { onLanguageSelected(LanguageMode.SPANISH) }
                )
            }
        }
    }
}

@Composable
private fun ThemeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    )
}
