package com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun RegistrationsFilterItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    value: String,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }

    // Анимации
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(150),
        label = "scale"
    )

    val containerColor by animateColorAsState(
        targetValue = when {
            checked -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.primaryContainer
        },
        animationSpec = tween(300),
        label = "containerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            checked -> MaterialTheme.colorScheme.onSecondary
            else -> MaterialTheme.colorScheme.onBackground
        },
        animationSpec = tween(300),
        label = "contentColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            checked -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        },
        animationSpec = tween(300),
        label = "borderColor"
    )

    FilterChip(
        modifier = modifier.scale(scale),
        selected = checked,
        onClick = {
            if (!checked) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            onClick()
        },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontFamily = if (checked) FontNunito.bold() else FontNunito.regular(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = contentColor
                )

                if (checked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Выбрано",
                        modifier = Modifier.size(16.dp),
                        tint = contentColor
                    )
                }
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColor,
            selectedContainerColor = containerColor,
            labelColor = contentColor,
            selectedLabelColor = contentColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = checked,
            borderColor = borderColor,
            selectedBorderColor = borderColor,
            borderWidth = 1.dp,
            selectedBorderWidth = 2.dp
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = FilterChipDefaults.filterChipElevation(
            elevation = if (checked) 4.dp else 2.dp,
            pressedElevation = 6.dp
        )
    )
}
