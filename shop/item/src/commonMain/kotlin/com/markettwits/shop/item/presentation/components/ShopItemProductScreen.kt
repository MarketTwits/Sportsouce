package com.markettwits.shop.item.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_screen.PullToRefreshScreen
import com.markettwits.shop.item.domain.models.ShopPageItem

@Composable
internal fun ShopItemProductScreen(
    item: ShopPageItem,
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
            modifier = Modifier.padding(16.dp),
            bottomBar = {
                content(Modifier.fillMaxWidth())
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
                    ShopItemImageContent(imageUrl = item.visual.imageUrl)
                    ShopItemTitle(title = item.visual.displayName)
                    ShopItemPriceRow(price = item.price)
                    ShopItemExtraOptions(extraOption = item.extraOptions, onClickOption = onClickOption)
                    ShopItemDescriptionOrOptions(
                        description = item.visual.description,
                        options = item.options
                    )
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
}