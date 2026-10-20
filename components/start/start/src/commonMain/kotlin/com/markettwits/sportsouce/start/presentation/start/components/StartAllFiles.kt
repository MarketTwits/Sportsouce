package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Link
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.divider.BaseDivider
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartAllFiles(modifier: Modifier = Modifier, results: List<StartItem.Result> = emptyList(),
    usefulLinks: List<StartItem.Result> = emptyList(), conditionFile: StartItem.ConditionFile? = null,
    onClickFile: (String) -> Unit) {
    val files = results.map { FileItem(it.name, it.url, "Результаты") } +
        usefulLinks.map { FileItem(it.name, it.url, "Полезная ссылка", link = true) } +
        listOfNotNull((conditionFile as? StartItem.ConditionFile.Base)?.let { FileItem("Положение", it.url, "Положение") })
    if (files.isEmpty()) return
    var expanded by rememberSaveable(files) { mutableStateOf(false) }
    StartContentBasePanel(modifier, label = "Файлы и ссылки") {
        Column(Modifier.fillMaxWidth()) {
            (if (expanded) files else files.take(3)).forEachIndexed { index, file ->
                if (index > 0) BaseDivider()
                SauceActionRow(title = file.name, subtitle = file.type,
                    icon = if (file.link) Icons.Outlined.Link else Icons.Outlined.Description,
                    onClick = { onClickFile(file.url) })
            }
            if (files.size > 3) SauceButton(if (expanded) "Свернуть" else "Ещё ${files.size - 3}",
                onClick = { expanded = !expanded }, variant = SauceButtonVariant.Text)
        }
    }
}

private data class FileItem(val name: String, val url: String, val type: String, val link: Boolean = false)
