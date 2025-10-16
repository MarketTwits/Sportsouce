package com.markettwits.sportsouce.club.info.presentation.components.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun SportHeaderSection(
    title: String,
    subtitle: String? = null,
    description: String? = null,
    badge: String? = null,
    height: Int = 400,
    primaryColor: Color = SportSouceColor.SportSouceRegistryOpenGreen,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")

    val animatedRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        // Neutral background
        Box(
            modifier = Modifier
                .matchParentSize()
        )

        // Animated floating shapes background
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawFloatingShapes(
                primaryColor = primaryColor,
                rotation = animatedRotation,
                scale = animatedScale
            )
        }

        // Content container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Badge at top
            badge?.let {
                Box(
                    modifier = Modifier
                        .background(
                            primaryColor.copy(alpha = 0.15f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        fontFamily = FontNunito.black(),
                        color = primaryColor,
                        letterSpacing = 1.sp
                    )
                }
            }

            if (badge != null) {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Main content
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
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun DrawScope.drawFloatingShapes(
    primaryColor: Color,
    rotation: Float,
    scale: Float,
) {
    val width = size.width
    val height = size.height

    // Draw floating rectangles with primaryColor variations
    val shapes = listOf(
        Triple(primaryColor, 0.35f, 1.2f),
        Triple(primaryColor, 0.28f, 0.8f),
        Triple(primaryColor, 0.20f, 1.5f),
        Triple(primaryColor, 0.15f, 0.6f)
    )

    shapes.forEachIndexed { index, (color, alpha, speedMultiplier) ->
        val shapeCount = 4 + index * 2

        for (i in 0 until shapeCount) {
            val angle = (i * 360f / shapeCount + rotation * speedMultiplier) * kotlin.math.PI / 180
            val distance = (width / 4) + (index * 30)

            val x = width / 2 + cos(angle).toFloat() * distance * scale
            val y = height / 2 + sin(angle).toFloat() * distance * scale

            val shapeSize = (20f + index * 10f) * scale

            // Draw rounded rectangles
            val path = Path()
            val cornerRadius = shapeSize / 4

            path.addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = x - shapeSize / 2,
                    top = y - shapeSize / 2,
                    right = x + shapeSize / 2,
                    bottom = y + shapeSize / 2,
                    radiusX = cornerRadius,
                    radiusY = cornerRadius
                )
            )

            drawPath(
                path = path,
                color = color.copy(alpha = alpha)
            )
        }
    }

    // Add scattered dots for texture
    for (i in 0 until 30) {
        val x = (width * 0.05f) + (i % 6) * (width * 0.15f) +
                cos((rotation + i * 24f) * kotlin.math.PI / 180).toFloat() * 25f
        val y = (height * 0.08f) + (i / 6) * (height * 0.16f) +
                sin((rotation + i * 24f) * kotlin.math.PI / 180).toFloat() * 25f

        drawCircle(
            color = primaryColor.copy(alpha = 0.25f),
            radius = 4f * scale,
            center = Offset(x, y)
        )
    }

    // Add some triangular shapes for variety
    for (i in 0 until 8) {
        val angle = (i * 45f + rotation * 0.5f) * kotlin.math.PI / 180
        val distance = width / 3 + (i % 2) * 40f
        val x = width / 2 + cos(angle).toFloat() * distance * scale
        val y = height / 2 + sin(angle).toFloat() * distance * scale

        val triangleSize = (15f + i % 3 * 5f) * scale
        val path = Path()

        // Create triangle
        path.moveTo(x, y - triangleSize)
        path.lineTo(x - triangleSize * 0.8f, y + triangleSize * 0.5f)
        path.lineTo(x + triangleSize * 0.8f, y + triangleSize * 0.5f)
        path.close()

        drawPath(
            path = path,
            color = primaryColor.copy(alpha = 0.12f)
        )
    }
}

@Composable
internal fun SubscribeGradientButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF5AE4C0),
                        Color(0xFF70BFF5)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Записаться",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}