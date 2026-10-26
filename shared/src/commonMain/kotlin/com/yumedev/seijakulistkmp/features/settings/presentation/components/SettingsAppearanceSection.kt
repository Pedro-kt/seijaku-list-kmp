package com.yumedev.seijakulistkmp.features.settings.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme
import com.yumedev.seijakulistkmp.features.settings.domain.model.LanguageMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeVariant
import com.yumedev.seijakulistkmp.features.tracking.presentation.components.ExpressiveFilterChip
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun SettingsAppearanceSection(
    selectedTheme: ThemeMode,
    selectedThemeVariant: ThemeVariant,
    selectedLanguage: LanguageMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onThemeVariantSelected: (ThemeVariant) -> Unit,
    onLanguageSelected: (LanguageMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = responsive.horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
    ) {
        Text(
            text = stringResource(Res.string.settings_appearance),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Column(verticalArrangement = Arrangement.spacedBy(responsive.smallSpacing)) {
            Text(
                text = stringResource(Res.string.settings_theme),
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(responsive.smallSpacing)
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

        Column(verticalArrangement = Arrangement.spacedBy(responsive.smallSpacing)) {
            Text(
                text = stringResource(Res.string.settings_theme_variant),
                style = MaterialTheme.typography.bodyLarge
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(responsive.smallSpacing)
            ) {
                item {
                    ThemeChip(
                        label = stringResource(Res.string.settings_theme_variant_sumi),
                        selected = selectedThemeVariant == ThemeVariant.SUMI,
                        onClick = { onThemeVariantSelected(ThemeVariant.SUMI) }
                    )
                }
                item {
                    ThemeChip(
                        label = stringResource(Res.string.settings_theme_variant_sakura),
                        selected = selectedThemeVariant == ThemeVariant.SAKURA,
                        onClick = { onThemeVariantSelected(ThemeVariant.SAKURA) }
                    )
                }
                item {
                    ThemeChip(
                        label = stringResource(Res.string.settings_theme_variant_ocean),
                        selected = selectedThemeVariant == ThemeVariant.OCEAN,
                        onClick = { onThemeVariantSelected(ThemeVariant.OCEAN) }
                    )
                }
                item {
                    ThemeChip(
                        label = stringResource(Res.string.settings_theme_variant_yozora),
                        selected = selectedThemeVariant == ThemeVariant.YOZORA,
                        onClick = { onThemeVariantSelected(ThemeVariant.YOZORA) }
                    )
                }
                item {
                    ThemeChip(
                        label = stringResource(Res.string.settings_theme_variant_momiji),
                        selected = selectedThemeVariant == ThemeVariant.MOMIJI,
                        onClick = { onThemeVariantSelected(ThemeVariant.MOMIJI) }
                    )
                }
                item {
                    ThemeChip(
                        label = stringResource(Res.string.settings_theme_variant_matcha),
                        selected = selectedThemeVariant == ThemeVariant.MATCHA,
                        onClick = { onThemeVariantSelected(ThemeVariant.MATCHA) }
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(responsive.smallSpacing)) {
            Text(
                text = stringResource(Res.string.settings_language),
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(responsive.smallSpacing)
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
    ExpressiveFilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        modifier = modifier
    )
}
