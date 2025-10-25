package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import kotlinx.coroutines.delay

@Composable
internal fun StartMembersStatistics(
    modifier: Modifier = Modifier,
    membersUi: List<StartMembersUi>
) {
    if (membersUi.isNotEmpty()) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }

        StartContentBasePanel(
            modifier = modifier,
            label = "Регистрации по дистанциям",
            icon = Icons.Default.BarChart
        ) {
            val distances = membersUi.mapToRegistrationDistance()
            val maxCount = distances.maxOfOrNull { it.count } ?: 1
            val displayedDistances = if (isExpanded) distances else distances.take(3)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                displayedDistances.forEach { member ->
                    AnimatedBarChartItem(
                        member = member,
                        maxCount = maxCount
                    )
                }

                AnimatedVisibility(
                    visible = distances.size > 3,
                    enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                ) {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isExpanded) "Свернуть" else "Ещё ${distances.size - 3} дистанций",
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun AnimatedBarChartItem(
    member: StartRegistrationDistanceStatistics,
    maxCount: Int,
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (animationPlayed) member.count.toFloat() / maxCount else 0f,
        animationSpec = tween(durationMillis = 800, delayMillis = 100),
        label = "bar_animation"
    )

    LaunchedEffect(Unit) {
        delay(100)
        animationPlayed = true
    }

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = member.distance,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 12.sp,
                modifier = Modifier.width(100.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .clip(Shapes.small)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(Shapes.small)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    member.color,
                                    member.color.copy(alpha = 0.8f)
                                )
                            )
                        )
                )
            }
            Text(
                text = member.count.toString(),
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = FontNunito.bold(),
                fontSize = 12.sp,
                modifier = Modifier.width(30.dp)
            )
        }
    }
}


@Composable
private fun List<StartMembersUi>.mapToRegistrationDistance(): List<StartRegistrationDistanceStatistics> {
    val brightColors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFFFFE66D),
        Color(0xFF95E1D3),
        Color(0xFFFF8C42),
        Color(0xFF6C5CE7),
        Color(0xFFFD79A8),
        Color(0xFF00B894),
        Color(0xFFFECE2F),
        Color(0xFF0984E3),
        Color(0xFFE17055),
        Color(0xFFA29BFE),
        Color(0xFF55EFC4),
        Color(0xFFFF7675),
        Color(0xFF74B9FF)
    )

    return this
        .groupBy { it.distance }
        .toList()
        .mapIndexed { index, pair ->
            StartRegistrationDistanceStatistics(
                count = pair.second.size,
                distance = pair.first,
                color = brightColors[index % brightColors.size]
            )
        }
}


@Stable
@Immutable
private data class StartRegistrationDistanceStatistics(
    val count: Int,
    val distance: String,
    val color: Color
)
