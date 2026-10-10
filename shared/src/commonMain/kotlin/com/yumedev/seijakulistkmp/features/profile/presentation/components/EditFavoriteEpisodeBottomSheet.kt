package com.yumedev.seijakulistkmp.features.profile.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Star
import dev.seyfarth.tablericons.outlined.Star
import dev.seyfarth.tablericons.outlined.Trash
import dev.seyfarth.tablericons.filled.Star as StarFilled
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.list_cancel
import seijakulistkmp.shared.generated.resources.list_delete_confirm
import seijakulistkmp.shared.generated.resources.list_delete_confirm_message
import seijakulistkmp.shared.generated.resources.list_delete_confirm_title
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_cancel
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_comment_label
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_comment_placeholder
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_delete
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_edit_title
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_info
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_rating_label
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_save
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_star_desc
import seijakulistkmp.shared.generated.resources.profile_favorite_episode_stars_desc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFavoriteEpisodeBottomSheet(
    episode: FavoriteEpisode,
    animeTitle: String?,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSave: (rating: Int?, comment: String?) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableStateOf(episode.rating) }
    var comment by remember { mutableStateOf(episode.comment ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(episode.rating, episode.comment) {
        rating = episode.rating
        comment = episode.comment ?: ""
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.profile_favorite_episode_edit_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = TablerIcons.Outlined.Trash,
                        contentDescription = stringResource(Res.string.profile_favorite_episode_delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!animeTitle.isNullOrBlank()) {
                    Text(
                        text = animeTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(Res.string.profile_favorite_episode_info, episode.episodeNumber, episode.episodeTitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.profile_favorite_episode_rating_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (rating != null && i <= rating!!) {
                                TablerIcons.Filled.Star
                            } else {
                                TablerIcons.Outlined.Star
                            },
                            contentDescription = if (i == 1) {
                                stringResource(Res.string.profile_favorite_episode_star_desc, i)
                            } else {
                                stringResource(Res.string.profile_favorite_episode_stars_desc, i)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clickable {
                                    rating = if (rating == i) null else i
                                },
                            tint = if (rating != null && i <= rating!!) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.profile_favorite_episode_comment_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(Res.string.profile_favorite_episode_comment_placeholder)) },
                    minLines = 3,
                    maxLines = 6
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(Res.string.profile_favorite_episode_cancel))
                }
                Button(
                    onClick = {
                        onSave(rating, comment.ifBlank { null })
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(Res.string.profile_favorite_episode_save))
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = stringResource(Res.string.list_delete_confirm_title))
            },
            text = {
                Text(text = stringResource(Res.string.list_delete_confirm_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDismiss()
                        onDelete()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(Res.string.list_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(Res.string.list_cancel))
                }
            }
        )
    }
}
