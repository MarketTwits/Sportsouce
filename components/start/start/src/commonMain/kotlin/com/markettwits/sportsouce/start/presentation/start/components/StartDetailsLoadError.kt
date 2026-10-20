package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme

/** Loaded details, initial loading and initial failure are mutually exclusive. */
@Composable
internal fun StartDetailsBody(
    hasData: Boolean,
    isLoading: Boolean,
    error: Throwable?,
    onClickRetry: () -> Unit,
    onClickBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(Modifier.fillMaxSize().background(SauceTheme.colors.background)) {
        when {
            hasData -> content()
            isLoading || error == null -> LoadingFullScreen(onClickBack = onClickBack)
            else -> Box(Modifier.fillMaxSize()) {
                BoxWithConstraints(Modifier.fillMaxSize().statusBarsPadding().padding(top = 64.dp).navigationBarsPadding()) {
                    Column(
                        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                            .heightIn(min = maxHeight).padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        StartDetailsLoadError(Modifier.widthIn(max = 480.dp), onClickRetry = onClickRetry)
                    }
                }
                BackFloatingActionButton(back = onClickBack)
            }
        }
    }
}

/** Uses the same card, typography and actions as the loaded details, including at 200% font. */
@Composable
internal fun StartDetailsLoadError(
    modifier: Modifier = Modifier,
    isPartialData: Boolean = false,
    hasData: Boolean = false,
    onClickRetry: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    OnBackgroundCard(modifier) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Outlined.CloudOff, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textMuted)
                Text(
                    if (hasData) "Не удалось обновить данные" else "Не удалось загрузить старт",
                    Modifier.weight(1f).semantics { heading() },
                    style = MaterialTheme.typography.titleMedium,
                    color = SauceTheme.colors.textPrimary,
                )
            }
            Text(
                when {
                    !hasData -> "Проверьте подключение к интернету и попробуйте ещё раз. Если ошибка повторяется, сообщите нам."
                    isPartialData -> "Показана краткая информация. Для загрузки остальных сведений попробуйте ещё раз."
                    else -> "Показана ранее загруженная информация. Попробуйте обновить страницу ещё раз."
                },
                Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium,
                color = SauceTheme.colors.textSecondary,
            )
            SauceButton("Повторить", onClickRetry)
            SauceButton("Сообщить об ошибке", { uriHandler.openUri("https://t.me/sportsoyuznsk") },
                variant = SauceButtonVariant.Text)
        }
    }
}
