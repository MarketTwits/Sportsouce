package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.progress.SauceLinearProgressIndicator
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun StartDistanceInfo(modifier: Modifier = Modifier, distances: List<DistinctDistance>, membersUi: List<StartMembersUi>? = null) {
    if (distances.isEmpty()) return
    var expanded by rememberSaveable(distances.map { it.id }) { mutableStateOf(false) }
    var statisticsVisible by rememberSaveable { mutableStateOf(true) }
    StartContentBasePanel(modifier, "Дистанции", Icons.AutoMirrored.Filled.DirectionsRun,
        onHeaderClick = if (distances.size > 3) ({ expanded = !expanded }) else null, expanded = expanded) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                (if (expanded) distances else distances.take(3)).forEach { SauceStatusBadge(it.name, showIndicator = false) }
            }
            if (!membersUi.isNullOrEmpty()) {
                StartSectionHeading("Регистрации", Icons.Outlined.BarChart,
                    onClick = { statisticsVisible = !statisticsVisible }, expanded = statisticsVisible)
                AnimatedVisibility(statisticsVisible,
                    enter = expandVertically(tween(SauceMotion.feedbackMillis)),
                    exit = shrinkVertically(tween(SauceMotion.feedbackMillis))) {
                    val groups = membersUi.groupBy { it.distance }
                    val maximum = groups.values.maxOfOrNull { it.size }?.coerceAtLeast(1) ?: 1
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                        groups.forEach { (distance, members) ->
                            SauceLinearProgressIndicator(label = distance, progress = members.size.toFloat() / maximum,
                                valueLabel = members.size.toString())
                        }
                    }
                }
            }
        }
    }
}
