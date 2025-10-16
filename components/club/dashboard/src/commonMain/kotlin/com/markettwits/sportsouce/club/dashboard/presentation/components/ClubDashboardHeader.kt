package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import org.jetbrains.compose.resources.painterResource
import sportsouce.components.club.dashboard.generated.resources.Res
import sportsouce.components.club.dashboard.generated.resources.im_club_main_image

@Composable
fun ClubDashboardHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "header_animation")

    val floatAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_animation"
    )

    val borderAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "border_animation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
    ) {
        // Background image with overlay
        Image(
            painter = painterResource(Res.drawable.im_club_main_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Dark overlay for text readability
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        // Decorative animated circles
        SportDecorativeElements(floatAnimation)

        // Smooth bottom transition
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        // Main content card with animated border
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .drawBehind {
                    val strokeWidth = 1.5.dp.toPx()
                    val gradientColors = listOf(
                        Color(0xFF5AE4C0),
                        Color(0xFF70BFF5),
                        Color(0xFF8B5CF6),
                        Color(0xFFEC4899),
                        Color(0xFF5AE4C0)
                    )

                    val gradientStart = (borderAnimation * gradientColors.size).toInt()
                    val currentColors = mutableListOf<Color>()
                    for (i in 0 until 3) {
                        val index = (gradientStart + i) % gradientColors.size
                        currentColors.add(gradientColors[index])
                    }

                    drawRoundRect(
                        brush = Brush.linearGradient(currentColors),
                        style = Stroke(width = strokeWidth),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                    )
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = if (isSystemInDarkTheme()) 0.95f else 0.92f
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .wrapContentWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Animated logo background
                AnimatedIconBackground(
                    colors = listOf(Color(0xFF5AE4C0), Color(0xFF70BFF5)),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Gradient title text
                val gradientColors = listOf(
                    Color(0xFF5AE4C0),
                    Color(0xFF70BFF5),
                    Color(0xFF8B5CF6),
                    Color(0xFFEC4899)
                )

                val animatedBrush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(
                        x = floatAnimation * 200f,
                        y = 0f
                    ),
                    end = Offset(
                        x = floatAnimation * 200f + 150f,
                        y = 0f
                    )
                )

                Text(
                    text = "СПОРТ СОЮЗ",
                    fontSize = 30.sp,
                    fontFamily = FontNunito.black(),
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(brush = animatedBrush)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "КОМАНДА И КЛУБ",
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sport icons row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SportIcon(
                        icon = Icons.Default.DownhillSkiing,
                        label = "Лыжи",
                        animationValue = floatAnimation,
                        colors = listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
                    )
                    SportIcon(
                        icon = Icons.Default.Pool,
                        label = "Плавание",
                        animationValue = floatAnimation,
                        delay = 1000,
                        colors = listOf(Color(0xFF06B6D4), Color(0xFF0891B2))
                    )
                    SportIcon(
                        icon = Icons.Default.FitnessCenter,
                        label = "Бег",
                        animationValue = floatAnimation,
                        delay = 2000,
                        colors = listOf(Color(0xFF10B981), Color(0xFF059669))
                    )
                }
            }
        }
    }
}

@Composable
internal fun AnimatedIconBackground(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient_animation")

    val animatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_shift"
    )

    val backgroundColors = colors.map { color ->
        Color(
            red = color.red,
            green = color.green,
            blue = color.blue,
            alpha = 0.1f + (animatedFloat * 0.05f)
        )
    }

    val iconColors = colors

    Box(
        modifier = modifier
            .background(
                brush = Brush.radialGradient(
                    colors = backgroundColors,
                    radius = 50f + (animatedFloat * 20f)
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides iconColors.first()
        ) {
            content()
        }
    }
}

@Composable
private fun SportDecorativeElements(animationValue: Float) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Floating circles with different animations
        repeat(6) { index ->
            val offsetX = (50 + index * 60).dp
            val offsetY = (100 + index * 45).dp
            val size = (20 + index * 8).dp
            val opacity = 0.1f + (animationValue * 0.15f)

            Box(
                modifier = Modifier
                    .offset(
                        x = offsetX + (animationValue * 10).dp, // Уменьшили с 20 до 10
                        y = offsetY + (kotlin.math.sin(animationValue * kotlin.math.PI + index) * 5).dp // Уменьшили с 10 до 5
                    )
                    .size(size)
                    .background(
                        color = if (index % 2 == 0)
                            Color(0xFF5AE4C0).copy(alpha = opacity)
                        else
                            Color(0xFF70BFF5).copy(alpha = opacity),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun SportIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    animationValue: Float,
    colors: List<Color>,
    delay: Int = 0,
) {
    val adjustedAnimation = (animationValue + delay / 4000f) % 1f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = (kotlin.math.sin(adjustedAnimation * 2 * kotlin.math.PI) * 2).dp)
            .wrapContentSize()
    ) {
        AnimatedIconBackground(
            colors = colors,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = colors.first(),
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
            textAlign = TextAlign.Center
        )
    }
}