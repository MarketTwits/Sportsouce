package com.markettwits.sportsouce.profile.results.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalDensity
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.selection.SauceSegmentedTabs
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.results.domain.*
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore.*
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore.State
import kotlin.math.abs

@Composable
internal fun ResultsProgressCard(state: State, event: (Intent) -> Unit) {
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionTitle("Динамика результатов")
            SauceSegmentedTabs(labels = listOf("График", "Таблица"), selectedIndex = if (state.table) 1 else 0,
                onSelected = { event(Intent.Table(it == 1)) })
            ProgressFilters(state, event)
            when {
                state.progressLoading -> SauceSkeleton(Modifier.fillMaxWidth().height(190.dp))
                state.progressError != null -> ResultsError(state.progressError) { event(Intent.RetryProgress) }
                state.progress != null -> {
                    val points = state.progress.points.filter { it.value?.let { value -> value.isFinite() && value >= 0 } == true }.sortedBy { it.date }
                    if (points.isEmpty()) Text("Пока нет результатов на выбранной дистанции.", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                    else {
                        if (state.table) ProgressTable(points, state.progress.mode == "pace", state.comparison)
                        else ProgressChart(points, state.progress.mode == "pace", state.comparison)
                        if (points.size == 1) Text("Пока есть одно участие. Динамика появится после следующего результата.", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                    }
                }
            }
        }
    }
}

