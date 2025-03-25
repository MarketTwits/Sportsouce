package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ShopItemTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        color = MaterialTheme.colorScheme.tertiary,
        textAlign = TextAlign.Start,
        fontSize = 16.sp,
        fontFamily = FontNunito.bold(),
    )
}