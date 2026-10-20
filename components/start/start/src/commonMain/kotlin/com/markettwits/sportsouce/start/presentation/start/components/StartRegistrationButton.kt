package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartRegistrationButton(
    modifier: Modifier = Modifier,
    startStatus: StartItem.StartStatus,
    isLoading: Boolean = false,
    isPartialData: Boolean = false,
    hasError: Boolean = false,
    onClickRegistration: () -> Unit,
) {
    if (startStatus.code != 3) return
    val ready = !isLoading && !isPartialData && !hasError
    val waitingForData = (isLoading || isPartialData) && !hasError
    val colors = SauceTheme.colors
    ButtonContentBase(
        modifier = modifier.clip(SauceShapes.button)
            .shimmer(showShimmer = waitingForData, durationMillis = 1400)
            .semantics { if (waitingForData) stateDescription = "Загрузка данных старта" },
        isEnabled = ready,
        containerColor = colors.action,
        textColor = colors.onAction,
        onClick = { if (ready) onClickRegistration() },
        showContent = true,
        content = {
            Text(
                "Зарегистрироваться", Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
            )
        },
    )
}
