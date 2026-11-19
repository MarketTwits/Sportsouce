package com.markettwits.sportsouce.start.presentation.album.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.*

@Composable
internal fun AlbumEmptyCard(modifier: Modifier = Modifier) {
    OnBackgroundCard(modifier.padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.small)) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(SauceSpacing.extraLarge),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.PhotoLibrary, null, Modifier.size(32.dp), tint = SauceTheme.colors.textMuted)
            Text("Альбом пуст", Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium,
                color = SauceTheme.colors.textPrimary, textAlign = TextAlign.Center)
            Text("В этом альбоме пока нет изображений", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium,
                color = SauceTheme.colors.textSecondary, textAlign = TextAlign.Center)
        }
    }
}
