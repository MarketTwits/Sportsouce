package com.markettwits.sportsouce.start.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import com.markettwits.core_ui.items.components.progress.shimmer
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard

/** Shared by the participant and result toolbars, keeping their callbacks in the owning screen. */
@Composable
internal fun StartListSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    filterCount: Int = 0,
    onFilter: (() -> Unit)? = null,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(modifier = Modifier.size(48.dp), onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = SauceTheme.colors.textPrimary)
        }
        BoundlessTextFieldBase(
            modifier = Modifier.weight(1f).semantics { contentDescription = "Поиск участника" },
            value = query,
            onValueChange = onQueryChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
            placeholder = { Text("Поиск", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium,
                color = SauceTheme.colors.textMuted) },
            trailingIcon = if (query.isNotEmpty()) ({
                IconButton(modifier = Modifier.size(48.dp), onClick = onClear) { Icon(Icons.Default.Close, "Очистить поиск", tint = SauceTheme.colors.textSecondary) }
            }) else null,
        )
        if (onFilter != null) {
            BadgedBox(badge = {
                if (filterCount > 0) Badge(containerColor = SauceTheme.colors.action, contentColor = SauceTheme.colors.onAction) {
                    Text(filterCount.toString(), style = MaterialTheme.typography.labelSmall)
                }
            }) {
                IconButton(modifier = Modifier.size(48.dp), onClick = onFilter) {
                    Icon(Icons.Default.FilterList, "Фильтры", tint = SauceTheme.colors.textPrimary)
                }
            }
        }
    }
}

@Composable
internal fun StartListField(label: String, value: String, modifier: Modifier = Modifier, highlighted: Boolean = false) {
    if (value.isBlank()) return
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
        Text(value, Modifier.fillMaxWidth(), style = if (highlighted) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (highlighted) SauceTheme.colors.action else SauceTheme.colors.textPrimary)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun StartListFacts(facts: List<Pair<String, String>>, highlightedLabel: String? = null, modifier: Modifier = Modifier) {
    FlowRow(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        facts.filter { it.second.isNotBlank() }.forEach { (label, value) ->
            StartListField(label, value, Modifier.width(IntrinsicSize.Max).widthIn(min = 48.dp), highlighted = label == highlightedLabel)
        }
    }
}

@Composable
internal fun StartListMetadata(label: String, value: String) {
    if (value.isNotBlank()) Text("$label: $value", Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
}

@Composable
internal fun StartListFilterSection(title: String, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
        content()
    }
}

@Composable
internal fun StartListRowShimmer(modifier: Modifier = Modifier, result: Boolean = false) {
    com.markettwits.core_ui.items.components.cards.OnBackgroundCard(modifier = modifier) {
        Column(Modifier.fillMaxWidth().padding(12.dp).shimmer(),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.fillMaxWidth(0.7f).height(20.dp).background(SauceTheme.colors.surfaceSubtle, com.markettwits.core_ui.items.theme.SauceShapes.badge))
            Box(Modifier.fillMaxWidth(0.5f).height(16.dp).background(SauceTheme.colors.surfaceSubtle, com.markettwits.core_ui.items.theme.SauceShapes.badge))
            if (result) Box(Modifier.fillMaxWidth().height(36.dp).background(SauceTheme.colors.surfaceSubtle, com.markettwits.core_ui.items.theme.SauceShapes.badge))
        }
    }
}

/** A raised list surface, sharing the same treatment in the list and its detail sheet. */
@Composable
internal fun StartListCard(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    OnBackgroundCard(modifier.shadow(2.dp, SauceShapes.card), onClick = onClick) { content() }
}

/** Passive distance / bib label; never styled as an interactive filter. */
@Composable
internal fun StartListTag(value: String) {
    if (value.isBlank()) return
    val style = MaterialTheme.typography.labelMedium
    val measured = rememberTextMeasurer().measure(value, style = style)
    val textWidth = with(LocalDensity.current) { measured.size.width.toDp() } + 1.dp
    Surface(color = SauceTheme.colors.surfaceSubtle, contentColor = SauceTheme.colors.textSecondary, shape = SauceShapes.badge) {
        Text(value, Modifier.padding(horizontal = 8.dp, vertical = 4.dp).width(textWidth), style = style)
    }
}

/** Secondary facts share a line when space allows, and wrap naturally with large text. */
@Composable
internal fun StartListInlineMetadata(value: String, modifier: Modifier = Modifier) {
    if (value.isBlank()) return
    val style = MaterialTheme.typography.bodySmall
    val measured = rememberTextMeasurer().measure(value, style = style)
    val width = with(LocalDensity.current) { measured.size.width.toDp() } + 1.dp
    Text(value, modifier.width(width), style = style, color = SauceTheme.colors.textSecondary)
}

/** Timing is the focal point; placement and gap remain distinct secondary facts. */
@Composable
internal fun StartResultSummary(place: String, time: String, shift: String, modifier: Modifier = Modifier, compact: Boolean = false) {
    Surface(modifier.fillMaxWidth(), color = SauceTheme.colors.surfaceSubtle, shape = SauceShapes.field) {
        BoxWithConstraints(Modifier.padding(if (compact) 8.dp else 12.dp)) {
            val fontScale = LocalDensity.current.fontScale
            val horizontal = maxWidth >= 220.dp * fontScale
            val placeWidth = (if (compact) 48.dp else 64.dp) * fontScale
            val columnSpacing = if (compact) 12.dp else 16.dp
            val valueSpacing = if (compact) 0.dp else 4.dp
            val timingWidth = if (horizontal) maxWidth - placeWidth - columnSpacing * 2 - 1.dp else maxWidth
            val timingWidthPx = with(LocalDensity.current) { timingWidth.roundToPx() }
            val measurer = rememberTextMeasurer()
            val metricStyles = (if (compact) listOf(MaterialTheme.typography.titleMedium, MaterialTheme.typography.titleSmall)
                else listOf(MaterialTheme.typography.titleLarge, MaterialTheme.typography.titleMedium, MaterialTheme.typography.titleSmall))
                .map { it.copy(fontFeatureSettings = "tnum") }
            val metricStyle = metricStyles.firstOrNull { measurer.measure(time, style = it).size.width <= timingWidthPx } ?: metricStyles.last()
            val placeAlignment = if (horizontal) TextAlign.Center else TextAlign.Start
            val placement: @Composable () -> Unit = {
                Column(if (horizontal) Modifier.width(placeWidth) else Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(valueSpacing)) {
                    Text("Место", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted, textAlign = placeAlignment)
                    Text(place, Modifier.fillMaxWidth(), style = metricStyle, color = SauceTheme.colors.textPrimary, textAlign = placeAlignment)
                }
            }
            val timing: @Composable () -> Unit = {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(valueSpacing)) {
                    if (time.isNotBlank()) {
                        Text("Результат", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                        Text(time, Modifier.fillMaxWidth(), style = metricStyle, color = SauceTheme.colors.action)
                    }
                    if (shift.isNotBlank()) Text("Отставание $shift", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                }
            }
            if (horizontal) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(columnSpacing)) {
                    placement()
                    Box(Modifier.width(1.dp).height(if (compact) 44.dp else 56.dp).background(SauceTheme.colors.border))
                    Box(Modifier.weight(1f)) { timing() }
                }
            } else {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) { placement(); timing() }
            }
        }
    }
}
