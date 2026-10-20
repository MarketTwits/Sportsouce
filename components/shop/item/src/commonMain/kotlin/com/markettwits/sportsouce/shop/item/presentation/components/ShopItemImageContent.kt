package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.commerce.SauceProductImage
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.*

@Composable
internal fun ShopItemImageContent(modifier: Modifier = Modifier, imageUrl: List<String>) {
    var fullImage by rememberSaveable { mutableStateOf(false) }
    val pager = rememberPagerState { imageUrl.size }
    Box(modifier.fillMaxWidth().height(360.dp)) {
        if (imageUrl.isEmpty()) SauceProductImage(null, Modifier.fillMaxSize())
        else {
            HorizontalPager(pager, Modifier.fillMaxSize()) { index ->
                SauceProductImage(imageUrl[index], Modifier.fillMaxSize().clickable(role = Role.Button,
                    onClickLabel = "Открыть фото товара") { fullImage = true })
            }
            if (imageUrl.size > 1) Surface(Modifier.align(Alignment.BottomStart).padding(16.dp),
                shape = SauceShapes.badge, color = SauceTheme.colors.surfaceRaised, contentColor = SauceTheme.colors.textSecondary) {
                Text("${pager.currentPage + 1} / ${imageUrl.size}", Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium)
            }
        }
    }
    if (fullImage && imageUrl.isNotEmpty()) FullImageScreen(image = imageUrl, selectedImageIndex = pager.currentPage,
        onDismiss = { fullImage = false })
}
