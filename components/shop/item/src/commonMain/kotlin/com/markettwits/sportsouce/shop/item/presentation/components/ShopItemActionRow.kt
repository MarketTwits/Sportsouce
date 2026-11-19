package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.ShareFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.rememberSauceFloatingActionGlass
import com.markettwits.core_ui.items.theme.SauceTheme
import dev.chrisbanes.haze.HazeState

@Composable
internal fun ShopItemActionRow(
    modifier: Modifier = Modifier,
    galleryBackdrop: HazeState,
    galleryBottom: () -> Int,
    onClickGoBack: () -> Unit,
    onClickShare: () -> Unit,
) {
    // Product photography can be much lighter or darker than the surrounding theme.
    val glass = rememberSauceFloatingActionGlass(
        backdrop = galleryBackdrop,
        tint = SauceTheme.colors.surfaceSubtle.copy(alpha = .66f),
        backdropBottom = galleryBottom,
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackFloatingActionButton(glass = glass, back = onClickGoBack)
        ShareFloatingActionButton(glass = glass, onClick = onClickShare)
    }
}
