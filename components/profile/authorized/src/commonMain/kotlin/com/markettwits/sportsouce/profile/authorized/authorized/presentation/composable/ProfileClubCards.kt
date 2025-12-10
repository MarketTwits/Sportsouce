package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.core_ui.items.theme.SportSouceColor.VeryLighBlue
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ProfileClubCards(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val cards = remember {
        listOf(
            ClubCardInfo(
                title = "Участвуем выгодно",
                subtitle = "· сервис · тренировки · драйв",
                tag = "Pass",
                gradient = listOf(
                    SportSouceColor.SportSouceRegistryOpenGreen.copy(alpha = 0.85f),
                    SportSouceColor.SportSouceLighBlue.copy(alpha = 0.85f),
                ),
                tagColor = SportSouceColor.SportSouceLightRed,
                accent = SportSouceColor.SportSouceBlue,
                accentSecondary = VeryLighBlue
            ),
            ClubCardInfo(
                title = "Твоя команда рядом",
                subtitle = "· расписание · чат · бонусы",
                tag = "Live",
                gradient = listOf(
                    SportSouceColor.SportSouceStartEndedPink.copy(alpha = 0.9f),
                    SportSouceColor.SportSouceDarkRed.copy(alpha = 0.85f),
                ),
                tagColor = SportSouceColor.SportSouceRegistryCommingSoonYellow,
                accent = SportSouceColor.SportSouceRegistryOpenGreen,
                accentSecondary = SportSouceColor.OnPrimaryDark
            ),
            ClubCardInfo(
                title = "Открой новый зал",
                subtitle = "· тестовый визит · скидки · партнёры",
                tag = "New",
                gradient = listOf(
                    SportSouceColor.SportSouceLighBlue.copy(alpha = 0.85f),
                    VeryLighBlue.copy(alpha = 0.9f),
                ),
                tagColor = SportSouceColor.SportSouceBlue,
                accent = SportSouceColor.SportSouceRegistryCommingSoonYellow,
                accentSecondary = SportSouceColor.SportSouceLightRed
            ),
        )
    }

    val pagerState = rememberPagerState(pageCount = { cards.size })

    LaunchedEffect(pagerState.pageCount) {
        while (true) {
            delay(10_000)
            if (pagerState.pageCount > 1) {
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Card(
        modifier = modifier,
        shape = Shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "Клуб",
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = FontNunito.bold(),
                fontSize = 18.sp
            )
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                state = pagerState,
                pageSpacing = 14.dp,
            ) { page ->
                ProfileClubCard(
                    modifier = Modifier.fillMaxWidth(),
                    info = cards[page],
                    onClick = onClick,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(cards.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .height(4.dp)
                            .width(if (isSelected) 14.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.onPrimary.copy(
                                    alpha = if (isSelected) 0.8f else 0.3f
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileClubCard(
    modifier: Modifier = Modifier,
    info: ClubCardInfo,
    onClick: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "clubCardAnimation")

    val circle1Offset by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle1Offset"
    )

    val circle2Scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle2Scale"
    )

    val lockRotation by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lockRotation"
    )

    val lockScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lockScale"
    )

    val passScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "passScale"
    )

    val gradientBrush = Brush.horizontalGradient(colors = info.gradient)

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.55f)
            ) {
                val width = size.width
                val height = size.height

                // Bold diagonal stroke with softer edges
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(info.accent.copy(alpha = 0.16f), Color.Transparent),
                        start = Offset.Zero,
                        end = Offset(width * 0.8f, height * 0.6f)
                    ),
                    size = Size(width = width * 0.82f, height = height * 0.55f),
                    topLeft = Offset(x = width * 0.06f, y = height * 0.12f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(width * 0.08f)
                )

                // Wave stripe kept low to avoid text overlap
                val path = Path().apply {
                    moveTo(width * 0.18f, height * 0.9f)
                    cubicTo(
                        width * 0.35f, height * 0.7f,
                        width * 0.65f, height * 1.05f,
                        width * 0.9f, height * 0.82f
                    )
                    lineTo(width * 0.9f, height)
                    lineTo(width * 0.18f, height)
                    close()
                }
                drawPath(
                    path = path,
                    brush = Brush.horizontalGradient(
                        listOf(
                            info.accentSecondary.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.06f),
                            info.accent.copy(alpha = 0.16f)
                        )
                    )
                )

                // Crisp circles for discipline
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = width * 0.2f,
                    center = Offset(x = width * 0.78f, y = height * 0.26f)
                )
                drawCircle(
                    color = info.accent.copy(alpha = 0.15f),
                    radius = width * 0.14f,
                    center = Offset(x = width * 0.22f, y = height * 0.32f)
                )

                // Thin guiding lines to keep strict look
                val lineColor = Color.White.copy(alpha = 0.12f)
                drawLine(
                    color = lineColor,
                    start = Offset(x = width * 0.05f, y = height * 0.12f),
                    end = Offset(x = width * 0.95f, y = height * 0.12f),
                    strokeWidth = 1.4f
                )
                drawLine(
                    color = lineColor,
                    start = Offset(x = width * 0.08f, y = height * 0.55f),
                    end = Offset(x = width * 0.92f, y = height * 0.45f),
                    strokeWidth = 1.2f
                )
            }

            // Content padding kept internal to avoid clipping background art
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = info.title,
                        fontSize = 22.sp,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text = info.subtitle,
                        fontSize = 14.sp,
                        fontFamily = FontNunito.regular(),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.12f),
                                shape = CircleShape
                            )
                    )

                    Icon(
                        modifier = Modifier
                            .size(38.dp)
                            .scale(lockScale)
                            .rotate(lockRotation),
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Pass",
                        tint = Color.White.copy(alpha = 0.9f)
                    )

                    Surface(
                        modifier = Modifier
                            .offset(x = 20.dp, y = (-12).dp)
                            .align(Alignment.TopEnd)
                            .scale(passScale),
                        shape = RoundedCornerShape(10.dp),
                        color = info.tagColor,
                        shadowElevation = 3.dp
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            text = info.tag,
                            fontSize = 12.sp,
                            fontFamily = FontNunito.bold(),
                            color = Color.White
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 18.dp, y = circle1Offset.dp - 6.dp)
                    .alpha(0.08f)
                    .background(Color.White, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 12.dp, y = 26.dp)
                    .scale(circle2Scale)
                    .alpha(0.04f)
                    .background(Color.White, CircleShape)
            )
        }
    }
}

private data class ClubCardInfo(
    val title: String,
    val subtitle: String,
    val tag: String,
    val gradient: List<Color>,
    val tagColor: Color,
    val accent: Color,
    val accentSecondary: Color,
)
