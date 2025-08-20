package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import kotlinx.coroutines.delay

@Composable
internal fun StartTitle(modifier: Modifier = Modifier, title: String, place: String) {
    var isVisible by rememberSaveable(title) { mutableStateOf(false) }
    var hasAnimated by rememberSaveable(title) { mutableStateOf(false) }

    LaunchedEffect(title) {
        if (!hasAnimated) {
            delay(50) // Small delay for smoother appearance
            isVisible = true
            hasAnimated = true
        } else {
            isVisible = true // Show immediately if already animated
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 600, easing = EaseOutCubic)
        ) + slideInVertically(
            animationSpec = tween(durationMillis = 700, easing = EaseOutCubic),
            initialOffsetY = { it / 4 } // Smoother, shorter slide distance
        )
    ) {
        Column(modifier = modifier) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontFamily = FontNunito.extraBold(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}