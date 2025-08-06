package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import kotlinx.coroutines.delay

@Composable
internal fun AddMemberActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetY = { it }
        ) + fadeIn(animationSpec = tween(400)) + scaleIn(
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            initialScale = 0.8f
        )
    ) {
        ExtendedFloatingActionButton(
            modifier = modifier,
            text = {
                Text(
                    color = MaterialTheme.colorScheme.onSecondary,
                    text = "Добавить участника",
                    fontFamily = FontNunito.bold(),
                    fontSize = 14.sp,
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить участника",
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
            },
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            shape = RoundedCornerShape(16.dp),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 12.dp,
                hoveredElevation = 8.dp
            )
        )
    }
}
