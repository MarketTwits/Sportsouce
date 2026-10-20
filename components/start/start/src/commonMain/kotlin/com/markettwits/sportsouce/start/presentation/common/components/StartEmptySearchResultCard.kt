package com.markettwits.sportsouce.start.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlinx.coroutines.delay

@Composable
fun StartEmptySearchResultCard(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
) {
    var isVisible by remember(title, message) { mutableStateOf(false) }

    LaunchedEffect(title, message) {
        isVisible = false
        delay(180)
        isVisible = true
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        OnBackgroundCard(
            modifier = Modifier
                .widthIn(max = 560.dp),
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 450)) +
                        scaleIn(
                            animationSpec = tween(durationMillis = 450),
                            initialScale = 0.92f
                        ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .background(
                                color = SauceTheme.colors.surfaceSubtle,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Ничего не найдено",
                            tint = SauceTheme.colors.textMuted,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Text(
                        text = title,
                        color = SauceTheme.colors.textPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = message,
                        color = SauceTheme.colors.textSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
