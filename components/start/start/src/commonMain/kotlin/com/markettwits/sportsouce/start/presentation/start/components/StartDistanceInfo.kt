package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import kotlinx.coroutines.delay

@Composable
internal fun StartDistanceInfo(
    modifier: Modifier = Modifier,
    distances: List<DistinctDistance>,
    membersUi: List<StartMembersUi>? = null,
) {
    if (distances.isNotEmpty()) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        var showStatistics by rememberSaveable { mutableStateOf(false) }
        val rotationAngle by animateFloatAsState(
            targetValue = if (isExpanded) 180f else 0f,
            animationSpec = tween(durationMillis = 300)
        )

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable { isExpanded = !isExpanded },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsRun,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Дистанции",
                            fontSize = 18.sp,
                            fontFamily = FontNunito.bold(),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(rotationAngle)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val displayedDistances = if (isExpanded) distances else distances.take(3)
                    displayedDistances.forEach { distance ->
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = distance.name,
                                    fontFamily = FontNunito.medium(),
                                    fontSize = 14.sp
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        )
                    }
                }

                // Add statistics section if membersUi is provided
                membersUi?.let { members ->
                    if (members.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Statistics header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .noRippleClickable { showStatistics = !showStatistics },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                                    shape = CircleShape,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BarChart,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(4.dp).size(16.dp)
                                    )
                                }
                                Text(
                                    text = "Регистрации",
                                    fontSize = 16.sp,
                                    fontFamily = FontNunito.semiBoldBold(),
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                            val statisticsRotationAngle by animateFloatAsState(
                                targetValue = if (showStatistics) 180f else 0f,
                                animationSpec = tween(durationMillis = 300)
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .rotate(statisticsRotationAngle)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        AnimatedVisibility(
                            visible = showStatistics,
                            enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                            exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                        ) {
                            val statisticsData = members.mapToRegistrationDistance()
                            val maxCount = statisticsData.maxOfOrNull { it.count } ?: 1

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                statisticsData.take(if (showStatistics) statisticsData.size else 3).forEach { member ->
                                    AnimatedBarChartItem(
                                        member = member,
                                        maxCount = maxCount
                                    )
                                }

                                if (statisticsData.size > 3 && !showStatistics) {
                                    TextButton(
                                        onClick = { showStatistics = true },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Все дистанции (${statisticsData.size})",
                                            fontFamily = FontNunito.semiBoldBold(),
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                AnimatedVisibility(
                    visible = !isExpanded && distances.size > 3,
                    enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
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
                    .height(18.dp)
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
private data class StartRegistrationDistanceStatistics(
    val count: Int,
    val distance: String,
    val color: Color,
)
