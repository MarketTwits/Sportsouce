package com.markettwits.sportsouce.starts.common.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
fun StartCardV3(
    modifier: Modifier = Modifier,
    start: StartsListItem,
    onItemClick: (StartsListItem) -> Unit,
) {
    val screenInfo = rememberScreenSizeInfo()
    val isCompactScreen = screenInfo.wDP < 390.dp
    val isVeryCompactScreen = screenInfo.wDP < 340.dp
    val overlayPadding = if (isVeryCompactScreen) 6.dp else 10.dp
    val overlayBadgeFontSize = if (isVeryCompactScreen) 11.sp else 12.sp
    val statusText = if (isCompactScreen) {
        startStatusCompactMessage(start.statusCode.id)
    } else {
        startStatusMessage(start.statusCode.id)
    }

    val organizer = start.organizers.firstOrNull { it.isMain } ?: start.organizers.firstOrNull()
    val organizerLogo = organizer?.photo?.fullPath?.takeIf { it.isNotBlank() }
    val slotsText = start.slots.toSlotsText()

    OnBackgroundCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = { onItemClick(start) }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(Shapes.medium)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        shape = Shapes.medium
                    )
            ) {
                AsyncImage(
                    model = imageRequestCrossfade(start.image),
                    filterQuality = FilterQuality.Medium,
                    contentDescription = start.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = ColorPainter(MaterialTheme.colorScheme.primaryContainer),
                    error = if (start.image.isEmpty()) {
                        DefaultImages.EmptyImageStart()
                    } else {
                        ColorPainter(MaterialTheme.colorScheme.primaryContainer)
                    }
                )

                Column(
                    modifier = Modifier
                        .padding(overlayPadding)
                        .align(Alignment.BottomStart),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StartCardV3ImageOverlayBadge(
                        text = statusText,
                        backgroundColor = startStatusBackground(start.statusCode.id).copy(alpha = 0.94f),
                        fontSize = overlayBadgeFontSize,
                    )

                    StartCardV3ImageOverlayBadge(
                        text = start.date,
                        backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.91f),
                        icon = {
                            Icon(
                                modifier = Modifier.size(13.dp),
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        fontSize = overlayBadgeFontSize,
                    )
                }

                if (slotsText != null) {
                    StartCardV3ImageOverlayBadge(
                        modifier = Modifier
                            .padding(overlayPadding)
                            .align(Alignment.BottomEnd),
                        text = slotsText,
                        backgroundColor = Color.Black.copy(alpha = 0.46f),
                        icon = if (isVeryCompactScreen) null else {
                            {
                                Icon(
                                    modifier = Modifier.size(13.dp),
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        fontSize = overlayBadgeFontSize,
                    )
                }

                if (organizer != null) {
                    Column(
                        modifier = Modifier
                            .padding(overlayPadding)
                            .align(Alignment.TopEnd),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(Shapes.large)
                                .background(Color.Black.copy(alpha = 0.58f))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.animateContentSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                AnimatedVisibility(
                                    visible = !isCompactScreen,
                                    enter = fadeIn() + expandHorizontally(),
                                    exit = fadeOut() + shrinkHorizontally()
                                ) {
                                    Text(
                                        text = organizer.name,
                                        color = Color.White,
                                        fontFamily = FontNunito.medium(),
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                if (organizerLogo != null) {
                                    AsyncImage(
                                        model = imageRequestCrossfade(organizerLogo),
                                        contentDescription = organizer.name,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(Shapes.large)
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                                shape = Shapes.large
                                            ),
                                        contentScale = ContentScale.Crop,
                                        placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                                        error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(Shapes.large)
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(16.dp),
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (isVeryCompactScreen) 8.dp else 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val firstSport = start.kindOfSports.firstOrNull()
                    if (firstSport != null) {
                        val sportColor = startKindOfSportsColor(firstSport)
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(Shapes.large)
                                .background(sportColor.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = startKindOfSportsIcon(firstSport),
                                contentDescription = null,
                                tint = sportColor
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.weight(1f),
                        text = start.name,
                        fontSize = 18.sp,
                        lineHeight = 22.sp,
                        fontFamily = FontNunito.bold(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    start.kindOfSports.take(if (isCompactScreen) 2 else 3).forEach { sport ->
                        val sportColor = startKindOfSportsColor(sport)
                        StartCardV3Badge(
                            text = sport.name,
                            backgroundColor = sportColor.copy(alpha = 0.16f),
                            contentColor = sportColor,
                        )
                    }
                }

                if (start.distance.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(14.dp),
                            imageVector = Icons.Default.NearMe,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                        HtmlText(
                            modifier = Modifier.fillMaxWidth(),
                            text = start.distance,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            maxLines = if (isCompactScreen) 2 else 3,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontNunito.medium(),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                StartCardV3MetaRow(
                    icon = {
                        Icon(
                            modifier = Modifier.size(14.dp),
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    },
                    text = start.place
                )
            }
        }
    }
}

@Composable
private fun StartCardV3MetaRow(
    icon: @Composable () -> Unit,
    text: String,
) {
    if (text.isBlank()) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        icon()
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            color = MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.medium(),
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StartCardV3Badge(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
) {
    Box(
        modifier = Modifier
            .clip(Shapes.medium)
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontFamily = FontNunito.medium(),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun StartsListItem.Slots?.toSlotsText(): String? {
    if (this == null) return null
    val open = openSlots.trim().ifEmpty { "0" }
    val total = totalSlots.trim().ifEmpty { "-" }
    return "$open/$total"
}

@Composable
private fun StartCardV3ImageOverlayBadge(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    icon: @Composable (() -> Unit)? = null,
    fontSize: androidx.compose.ui.unit.TextUnit = 12.sp,
) {
    Box(
        modifier = modifier
            .clip(Shapes.large)
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon?.invoke()
            Text(
                text = text,
                color = Color.White,
                fontFamily = FontNunito.medium(),
                fontSize = fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
