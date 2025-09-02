package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.ShareFloatingActionButton

@Composable
internal fun ShopItemActionRow(
    modifier: Modifier = Modifier,
    onClickGoBack: () -> Unit,
    onClickShare: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackFloatingActionButton(back = onClickGoBack)
        ShareFloatingActionButton(onClick = onClickShare)
    }
}
