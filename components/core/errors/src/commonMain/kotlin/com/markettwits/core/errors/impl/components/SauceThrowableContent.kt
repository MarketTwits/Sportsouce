package com.markettwits.core.errors.impl.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core.errors.impl.supcomponents.mapSauceErrorImage
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun SauceThrowableContent(
    modifier: Modifier = Modifier,
    sauceError: SauceError,
    onClickRetry: (() -> Unit)? = null,
) {

    val uriHandler = LocalUriHandler.current
    val colors = SauceTheme.colors

    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = sauceError.mapSauceErrorImage(),
            contentDescription = null,
            tint = colors.textMuted,
        )
        Text(
            text = when (sauceError) {
                is SauceError.Empty, is SauceError.NotFound -> "Данные не найдены"
                is SauceError.WrongRequest -> "Не удалось выполнить запрос"
                else -> "Не удалось загрузить данные"
            },
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = sauceError.mapToString(),
            textAlign = TextAlign.Center,
            color = colors.textSecondary,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (onClickRetry != null) {
            SauceButton(
                title = "Повторить",
                onClick = onClickRetry,
                modifier = Modifier.widthIn(max = 280.dp),
                fillWidth = false,
            )
        }
        SauceButton(
            title = "Сообщить об ошибке",
            onClick = { uriHandler.openUri("https://t.me/sportsoyuznsk") },
            variant = SauceButtonVariant.Text,
            fillWidth = false,
        )
    }
}

