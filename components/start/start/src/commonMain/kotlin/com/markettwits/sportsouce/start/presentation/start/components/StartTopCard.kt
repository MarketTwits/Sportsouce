package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard

@Composable
internal fun StartTopCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    OnBackgroundCard(modifier.fillMaxWidth()) { content() }
}
