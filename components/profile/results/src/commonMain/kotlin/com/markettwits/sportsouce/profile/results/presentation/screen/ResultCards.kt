package com.markettwits.sportsouce.profile.results.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.status.*
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.sportsouce.profile.results.domain.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SummaryStrip(summary: ResultsSummary) {
    val colors = SauceTheme.colors
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SummaryMetric(summary.totalStarts, resultCountWord(summary.totalStarts, "старт", "старта", "стартов"),
                    false, Modifier.weight(1f))
                SummaryMetric(summary.finishes, resultCountWord(summary.finishes, "финиш", "финиша", "финишей"),
                    true, Modifier.weight(1f))
            }
            if (summary.since != null || summary.pendingExternal > 0) {
                HorizontalDivider(color = colors.border)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    summary.since?.let { Text("Участвуете с $it года", style = MaterialTheme.typography.bodySmall, color = colors.textMuted) }
                    if (summary.pendingExternal > 0) Text("На проверке: ${summary.pendingExternal}",
                        style = MaterialTheme.typography.bodySmall, color = colors.warning.content)
                }
            }
        }
    }
}

@Composable
private fun SummaryMetric(value: Int, label: String, finish: Boolean, modifier: Modifier) {
    val colors = SauceTheme.colors
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(shape = SauceShapes.field, color = if (finish) colors.success.container else colors.surfaceSubtle) {
                Icon(if (finish) Icons.Outlined.CheckCircle else Icons.Outlined.Flag, null,
                    Modifier.padding(7.dp).size(18.dp), tint = if (finish) colors.success.content else colors.textSecondary)
            }
            Text(value.toString(), style = MaterialTheme.typography.titleLarge.copy(fontFeatureSettings = "tnum"), color = colors.textPrimary)
        }
        Text(label, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
    }
}

@Composable
internal fun BestResultCard(category: ResultCategory, openStart: (Int) -> Unit) {
    val best = category.best ?: return
    val colors = SauceTheme.colors
    OnBackgroundCard(colors = CardDefaults.cardColors(containerColor = lerp(colors.surface, colors.warning.container, .35f)),
        onClick = best.startId?.takeIf { it > 0 }?.let { { openStart(it) } }) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(shape = SauceShapes.field, color = colors.warning.container) {
                    Icon(Icons.Outlined.EmojiEvents, null, Modifier.padding(9.dp).size(20.dp), tint = colors.warning.content)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(category.label, style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
                    Text(resultTime(best.seconds), style = MaterialTheme.typography.titleLarge.copy(fontFeatureSettings = "tnum"), color = colors.textPrimary)
                }
                if (best.startId != null && best.startId > 0) Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null,
                    Modifier.size(20.dp), tint = colors.textMuted)
            }
            Text(best.startName, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis, color = colors.textSecondary)
            Text(resultDate(best.date), style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MyResultCard(result: MyResult, openStart: (Int) -> Unit) {
    val intent = rememberIntentActionByPlatform()
    val colors = SauceTheme.colors
    val status = result.status?.lowercase()
    val nonFinish = result.kind == "result" && status in setOf("dnf", "dns", "dsq")
    val tone = when {
        result.kind == "rejected" || status == "dsq" -> SauceStatusTone.Danger
        result.kind == "pending" || nonFinish -> SauceStatusTone.Warning
        result.kind == "result" -> SauceStatusTone.Success
        else -> SauceStatusTone.Neutral
    }
    val label = when {
        nonFinish -> status!!.uppercase()
        result.kind == "pending" -> "На проверке"
        result.kind == "rejected" -> "Не подтверждён"
        result.kind == "file" -> "Протокол"
        result.kind == "upcoming" -> "Впереди"
        result.kind == "awaiting" -> "Ожидается"
        result.source == "external" -> "Подтверждён"
        result.isTeam -> "Командный"
        else -> "Финиш"
    }
    OnBackgroundCard(onClick = result.startId?.takeIf { it > 0 && result.source != "external" }?.let { { openStart(it) } }) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(4.dp), itemVerticalAlignment = Alignment.CenterVertically) {
                Text(resultDate(result.date), style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
                SauceStatusBadge(label = label, tone = tone)
            }
            Text(result.startName.ifBlank { "Старт" }, style = MaterialTheme.typography.titleSmall,
                color = colors.textPrimary, maxLines = 2, overflow = TextOverflow.Ellipsis)
            val metadata = listOfNotNull(
                (result.categoryLabel ?: result.distance)?.takeIf { it.isNotBlank() },
                result.bib?.takeIf { it.isNotBlank() }?.let { "№ $it" },
            )
            if (metadata.isNotEmpty()) Text(metadata.joinToString(" · "),
                style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
            if (result.kind in listOf("result", "pending", "rejected")) {
                ResultTimingPanel(result)
                val group = result.group?.takeIf { it.isNotBlank() }
                if (group != null) Text(listOfNotNull(result.groupPlace?.takeIf { it > 0 }?.let { "$it место в группе" }, group).joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall, color = colors.textMuted, maxLines = 2, overflow = TextOverflow.Ellipsis)
            } else if (result.kind == "awaiting") Text("Результаты ещё не опубликованы",
                style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
            result.comment?.takeIf { it.isNotBlank() }?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary) }
            result.fileUrl?.takeIf { it.startsWith("https://") || it.startsWith("http://") }?.let { url ->
                SauceButton("Открыть протокол", { intent.openWebPage(url) }, variant = SauceButtonVariant.Outlined)
            }
        }
    }
}

@Composable
private fun ResultTimingPanel(result: MyResult) {
    val colors = SauceTheme.colors
    Surface(color = colors.surfaceSubtle, shape = SauceShapes.field) {
        BoxWithConstraints(Modifier.fillMaxWidth().padding(12.dp)) {
            val timeStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium, fontFeatureSettings = "tnum")
            val placeStyle = MaterialTheme.typography.titleSmall.copy(fontFeatureSettings = "tnum")
            val measurer = rememberTextMeasurer()
            val density = LocalDensity.current
            val place = result.place?.takeIf { it > 0 }
            val placeLabel = if (place != null) "$place" + (result.finishers?.takeIf { it >= place }?.let { " из $it" } ?: "") else null
            val time = result.resultLabel()
            val inline = placeLabel != null && measurer.measure(time, timeStyle).size.width +
                measurer.measure(placeLabel, placeStyle).size.width + with(density) { 24.dp.roundToPx() } <= constraints.maxWidth
            if (inline) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TimingValue("Результат", time, timeStyle, colors.action)
                TimingValue("Место", placeLabel!!, placeStyle, colors.textPrimary, Alignment.End)
            } else Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TimingValue("Результат", time, timeStyle, colors.action)
                placeLabel?.let { TimingValue("Место", it, placeStyle, colors.textPrimary) }
            }
        }
    }
}

@Composable
private fun TimingValue(label: String, value: String, style: androidx.compose.ui.text.TextStyle,
    color: androidx.compose.ui.graphics.Color, alignment: Alignment.Horizontal = Alignment.Start) {
    Column(horizontalAlignment = alignment, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, style = style, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = SauceTheme.colors.textMuted)
    }
}
