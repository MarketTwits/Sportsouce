package com.markettwits.sportsouce.club.info.presentation.components.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun SportHeaderSection(
    title: String,
    subtitle: String? = null,
    description: String? = null,
    badge: String? = null,
    hazeState: HazeState,
    height: Int = 400,
    primaryColor: Color = SportSouceColor.SportSouceRegistryOpenGreen,
    backgroundImage: Painter? = null,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")

    val animatedOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )

    val animatedOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        // Background image layer (bottom)
        backgroundImage?.let { painter ->
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .blur(2.dp)
                    .alpha(0.6f),
                contentScale = ContentScale.Crop
            )
        }

        // Color gradient layer (above image)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = if (backgroundImage != null) 0.7f else 1f),
                            primaryColor.copy(alpha = if (backgroundImage != null) 0.5f else 0.8f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        )

        // Blur эффект
        Box(
            modifier = Modifier
                .matchParentSize()
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        tint = HazeTint(primaryColor.copy(alpha = 0.3f)),
                        blurRadius = 24.dp
                    )
                )
        )

        // Анимированные геометрические узоры в правом углу
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .alpha(animatedAlpha)
        ) {
            val circleRadius = 80.dp.toPx()

            // Большой круг с анимацией
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = circleRadius,
                center = Offset(
                    size.width * 0.6f + animatedOffset1,
                    size.height * 0.4f + animatedOffset2
                )
            )

            // Средний круг с анимацией
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = circleRadius * 0.6f,
                center = Offset(
                    size.width * 0.8f - animatedOffset2,
                    size.height * 0.2f + animatedOffset1
                )
            )

            // Малый круг с анимацией
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = circleRadius * 0.3f,
                center = Offset(
                    size.width * 0.9f + animatedOffset1 * 0.5f,
                    size.height * 0.6f - animatedOffset2 * 0.5f
                )
            )
        }

        // Дополнительный анимированный паттерн слева
        Canvas(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 30.dp)
                .alpha(animatedAlpha * 0.7f)
        ) {
            val circleRadius = 60.dp.toPx()

            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = circleRadius * 0.8f,
                center = Offset(
                    size.width * 0.3f - animatedOffset1 * 0.8f,
                    size.height * 0.5f + animatedOffset2 * 0.8f
                )
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Контейнер с правильным расположением элементов
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Badge сверху с отступом
            badge?.let {
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        fontFamily = FontNunito.black(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Spacer для гибкого распределения пространства
            if (badge != null) {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Центральный контент с ограничением роста
            Column(
                modifier = Modifier.weight(1f, fill = false),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 32.sp,
                    fontFamily = FontNunito.black(),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )

                subtitle?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 32.sp,
                        fontFamily = FontNunito.black(),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp
                    )
                }

                description?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontFamily = FontNunito.medium(),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}