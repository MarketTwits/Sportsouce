package com.markettwits.sportsouce.shop.filter.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.sportsouce.shop.filter.presentation.components.filter.ShopFilterContent
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore

@Composable
fun ShopFilterScreen(
    modifier: Modifier = Modifier,
    component: ShopFilterComponent,
    onClickApplyFilter : () -> Unit
) {
    val state by component.state.collectAsState()

    ShopFilterContent(
        modifier = modifier,
        state = state,
        onClickGoBack = {
            component.obtainEvent(ShopFilterStore.Intent.OnClickGoBack)
        },
        onClickReset = {
            component.obtainEvent(ShopFilterStore.Intent.OnClickResetFilter)
        },
        onMinPriceChange = {
            component.obtainEvent(ShopFilterStore.Intent.OnUpdatePrice(true, it))
        },
        onMaxPriceChange = {
            component.obtainEvent(ShopFilterStore.Intent.OnUpdatePrice(false, it))
        },
        onClickCategory = {
            component.obtainEvent(ShopFilterStore.Intent.OnClickCategory(it))
        },
        onOptionClick = {
            component.obtainEvent(ShopFilterStore.Intent.OnClickOption(it))
        },
        onClickApplyFilter = {
            component.obtainEvent(ShopFilterStore.Intent.OnClickApplyFilter)
            onClickApplyFilter()
        },
        onClickRetry = {
            component.obtainEvent(ShopFilterStore.Intent.OnClickRetry)
        }
    )
}