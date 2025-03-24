package com.markettwits.shop.catalog.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.paging.fold
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.domain.model.ShopItem


@Composable
fun ShopItemsContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<ShopItem>,
    onClickItem: (ShopItem) -> Unit
) {
    items.fold(onLoading = {
        ShopItemsShimmer(modifier = modifier)

    }, onException = {
        it.mapToSauceError().SauceErrorScreen(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            onClickRetry = items::refresh
        )
    }, onSuccess = { isRefreshing ->
        ShopItemsBase(
            modifier = modifier,
            isRefreshing = isRefreshing,
            items = items,
            onClickItem = onClickItem
        )
    }, onEmpty = {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "По вашему запросу не были найдены данные",
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 16.sp,
            )
        }
    }
    )
}
