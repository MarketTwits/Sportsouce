package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.domain.StartRefundRules

@Composable
internal fun StartRefundRulesPanel(
    modifier: Modifier = Modifier,
    data: StartRefundRules?,
    loading: Boolean,
    failed: Boolean,
    onRetry: () -> Unit,
) {
    if (data == null && !loading && !failed) return
    val colors = SauceTheme.colors
    StartContentBasePanel(modifier, "Правила возврата слота") {
        if (data != null) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (data.startDate.isNotBlank()) RefundMetadata("Дата старта", data.startDate)
                when (data.source) {
                    StartRefundRules.Source.Global -> RefundMetadata("Источник правил", "Глобальные правила")
                    StartRefundRules.Source.Start -> RefundMetadata("Источник правил", "Правила старта")
                    StartRefundRules.Source.Unknown -> Unit
                }
            }
            if (data.rules.isEmpty()) {
                Text("Правила возврата для этого старта не указаны.", style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary)
            } else {
                HorizontalDivider(color = colors.border)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    data.rules.forEach { rule -> RefundRuleRow(rule) }
                }
            }
        }
        if (loading) {
            if (data == null) Column(
                Modifier.fillMaxWidth().semantics {
                    contentDescription = "Загрузка правил возврата"
                    progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
                }, verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SauceSkeleton(Modifier.fillMaxWidth(.6f).height(36.dp))
                repeat(3) { SauceSkeleton(Modifier.fillMaxWidth().height(48.dp), SauceShapes.field) }
            } else Text("Обновляем правила…", style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
        }
        if (failed) {
            Text(if (data == null) "Не удалось загрузить правила возврата." else "Не удалось обновить правила. Показана последняя загруженная версия.",
                style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
            SauceButton("Повторить", onRetry, variant = SauceButtonVariant.Outlined, fillWidth = false)
        }
    }
}

@Composable
private fun RefundMetadata(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
    }
}

@Composable
private fun RefundRuleRow(rule: StartRefundRules.Rule) {
    val colors = SauceTheme.colors
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = SauceShapes.badge,
            color = if (rule.refundPercent > 0) colors.success.container else colors.neutral.container,
            contentColor = if (rule.refundPercent > 0) colors.success.content else colors.neutral.content) {
            Box(Modifier.widthIn(min = 64.dp).padding(horizontal = 10.dp, vertical = 8.dp), contentAlignment = Alignment.Center) {
                Text("${rule.refundPercent}%", style = MaterialTheme.typography.titleSmall)
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val days = rule.daysBeforeStart
            val unit = when {
                days % 100 in 11..14 -> "дней"
                days % 10 == 1 -> "день"
                days % 10 in 2..4 -> "дня"
                else -> "дней"
            }
            Text("Не позднее чем за $days $unit", style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary)
            if (rule.description.isNotBlank()) Text(rule.description,
                style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
        }
    }
}
