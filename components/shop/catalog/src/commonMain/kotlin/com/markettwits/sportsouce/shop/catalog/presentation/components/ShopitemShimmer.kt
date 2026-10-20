package com.markettwits.sportsouce.shop.catalog.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.theme.SauceShapes

@Composable
fun ShopItemShimmer(modifier: Modifier = Modifier) {
    OnBackgroundCard(modifier, shape = SauceShapes.field) {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SauceSkeleton(Modifier.fillMaxWidth().aspectRatio(0.9f))
            SauceSkeleton(Modifier.fillMaxWidth().height(18.dp))
            SauceSkeleton(Modifier.fillMaxWidth(0.7f).height(18.dp))
            SauceSkeleton(Modifier.width(80.dp).height(24.dp))
        }
    }
}
