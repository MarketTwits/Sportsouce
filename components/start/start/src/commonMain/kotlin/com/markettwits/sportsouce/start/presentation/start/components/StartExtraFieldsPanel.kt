package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer
import kotlinx.coroutines.delay

@Composable
internal fun StartExtraFieldsPanel(
    modifier: Modifier = Modifier,
    place: String,
    organizers: List<Organizer>,
    startDate: String
) {
    // Create a unique key for this data combination
    val dataKey = "${startDate}_${place}_${organizers.joinToString { it.name }}"

    // Animation states for staggered appearance (top to bottom)
    var showDateRow by rememberSaveable(dataKey) { mutableStateOf(false) }
    var showPlaceRow by rememberSaveable(dataKey) { mutableStateOf(false) }
    var showOrganizersRow by rememberSaveable(dataKey) { mutableStateOf(false) }
    var hasAnimated by rememberSaveable(dataKey) { mutableStateOf(false) }

    // Staggered animation timing - trigger when data changes
    LaunchedEffect(startDate, place, organizers) {
        if (!hasAnimated) {
            delay(50) // Small delay to ensure reset

            if (startDate.isNotEmpty()) {
                showDateRow = true
                delay(150) // Slightly longer delay for smoother sequence
            }
            if (place.isNotEmpty()) {
                showPlaceRow = true
                delay(150)
            }
            if (organizers.isNotEmpty()) {
                showOrganizersRow = true
            }
            hasAnimated = true
        } else {
            // Show immediately if already animated
            if (startDate.isNotEmpty()) showDateRow = true
            if (place.isNotEmpty()) showPlaceRow = true
            if (organizers.isNotEmpty()) showOrganizersRow = true
        }
    }
    
    Column(modifier = modifier) {
        if (startDate.isNotEmpty()) {
            AnimatedVisibility(
                visible = showDateRow,
                enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = EaseOutCubic)) + slideInVertically(
                    animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
                    initialOffsetY = { it / 4 } // Smoother, shorter slide distance
                )
            ) {
                StartExtraFiledRow(
                    icon = Icons.Outlined.DateRange,
                    value = startDate,
                )
            }
        }
        if (place.isNotEmpty()) {
            AnimatedVisibility(
                visible = showPlaceRow,
                enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = EaseOutCubic)) + slideInVertically(
                    animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
                    initialOffsetY = { it / 4 } // Smoother, shorter slide distance
                )
            ) {
                StartExtraFiledRow(
                    icon = Icons.Outlined.Place,
                    value = place,
                )
            }
        }
        if (organizers.isNotEmpty()) {
            AnimatedVisibility(
                visible = showOrganizersRow,
                enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = EaseOutCubic)) + slideInVertically(
                    animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
                    initialOffsetY = { it / 4 } // Smoother, shorter slide distance
                )
            ) {
                StartExtraFiledRow(
                    icon = Icons.Outlined.PersonOutline,
                    value = "Организаторы ${organizers.joinToString(", ") { it.name }}"

                )
            }
        }
    }
}

@Composable
private fun StartExtraFiledRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
) {
    Row(
        modifier = modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "icon",
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontNunito.medium(),
            fontSize = 14.sp
        )
    }
}