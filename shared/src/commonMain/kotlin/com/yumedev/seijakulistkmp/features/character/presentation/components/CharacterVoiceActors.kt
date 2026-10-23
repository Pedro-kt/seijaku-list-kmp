package com.yumedev.seijakulistkmp.features.character.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme
import com.yumedev.seijakulistkmp.features.character.presentation.model.VoiceActorUiModel
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun CharacterVoiceActors(
    voiceActors: List<VoiceActorUiModel>,
    modifier: Modifier = Modifier
) {
    if (voiceActors.isEmpty()) return

    val responsive = ResponsiveTheme.values

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
    ) {
        Text(
            text = stringResource(Res.string.character_voice_actor),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = responsive.horizontalPadding)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding),
            contentPadding = PaddingValues(start = responsive.horizontalPadding, end = responsive.horizontalPadding)
        ) {
            items(voiceActors) { voiceActor ->
                VoiceActorItem(voiceActor = voiceActor)
            }
        }
    }
}

@Composable
private fun VoiceActorItem(
    voiceActor: VoiceActorUiModel,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values

    Column(
        modifier = modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        ) {
            voiceActor.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = voiceActor.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = voiceActor.name.take(1),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Text(
            text = voiceActor.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        voiceActor.languageLabel?.let { language ->
            Text(
                text = language,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
