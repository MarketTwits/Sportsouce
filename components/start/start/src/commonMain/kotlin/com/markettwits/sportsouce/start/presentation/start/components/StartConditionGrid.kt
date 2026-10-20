package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.domain.StartItem.ConditionFile

@Composable
fun StartConditionGrid(modifier: Modifier = Modifier, conditionItems: List<StartItem.ConditionDetail>,
    conditionFile: ConditionFile = ConditionFile.Empty, onClickFile: (String) -> Unit = {}) {
    val regulation = conditionItems.filterIsInstance<StartItem.ConditionDetail.Regulation>().firstOrNull()
    val statement = conditionItems.filterIsInstance<StartItem.ConditionDetail.Statement>().firstOrNull()
    if (regulation == null && statement == null) return
    var selectedCondition by rememberSaveable(stateSaver = ConditionItemSaver) { mutableStateOf<ConditionItem?>(null) }
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val items: @Composable (Modifier) -> Unit = { itemModifier ->
            if (regulation != null) ConditionGridItem(itemModifier, "Регламент", "Правила проведения старта", Icons.Outlined.Description) {
                selectedCondition = ConditionItem("Регламент", regulation.value, null, ConditionType.REGULATION)
            }
            if (statement != null) ConditionGridItem(itemModifier, "Положение", "Условия участия", Icons.AutoMirrored.Outlined.Assignment) {
                selectedCondition = ConditionItem("Положение", statement.value,
                    conditionFile.takeIf { it is ConditionFile.Base }, ConditionType.STATEMENT)
            }
        }
        if (maxWidth >= 340.dp * LocalDensity.current.fontScale) {
            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) { items(Modifier.weight(1f).fillMaxHeight()) }
        } else Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) { items(Modifier.fillMaxWidth()) }
    }
    selectedCondition?.let { condition ->
        DefaultModalBottomSheet(title = condition.title, onDismissRequest = { selectedCondition = null }) {
            ConditionDetailContent(condition.htmlContent, condition.conditionFile,
                Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(SauceSpacing.large), onClickFile)
        }
    }
}

@Composable
private fun ConditionGridItem(modifier: Modifier, title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
    OnBackgroundCard(modifier, onClick = onClick) {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.medium), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                Icon(icon, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textSecondary)
                Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            }
            Text(description, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
        }
    }
}

@Composable
internal fun ConditionDetailContent(html: String, file: ConditionFile?, modifier: Modifier = Modifier, onClickFile: (String) -> Unit) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
        HtmlText(text = html, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyLarge,
            color = SauceTheme.colors.textPrimary, selectable = true)
        if (file is ConditionFile.Base) SauceActionRow("Положение", { onClickFile(file.url) },
            subtitle = "Открыть файл", icon = Icons.Outlined.Description)
    }
}

private data class ConditionItem(val title: String, val htmlContent: String, val conditionFile: ConditionFile?, val type: ConditionType)
private val ConditionItemSaver = Saver<ConditionItem?, List<String>>(
    save = { item -> item?.let { listOf(it.title, it.htmlContent, (it.conditionFile as? ConditionFile.Base)?.url.orEmpty(), it.type.name) } },
    restore = { data -> if (data.size < 4) null else ConditionItem(data[0], data[1],
        data[2].takeIf { it.isNotEmpty() }?.let { ConditionFile.Base(it) }, ConditionType.valueOf(data[3])) },
)
private enum class ConditionType { REGULATION, STATEMENT }
