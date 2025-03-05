package com.markettwits.shop.item.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_screen.PullToRefreshScreen
import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.item.domain.models.ShopExtraOptions

@Composable
internal fun ShopItemProductScreen(
    item: ShopItem,
    options: List<ShopExtraOptions>,
    isLoading: Boolean,
    onClickOption: (String) -> Unit,
    onClickGoBack: () -> Unit,
    onRefresh: () -> Unit,
    onClickAddToFavorite: () -> Unit,
    onClickShare: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    PullToRefreshScreen(
        isRefreshing = isLoading,
        onRefresh = onRefresh,
    ) {
        Scaffold(
            bottomBar = {
                content(Modifier.fillMaxWidth())
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                ShopItemImageContent(imageUrl = item.visual.imageUrl)
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ShopItemTitle(title = item.visual.displayName)
                    ShopItemPriceRow(price = item.price)
                    ShopItemExtraOptions(extraOption = options, onClickOption = onClickOption)
                    ShopItemDescriptionOrOptions(
                        description = item.visual.description,
                        options = item.options
                    )
                }

            }
            ShopItemActionRow(
                modifier = Modifier.fillMaxWidth(),
                onClickGoBack = onClickGoBack,
                onClickAddToFavorite = onClickAddToFavorite,
                onClickShare = onClickShare
            )
        }
    }
}