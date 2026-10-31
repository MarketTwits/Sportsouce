package com.markettwits.sportsouce.inappnotification.impl.composable.type

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification

/** A single action above navigation. Its parent owns visibility and animation. */
@Composable
internal fun ComposableInAppNotificationSelfUpdateReady(
    notification: InAppNotification.SelfUpdateReady,
    onClickAction: () -> Unit,
    onDismiss: () -> Unit,
) {
    val action = when {
        notification.informationalOnly -> "Доступно обновление сайта"
        notification.desktopDiskImage -> "Открыть образ установки"
        notification.desktopInstaller -> "Открыть установщик"
        notification.readyToInstall -> "Установить обновление"
        else -> "Обновить Sportsauce"
    }
    val colors = SauceTheme.colors.success
    UpdateNoticeFrame(colors, actionLabel = action, onClick = onClickAction) {
        Canvas(Modifier.size(16.dp)) {
            val stroke = 2.dp.toPx()
            val x = size.width / 2
            val bottom = size.height * 0.65f
            drawLine(colors.content, Offset(x, 0f), Offset(x, bottom), stroke, StrokeCap.Round)
            drawLine(colors.content, Offset(size.width * 0.25f, size.height * 0.4f), Offset(x, bottom), stroke, StrokeCap.Round)
            drawLine(colors.content, Offset(size.width * 0.75f, size.height * 0.4f), Offset(x, bottom), stroke, StrokeCap.Round)
            drawLine(colors.content, Offset(size.width * 0.2f, size.height * 0.9f), Offset(size.width * 0.8f, size.height * 0.9f), stroke, StrokeCap.Round)
        }
        Text(action, color = colors.content, style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = 560.dp).weight(1f, fill = false))
    }
}
