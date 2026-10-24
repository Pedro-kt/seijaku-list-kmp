package com.yumedev.seijakulistkmp.features.character.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.core.clipboard.rememberClipboardManager
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Copy
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun CharacterHeader(
    imageUrl: String?,
    name: String,
    nameNative: String?,
    roleLabel: String?,
    formattedFavorites: String?,
    formattedAppearances: String?,
    formattedRanking: String?,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    val clipboardManager = rememberClipboardManager()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = responsive.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(responsive.largePadding)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .size(responsive.largeIconSize * 3)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { clipboardManager.copyToClipboard(name) },
                    modifier = Modifier.size(responsive.smallIconSize)
                ) {
                    Icon(
                        imageVector = TablerIcons.Outlined.Copy,
                        contentDescription = "Copiar nombre",
                        modifier = Modifier.size(responsive.smallIconSize),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            nameNative?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing)
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = { clipboardManager.copyToClipboard(it) },
                        modifier = Modifier.size(responsive.smallIconSize)
                    ) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Copy,
                            contentDescription = "Copiar nombre nativo",
                            modifier = Modifier.size(responsive.smallIconSize),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            roleLabel?.let { label ->
                Surface(
                    shape = RoundedCornerShape(responsive.cornerRadius),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = responsive.smallPadding)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = responsive.mediumPadding, vertical = responsive.smallSpacing)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
        ) {
            formattedFavorites?.let { favorites ->
                StatCard(
                    label = stringResource(Res.string.character_favorites).uppercase(),
                    value = favorites,
                    modifier = Modifier.weight(1f)
                )
            }

            formattedAppearances?.let { appearances ->
                StatCard(
                    label = stringResource(Res.string.character_appearances).uppercase(),
                    value = appearances,
                    modifier = Modifier.weight(1f)
                )
            }

            formattedRanking?.let { ranking ->
                StatCard(
                    label = stringResource(Res.string.character_ranking).uppercase(),
                    value = ranking,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = RoundedCornerShape(responsive.cornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(vertical = responsive.largePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
