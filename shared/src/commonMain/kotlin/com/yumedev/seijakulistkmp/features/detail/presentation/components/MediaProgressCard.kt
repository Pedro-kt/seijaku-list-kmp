package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.validator.MediaListValidator
import com.yumedev.seijakulistkmp.ui.theme.ExpressiveMotion
import com.yumedev.seijakulistkmp.ui.theme.InteractionAnimations
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme
import com.yumedev.seijakulistkmp.ui.theme.SeijakuTheme
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.DotsVertical
import dev.seyfarth.tablericons.outlined.Edit
import dev.seyfarth.tablericons.outlined.Minus
import dev.seyfarth.tablericons.outlined.Plus
import dev.seyfarth.tablericons.outlined.Trash
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MediaProgressCard(
    entry: MediaListEntry,
    mediaType: MediaType,
    mediaStatus: String?,
    totalEpisodes: Int?,
    totalChapters: Int?,
    temporaryProgress: Int?,
    originalProgress: Int?,
    onIncrementProgress: () -> Unit,
    onDecrementProgress: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    var expanded by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showDecrementButton by remember { mutableStateOf(false) }
    var lastActionTime by remember { mutableStateOf(0L) }
    var cachedProgress by remember { mutableStateOf<Int?>(null) }

    val canIncrement = MediaListValidator.canIncrementProgress(
        currentStatus = entry.status,
        currentProgress = temporaryProgress ?: entry.progress,
        total = when (mediaType) {
            MediaType.ANIME -> totalEpisodes
            MediaType.MANGA -> totalChapters
        },
        mediaStatus = mediaStatus
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(Res.string.list_delete_confirm_title)) },
            text = { Text(stringResource(Res.string.list_delete_confirm_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
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

    val displayProgress = temporaryProgress ?: cachedProgress ?: entry.progress

    LaunchedEffect(temporaryProgress) {
        if (temporaryProgress != null) {
            cachedProgress = temporaryProgress
        }
    }

    LaunchedEffect(entry.progress) {
        if (temporaryProgress == null) {
            cachedProgress = entry.progress
        }
    }

    val progressText = when (mediaType) {
        MediaType.ANIME -> totalEpisodes?.let { "$displayProgress/$it" } ?: "$displayProgress"
        MediaType.MANGA -> totalChapters?.let { "$displayProgress/$it" } ?: "$displayProgress"
    }

    val canDecrement = temporaryProgress != null && temporaryProgress > 0

    LaunchedEffect(lastActionTime) {
        if (lastActionTime > 0L) {
            delay(3000)
            showDecrementButton = false
        }
    }

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(durationMillis = 400)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeOut(animationSpec = tween(durationMillis = 300)),
        modifier = modifier
    ) {
        Box {
            HorizontalFloatingToolbar(
                expanded = expanded,
                floatingActionButton = {
                    FloatingToolbarDefaults.VibrantFloatingActionButton(
                        onClick = {
                            if (canIncrement) {
                                onIncrementProgress()
                                showDecrementButton = true
                                lastActionTime = System.currentTimeMillis()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Plus,
                            contentDescription = stringResource(Res.string.list_increase)
                        )
                    }
                },
                modifier = Modifier.wrapContentWidth(),
                shape = RoundedCornerShape(50),
                colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
                    toolbarContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                contentPadding = PaddingValues(
                    horizontal = responsive.mediumPadding,
                    vertical = responsive.mediumPadding
                )
            ) {
            Crossfade(
                targetState = showDecrementButton && canDecrement,
                animationSpec = tween(durationMillis = 400),
                modifier = Modifier.align(Alignment.CenterVertically)
            ) { showDecrement ->
                if (showDecrement) {
                    DecrementChip(
                        onDecrementClick = {
                            onDecrementProgress()
                            showDecrementButton = true
                            lastActionTime = System.currentTimeMillis()
                        }
                    )
                } else {
                    StatusChip(
                        status = entry.status,
                        mediaType = mediaType
                    )
                }
            }

            Spacer(modifier = Modifier.width(responsive.mediumSpacing))

            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = progressText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = responsive.mediumPadding, vertical = responsive.smallPadding)
                )
            }

            Spacer(modifier = Modifier.width(responsive.mediumSpacing))

            Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                Surface(
                    onClick = { showMenu = true },
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Icon(
                        imageVector = TablerIcons.Outlined.DotsVertical,
                        contentDescription = stringResource(Res.string.filter_menu),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(responsive.smallPadding)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.list_edit_title)) },
                        onClick = {
                            showMenu = false
                            onEditClick()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = TablerIcons.Outlined.Edit,
                                contentDescription = null
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(Res.string.detail_delete_tooltip),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = TablerIcons.Outlined.Trash,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }

            /*
            if (showDecrementButton && canDecrement) {
                FloatingToolbarDefaults.VibrantFloatingActionButton(
                    onClick = {
                        onDecrementProgress()
                        showDecrementButton = false
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-responsive.mediumPadding), y = (-80).dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(
                        imageVector = TablerIcons.Outlined.Minus,
                        contentDescription = stringResource(Res.string.list_decrease)
                    )
                }
            }
            */
        }
    }
}

