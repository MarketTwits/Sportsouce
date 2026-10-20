package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun FailedScreen(
    modifier: Modifier = Modifier.padding(5.dp),
    title: String = "Не удалось загрузить данные",
    message: String = "Произошла ошибка при загрузке данных",
    onClickBack: (() -> Unit?)? = null,
    onClickRetry: () -> Unit,
) {
    val colors = SauceTheme.colors
    Box(modifier.fillMaxSize()) {
        Column(
            Modifier.align(Alignment.Center).widthIn(max = 480.dp).fillMaxWidth()
                .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp,
                    vertical = if (onClickBack != null) 64.dp else 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(Modifier.size(64.dp).background(colors.surfaceSubtle, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.CloudOff, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(28.dp))
            }
            Text(title, color = colors.textPrimary, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text(message, color = colors.textSecondary, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            SauceButton("Повторить", onClickRetry, variant = SauceButtonVariant.Outlined, fillWidth = false)
        }
        if (onClickBack != null) BackFloatingActionButton { onClickBack() }
    }
}
