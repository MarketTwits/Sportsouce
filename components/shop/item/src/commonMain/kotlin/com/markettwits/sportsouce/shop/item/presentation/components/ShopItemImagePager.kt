package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.commerce.SauceProductImage
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.*

@Composable
internal fun ShopItemImagePager(modifier: Modifier, imageUrl: List<String>) {
    var selected by remember(imageUrl) { mutableStateOf(0) }
    var fullImage by remember { mutableStateOf(false) }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(onClick = { if (imageUrl.isNotEmpty()) fullImage = true }, shape = SauceShapes.card) {
            SauceProductImage(imageUrl.getOrNull(selected), Modifier.fillMaxWidth().aspectRatio(1f))
        }
        if (imageUrl.size > 1) Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            imageUrl.forEachIndexed { index, url ->
                Surface(onClick = { selected = index }, shape = SauceShapes.field,
                    border = BorderStroke(1.dp, if (index == selected) SauceTheme.colors.action else SauceTheme.colors.border)) {
                    SauceProductImage(url, Modifier.size(80.dp))
                }
            }
        }
    }
    if (fullImage && imageUrl.isNotEmpty()) FullImageScreen(image = imageUrl, selectedImageIndex = selected,
        onDismiss = { fullImage = false })
}
