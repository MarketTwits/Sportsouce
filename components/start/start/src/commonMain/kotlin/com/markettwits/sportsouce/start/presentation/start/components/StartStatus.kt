package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon
import com.markettwits.sportsouce.starts.common.presentation.startStatusBackground
import com.markettwits.sportsouce.starts.common.presentation.startStatusMessage
import kotlinx.coroutines.delay

@Composable
internal fun StartStatus(
    modifier: Modifier = Modifier,
    organizers: List<Organizer>,
    kindOfSports: List<StartItem.KindOfSport>,
    startStatus: StartItem.StartStatus,
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }
    var hasAnimated by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(startStatus.code) {
        if (!hasAnimated) {
            delay(200) // Slight delay after StartTitle
            isVisible = true
            hasAnimated = true
        } else {
            isVisible = true
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 500, easing = EaseOutCubic)
        ) + slideInVertically(
            animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
            initialOffsetY = { it / 5 }
        )
    ) {
        val mainOrganizer = organizers.find { it.isMain } ?: organizers.firstOrNull()

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status badge - takes available space
            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .widthIn(min = 150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(startStatusBackground(startStatus.code).copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(startStatusBackground(startStatus.code).copy(alpha = 0.8f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = startStatusMessage(startStatus.code),
                        fontFamily = FontNunito.semiBoldBold(),
                        fontSize = 14.sp,
                        color = androidx.compose.ui.graphics.Color.White,
                        maxLines = 1
                    )
                }
            }

            // Sport type icons (without names to save space)
            kindOfSports.take(2).forEach { sport ->
                val sportListItem = StartsListItem.KindOfSport(
                    id = sport.id,
                    name = sport.name
                )
                val kindOfSportsColor = remember {
                    startKindOfSportsColor(sportListItem)
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(kindOfSportsColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = startKindOfSportsIcon(sportListItem),
                        contentDescription = sport.name,
                        tint = kindOfSportsColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            mainOrganizer?.let { organizer ->
                val photoPath = organizer.photo?.fullPath
                if (!photoPath.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        SubcomposeAsyncImage(
                            model = imageRequestCrossfade(model = photoPath),
                            contentDescription = "Logo организатора ${organizer.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            error = {
                                OrganizerInitials(name = organizer.name)
                            },
                            success = {
                                SubcomposeAsyncImageContent()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun OrganizerInitials(name: String) {
    val initials = name.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")

    Text(
        text = initials.ifEmpty { "?" },
        color = MaterialTheme.colorScheme.secondary,
        fontFamily = FontNunito.bold(),
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    )
}