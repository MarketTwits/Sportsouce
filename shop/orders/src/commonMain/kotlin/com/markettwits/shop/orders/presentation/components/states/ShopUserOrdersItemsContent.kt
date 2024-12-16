package com.markettwits.shop.orders.presentation.components.states

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.shop.orders.domain.models.ShopUserOrder

@Composable
internal fun ShopUserOrdersItemsContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    isSuccess : Boolean,
    items : List<ShopUserOrder>
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(top = paddingValues.calculateTopPadding())
            .padding(10.dp)
    ) {
        if (isSuccess) {
            if (items.isNotEmpty()) {
                ShopUserOrdersWithItemsContent(items = items)
            } else {
                ShopUserOrdersWithoutItemsContent()
            }
        }
    }
}