@Composable
private fun DecrementChip(
    onDecrementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) InteractionAnimations.PRESS_SCALE else 1f,
        animationSpec = ExpressiveMotion.quickSpring
    )

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.2f else 0.12f,
        animationSpec = ExpressiveMotion.smoothSpring
    )

    Surface(
        onClick = onDecrementClick,
        modifier = modifier.scale(scale),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surface,
        interactionSource = interactionSource
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(responsive.smallSpacing),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = TablerIcons.Outlined.Minus,
                contentDescription = stringResource(Res.string.list_decrease),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(Res.string.list_decrease),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun StatusChip(
    status: MediaListStatus,
    mediaType: MediaType,
    modifier: Modifier = Modifier
) {
    val statusText = when (status) {
        MediaListStatus.CURRENT -> if (mediaType == MediaType.ANIME) {
            stringResource(Res.string.list_status_watching)
        } else {
            stringResource(Res.string.list_status_reading)
        }
        MediaListStatus.COMPLETED -> stringResource(Res.string.list_status_completed)
        MediaListStatus.PLANNING -> if (mediaType == MediaType.ANIME) {
            stringResource(Res.string.list_status_plan_to_watch)
        } else {
            stringResource(Res.string.list_status_plan_to_read)
        }
        MediaListStatus.PAUSED -> stringResource(Res.string.list_status_paused)
        MediaListStatus.DROPPED -> stringResource(Res.string.list_status_dropped)
        MediaListStatus.REPEATING -> if (mediaType == MediaType.ANIME) {
            stringResource(Res.string.list_status_repeating)
        } else {
            stringResource(Res.string.list_status_rereading)
        }
    }

    val statusColor = when (status) {
        MediaListStatus.CURRENT -> Color(0xFF2196F3)
        MediaListStatus.COMPLETED -> Color(0xFF4CAF50)
        MediaListStatus.PLANNING -> Color(0xFF9E9E9E)
        MediaListStatus.PAUSED -> Color(0xFFFFA726)
        MediaListStatus.DROPPED -> Color(0xFFEF5350)
        MediaListStatus.REPEATING -> MaterialTheme.colorScheme.tertiary
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) InteractionAnimations.PRESS_SCALE else 1f,
        animationSpec = ExpressiveMotion.quickSpring
    )

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.2f else 0.12f,
        animationSpec = ExpressiveMotion.smoothSpring
    )

    Surface(
        onClick = { },
        modifier = modifier.scale(scale),
        shape = RoundedCornerShape(50),
        color = statusColor.copy(alpha = backgroundAlpha),
        interactionSource = interactionSource
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelLarge,
            color = statusColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

