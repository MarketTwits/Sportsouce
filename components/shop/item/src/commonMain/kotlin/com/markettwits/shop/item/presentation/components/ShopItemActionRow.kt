package com.markettwits.shop.item.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton

@Composable
internal fun ShopItemActionRow(
    modifier: Modifier = Modifier,
    onClickGoBack: () -> Unit,
    onClickAddToFavorite: () -> Unit,
    onClickShare: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackFloatingActionButton(back = onClickGoBack)
        Row {
            ShareActionButton(onClick = onClickShare)
        }
    }
}

@Composable
private fun ShareActionButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SmallFloatingActionButton(
        modifier = modifier.padding(10.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.tertiary,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Default.Share,
            contentDescription = "Share"
        )
    }
}
