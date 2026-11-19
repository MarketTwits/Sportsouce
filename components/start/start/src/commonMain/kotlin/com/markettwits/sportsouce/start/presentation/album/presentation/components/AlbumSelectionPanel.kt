package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.screens.SauceDialog
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore

@Composable
internal fun AlbumSelectionPanel(count: Int, onClear: () -> Unit, onShare: () -> Unit, onDownload: () -> Unit, modifier: Modifier = Modifier) {
    Surface(modifier, shape = SauceShapes.sheet, color = SauceTheme.colors.surface, shadowElevation = 6.dp) {
        BoxWithConstraints {
            val compact = maxWidth < 360.dp || LocalDensity.current.fontScale > 1.2f
            Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onClear) { Icon(Icons.Outlined.Close, "Отменить выбор", tint = SauceTheme.colors.textPrimary) }
                Text(if (compact) "$count фото" else "Выбрано: $count", Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                FilledTonalIconButton(onClick = onDownload, modifier = Modifier.size(48.dp), shape = SauceShapes.button,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = SauceTheme.colors.actionSoft, contentColor = SauceTheme.colors.action)) {
                    Icon(Icons.Outlined.Download, "Скачать выбранные фото")
                }
                if (compact) FilledIconButton(onClick = onShare, modifier = Modifier.size(48.dp), shape = SauceShapes.button,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = SauceTheme.colors.action, contentColor = SauceTheme.colors.onAction)) {
                    Icon(Icons.Outlined.Share, "Поделиться выбранными фото")
                } else SauceButton("Поделиться", onShare, fillWidth = false)
            }
        }
    }
}

@Composable
internal fun AlbumExportDialog(state: StartAlbumStore.State, onEvent: (StartAlbumStore.Intent) -> Unit) {
    val progress = state.exportProgress
    val result = state.downloadResult
    if (progress == null && state.exportError == null && result == null) return
    val downloading = state.exportAction == StartAlbumStore.ExportAction.Download
    val dismiss = { onEvent(StartAlbumStore.Intent.CancelExport) }
    SauceDialog(onDismissRequest = dismiss) {
        Surface(Modifier.padding(24.dp).widthIn(max = 420.dp), shape = SauceShapes.card, color = SauceTheme.colors.surface) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (result != null) {
                    Icon(Icons.Outlined.CheckCircle, null, Modifier.size(48.dp).align(Alignment.CenterHorizontally), tint = SauceTheme.colors.action)
                    Text("Сохранено фото: ${result.count}", Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                    Text(result.destination, Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                    SauceButton("Готово", dismiss)
                } else {
                    val title = if (progress != null) {
                        if (downloading) "Скачивание оригиналов" else "Загрузка оригиналов"
                    } else if (downloading) "Не удалось сохранить фото" else "Не удалось подготовить фото"
                    Text(title, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                    if (progress != null) {
                        Text("${progress.completed} из ${progress.total} · ${(progress.bytes / 1_000_000.0 * 10).toInt() / 10.0} МБ",
                            style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                        LinearProgressIndicator(Modifier.fillMaxWidth(), color = SauceTheme.colors.action)
                        Text(if (downloading) "Сохраняем оригиналы без сжатия. Это может занять немного времени."
                            else "Отправим оригиналы без сжатия, когда все фото загрузятся.",
                            style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                    } else Text(state.exportError.orEmpty(), color = SauceTheme.colors.textSecondary, style = MaterialTheme.typography.bodyMedium)
                    if (progress == null) SauceButton("Повторить", { onEvent(StartAlbumStore.Intent.RetryExport) })
                    SauceButton("Отмена", dismiss, variant = SauceButtonVariant.Text)
                }
            }
        }
    }
}
