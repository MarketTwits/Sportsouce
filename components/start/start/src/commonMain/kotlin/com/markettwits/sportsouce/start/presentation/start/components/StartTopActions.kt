package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.rememberSauceFloatingActionGlass
import com.markettwits.core_ui.items.components.buttons.sauceFloatingActionTopPadding
import com.markettwits.sportsouce.start.presentation.start.component.StartFavoriteState
import dev.chrisbanes.haze.HazeState

@Composable
internal fun StartTopActions(
    modifier: Modifier = Modifier,
    backdrop: HazeState,
    coverBottom: () -> Int,
    favoriteState: StartFavoriteState,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
) {
    val glass = rememberSauceFloatingActionGlass(backdrop, backdropBottom = coverBottom)
    Row(
        modifier = modifier.fillMaxWidth().padding(top = sauceFloatingActionTopPadding())
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SauceFloatingActionButton(glass = glass, onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.weight(1f))
        StartShareActionButton(glass = glass, onClick = onShare)
        StartFavoriteActionButton(
            glass = glass,
            isFavorite = favoriteState.isFavorite,
            isLoading = favoriteState.isLoading(),
            onClick = onFavorite,
        )
    }
}