private data class ProgressChoice(val key: String?, val label: String)

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ProgressFilters(state: State, event: (Intent) -> Unit) {
    var picker by rememberSaveable { mutableStateOf<String?>(null) }
    val allowed = state.series.find { it.key == state.seriesKey }?.categories
    val distances = state.summary?.categories.orEmpty().filter { allowed == null || it.key in allowed }
        .map { ProgressChoice(it.key, it.label) } + ProgressChoice(null, "Все дистанции (темп)")
    val distance = distances.find { it.key == state.category }?.label ?: "Все дистанции (темп)"
    val series = listOf(ProgressChoice(null, "Все старты")) + state.series.map { ProgressChoice(it.key, it.name) }
    val seriesTitle = series.find { it.key == state.seriesKey }?.label ?: "Все старты"
    val compareTitle = when (state.comparison) {
        ResultComparison.Self -> "Мои результаты"
        ResultComparison.Group -> "С группой"
        ResultComparison.Age -> "С моим возрастом"
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (state.series.isNotEmpty()) SauceButton(seriesTitle, { picker = "series" },
            Modifier.semantics { contentDescription = "Старт"; stateDescription = seriesTitle },
            variant = SauceButtonVariant.Outlined, fillWidth = false)
        SauceButton(if (state.category == null) "Все дистанции" else distance, { picker = "distance" },
            Modifier.semantics { contentDescription = "Дистанция"; stateDescription = distance },
            variant = SauceButtonVariant.Outlined, fillWidth = false)
        SauceButton(compareTitle, { picker = "compare" },
            Modifier.semantics { contentDescription = "Сравнить"; stateDescription = state.comparison.label },
            variant = if (state.comparison == ResultComparison.Self) SauceButtonVariant.Outlined else SauceButtonVariant.Soft,
            fillWidth = false)
    }
    picker?.let { current ->
        val title = when (current) { "series" -> "Старт"; "distance" -> "Дистанция"; else -> "Сравнить" }
        val choices = when (current) {
            "series" -> series
            "distance" -> distances
            else -> ResultComparison.entries.map { ProgressChoice(it.apiValue, it.label) }
        }
        val selected = when (current) { "series" -> state.seriesKey; "distance" -> state.category; else -> state.comparison.apiValue }
        DefaultModalBottomSheet(title = title, onDismissRequest = { picker = null }) {
            SauceLazyVerticalGrid(GridCells.Adaptive(280.dp), Modifier.fillMaxWidth().heightIn(max = 440.dp).selectableGroup(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(choices, key = { if (it.key == null) "all" else "value-${it.key}" }) { choice ->
                    SauceRadioRow(choice.label, selected == choice.key, onClick = {
                        when (current) {
                            "series" -> event(Intent.Series(choice.key))
                            "distance" -> event(Intent.Category(choice.key))
                            else -> ResultComparison.entries.find { it.apiValue == choice.key }?.let { event(Intent.Compare(it)) }
                        }
                        picker = null
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProgressChart(points: List<ProgressPoint>, pace: Boolean, comparison: ResultComparison) {
    var selected by remember(points) { mutableIntStateOf(points.lastIndex) }
    val colors = SauceTheme.colors
    val labelStyle = MaterialTheme.typography.labelSmall.copy(color = colors.textMuted)
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val hasComparison = points.any { it.compare?.value?.let { value -> value.isFinite() && value >= 0 } == true }
    val values = points.flatMap { listOfNotNull(it.value, it.compare?.value?.takeIf { value -> value.isFinite() && value >= 0 }) }
    val low = values.minOrNull() ?: 0.0
    val high = values.maxOrNull() ?: 0.0
    val padding = maxOf((high - low) * .12, 5.0)
    val min = (low - padding).coerceAtLeast(0.0)
    val max = high + padding
    val ticks = (0..2).map { min + (max - min) * it / 2 }
    val labels = ticks.map { textMeasurer.measure(resultTime(it), labelStyle) }
    val gutter = labels.maxOf { it.size.width }.toFloat() + with(density) { 12.dp.toPx() }
    val labelHeight = labels.maxOf { it.size.height }.toFloat()
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (points.size > 1) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ChartLegend("Мой результат", colors.action)
                if (hasComparison) ChartLegend(comparisonTitle(comparison), colors.warning.content)
            }
            Canvas(Modifier.fillMaxWidth().height(190.dp).semantics {
                contentDescription = "График результатов, ${points.size} точек. Точные значения доступны во вкладке Таблица."
            }.pointerInput(points, gutter) {
                detectTapGestures { offset ->
                    val width = (size.width - gutter - 8.dp.toPx()).coerceAtLeast(1f)
                    selected = points.indices.minBy { abs(it.toFloat() / points.lastIndex - ((offset.x - gutter) / width)) }
                }
            }) {
                val left = gutter; val right = size.width - 8.dp.toPx()
                val top = labelHeight / 2 + 4.dp.toPx()
                val bottom = size.height - labelHeight - 14.dp.toPx()
                fun x(index: Int) = left + (right - left) * index.toFloat() / points.lastIndex
                fun y(value: Double) = top + ((value - min) / (max - min)).toFloat() * (bottom - top)
                ticks.forEachIndexed { index, value ->
                    val yy = y(value)
                    drawLine(colors.border, Offset(left, yy), Offset(right, yy), 1.dp.toPx())
                    drawText(labels[index], topLeft = Offset(0f, yy - labels[index].size.height / 2))
                }
                val mine = Path()
                points.forEachIndexed { index, point ->
                    if (index == 0) mine.moveTo(x(index), y(point.value!!)) else mine.lineTo(x(index), y(point.value!!))
                }
                val area = Path().apply {
                    addPath(mine)
                    lineTo(x(points.lastIndex), bottom)
                    lineTo(x(0), bottom)
                    close()
                }
                drawPath(area, Brush.verticalGradient(listOf(colors.action.copy(alpha = .13f), colors.action.copy(alpha = .015f)), startY = top, endY = bottom))
                drawPath(mine, colors.action, style = Stroke(2.5.dp.toPx()))
                val other = Path(); var connected = false
                points.forEachIndexed { index, point ->
                    val value = point.compare?.value?.takeIf { it.isFinite() && it >= 0 }
                    if (value == null) connected = false else {
                        if (connected) other.lineTo(x(index), y(value)) else other.moveTo(x(index), y(value))
                        connected = true
                        drawCircle(colors.warning.content, 2.5.dp.toPx(), Offset(x(index), y(value)))
                    }
                }
                drawPath(other, colors.warning.content, style = Stroke(1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 5.dp.toPx()))))
                points.forEachIndexed { index, point ->
                    if (index == selected) drawCircle(colors.action.copy(alpha = .14f), 9.dp.toPx(), Offset(x(index), y(point.value!!)))
                    drawCircle(colors.action, (if (index == selected) 4 else 3).dp.toPx(), Offset(x(index), y(point.value!!)))
                }
                val first = textMeasurer.measure(points.first().date?.take(10).orEmpty(), labelStyle)
                val last = textMeasurer.measure(points.last().date?.take(10).orEmpty(), labelStyle)
                val indices = if (first.size.width + last.size.width + 16.dp.toPx() <= right - left) listOf(0, points.lastIndex) else listOf(selected)
                indices.forEach { index ->
                    val measured = textMeasurer.measure(points[index].date?.take(10).orEmpty(), labelStyle)
                    val xx = (x(index) - measured.size.width / 2).coerceIn(0f, (size.width - measured.size.width).coerceAtLeast(0f))
                    drawText(measured, topLeft = Offset(xx, bottom + 10.dp.toPx()))
                }
            }
        }
        val point = points[selected.coerceIn(points.indices)]
        Surface(color = colors.surfaceSubtle, shape = SauceShapes.field) {
            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(resultDate(point.date), style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                    Text(resultTime(point.value) + if (pace) " /км" else "", style = MaterialTheme.typography.titleSmall.copy(fontFeatureSettings = "tnum"), color = colors.action)
                }
                Text(point.startName, style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary, maxLines = 2, overflow = TextOverflow.Ellipsis)
                point.compare?.let { Text("${it.label}: ${resultTime(it.value)}${if (pace) " /км" else ""} · участников: ${it.sample}",
                    style = MaterialTheme.typography.bodySmall, color = colors.textSecondary) }
            }
        }
        if (points.size > 1) Text(if (pace) "Темп на километр. Чем выше точка, тем быстрее результат." else "Чем выше точка, тем быстрее результат.",
            style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
        if (comparison != ResultComparison.Self && !hasComparison) Text("Данных для сравнения пока нет.",
            style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
    }
}

@Composable
private fun ChartLegend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(6.dp).background(color, CircleShape))
        Text(label, style = MaterialTheme.typography.labelSmall, color = SauceTheme.colors.textSecondary)
    }
}

@Composable
private fun ProgressTable(points: List<ProgressPoint>, pace: Boolean, comparison: ResultComparison) {
    Column(Modifier.horizontalScroll(rememberScrollState()).widthIn(min = 600.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Дата и старт" to 220, "Результат" to 100, "Темп /км" to 90, "Место" to 60, comparisonTitle(comparison) to 180).forEach { (text, width) ->
                Text(text, Modifier.width(width.dp), style = MaterialTheme.typography.labelMedium, color = SauceTheme.colors.textSecondary)
            }
        }
        points.forEach { point ->
            HorizontalDivider(color = SauceTheme.colors.border)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.width(220.dp)) {
                    Text(resultDate(point.date), style = MaterialTheme.typography.labelMedium)
                    Text(point.startName, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                }
                Text(resultTime(point.resultSeconds), Modifier.width(100.dp), style = MaterialTheme.typography.bodyMedium)
                Text(resultTime(point.pace), Modifier.width(90.dp), style = MaterialTheme.typography.bodyMedium)
                Text(point.place?.toString() ?: "—", Modifier.width(60.dp), style = MaterialTheme.typography.bodyMedium)
                Text(point.compare?.let { resultTime(it.value) + if (pace) " /км" else "" } ?: "—", Modifier.width(180.dp), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private fun comparisonTitle(comparison: ResultComparison) = when (comparison) {
    ResultComparison.Self -> "Сравнение"
    ResultComparison.Group -> "Медиана возрастной группы"
    ResultComparison.Age -> "Медиана участников вашего возраста"
}
