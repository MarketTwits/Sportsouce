package com.markettwits.sportsouce.inappnotification.impl.composable.type

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.inappnotification.api.model.UpdateFailureKind

@Composable
internal fun ComposableInAppNotificationUpdateStatus(isError: Boolean, onDismiss: () -> Unit, failureKind: UpdateFailureKind = UpdateFailureKind.General, percent: Int? = null, paused: Boolean = false, onOpen: () -> Unit = {}) {
    val colors = if (isError) SauceTheme.colors.danger else SauceTheme.colors.success
    UpdateNoticeFrame(colors, actionLabel = if (isError) "Скрыть сообщение" else "Открыть обновление", onClick = if (isError) onDismiss else onOpen) {
        Text(when {
            failureKind == UpdateFailureKind.UnsupportedDevice -> "Обновление недоступно для этого устройства"
            isError -> "Не удалось подготовить обновление"
            paused -> if (percent != null) "Загрузка приостановлена: $percent%" else "Загрузка приостановлена"
            percent != null -> "Загрузка обновления: $percent%"
            else -> "Загрузка обновления…"
        }, color = colors.content, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
    }
}
