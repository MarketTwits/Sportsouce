package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance

@Composable
internal fun StartDistanceInfo(
    modifier: Modifier = Modifier,
    distances: List<DistinctDistance>,
) {
    if (distances.isNotEmpty()) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
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
