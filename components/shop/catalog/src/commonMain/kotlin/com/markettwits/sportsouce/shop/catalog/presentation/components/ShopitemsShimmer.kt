package com.markettwits.sportsouce.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import kotlin.math.min

@Composable
fun ShopItemsShimmer(modifier: Modifier = Modifier) {
    val backgroundColor =
        if (LocalDarkOrLightTheme.current) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outlineVariant
    val screenWidth = rememberScreenSizeInfo().wDP
    val maxColumns = 5
    val columnWidth = 200.dp
    val columns = min(maxColumns, (screenWidth / columnWidth).toInt())
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(30) {
            ShopItemShimmer()
        }
    }
}