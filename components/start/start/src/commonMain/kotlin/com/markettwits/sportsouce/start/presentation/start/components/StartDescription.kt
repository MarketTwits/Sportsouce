package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import kotlinx.coroutines.delay


@Composable
internal fun StartDescription(modifier: Modifier, description: String, isPartialData: Boolean = false) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isVisible by rememberSaveable(description) { mutableStateOf(false) }
    var hasAnimated by rememberSaveable(description) { mutableStateOf(false) }

    LaunchedEffect(description) {
        if (!hasAnimated) {
            delay(150) // Small delay for smoother appearance
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
            initialOffsetY = { it / 4 }
        )
    ) {
        Column(modifier) {
            if (description.isNotEmpty()) {
                HtmlText(
                    modifier = Modifier.animateContentSize(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = EaseOutCubic
                        )
                    ),
                    text = if (expanded) description else description.take(350),
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium(),
                    lineHeight = 16.sp,
                    selectable = true,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                AnimatedVisibility(description.length > 350) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(Shapes.medium)
                                .clickable { expanded = !expanded },
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(10.dp),
                                text = if (expanded) "Скрыть" else "Подробнее",
                                fontFamily = FontNunito.bold(),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }
            }
        }
    }
}