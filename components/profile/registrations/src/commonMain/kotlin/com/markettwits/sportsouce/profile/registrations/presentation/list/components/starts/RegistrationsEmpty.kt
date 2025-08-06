package com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import kotlinx.coroutines.delay

@Composable
fun RegistrationsAbsolutelyEmpty(modifier: Modifier = Modifier) {
    var isMainContentVisible by remember { mutableStateOf(false) }
    var isSecondaryContentVisible by remember { mutableStateOf(false) }
    var isSportsIconsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isMainContentVisible = true
        delay(400)
        isSportsIconsVisible = true
        delay(300)
        isSecondaryContentVisible = true
    }

    OnBackgroundCard(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        AnimatedVisibility(
            visible = isMainContentVisible,
            enter = slideInVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                initialOffsetY = { it / 2 }
            ) + fadeIn(animationSpec = tween(500))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedSportsIcons(
                    isVisible = isSportsIconsVisible,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                AnimatedMainIcon()

                AnimatedVisibility(
                    visible = isMainContentVisible,
                    enter = fadeIn(animationSpec = tween(600, delayMillis = 300)) +
                            slideInVertically(animationSpec = tween(600, delayMillis = 300))
                ) {
                    Text(
                        text = "Здесь будут ваши спортивные достижения",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.bold(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }

                AnimatedVisibility(
                    visible = isSecondaryContentVisible,
                    enter = fadeIn(animationSpec = tween(500)) +
                            slideInVertically(
                                animationSpec = tween(500),
                                initialOffsetY = { it / 3 }
                            )
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Участвуйте в спортивных стартах, марафонах и соревнованиях — все ваши регистрации сохранятся в истории",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.medium(),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )

                        Text(
                            text = "🏃‍♂️ Начните свой спортивный путь уже сегодня!",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontNunito.bold(),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = "Найдите подходящие старты во вкладке \"Старты\"",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.8f),
                            fontFamily = FontNunito.regular(),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedMainIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "main_icon_transition")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_animation"
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Спортивные достижения",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun AnimatedSportsIcons(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val sportsIcons = listOf(
        Icons.AutoMirrored.Filled.DirectionsRun,
        Icons.Default.FitnessCenter,
        Icons.Default.Timer
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) +
                scaleIn(animationSpec = tween(800, easing = EaseOutBounce)),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            sportsIcons.forEachIndexed { index, icon ->
                AnimatedSportIcon(
                    icon = icon,
                    delay = index * 150L,
                    isVisible = isVisible
                )
            }
        }
    }
}

@Composable
private fun AnimatedSportIcon(
    icon: ImageVector,
    delay: Long,
    isVisible: Boolean,
) {
    var iconVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(delay)
            iconVisible = true
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "sport_icon_transition")

    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000 + (delay.toInt()), easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation_animation"
    )

    AnimatedVisibility(
        visible = iconVisible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            initialOffsetY = { -it }
        ) + fadeIn(animationSpec = tween(400)) +
                scaleIn(animationSpec = tween(400, easing = EaseOutBack))
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                .rotate(rotation),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Спорт",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
