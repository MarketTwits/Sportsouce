package com.markettwits.sportsouce.shop.filter.presentation.components.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.sportsouce.shop.filter.presentation.components.filter.ShopFilterSelectedParams
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore

@Composable
fun SelectedFilterParams(
    modifier: Modifier = Modifier,
    component: ShopFilterComponent
) {
    val state by component.state.collectAsState()

    ShopFilterSelectedParams(
        modifier = modifier,
        selectedCategoriesPath = state.selectedCategoryPath,
        selectedOptions = state.selectedOptionUID,
        onClickCategory = {
            component.obtainEvent(ShopFilterStore.Intent.OnClickResetCategory)
            component.obtainEvent(ShopFilterStore.Intent.OnClickApplyFilter)
        },
        onClickOptionValue = {
            component.obtainEvent(ShopFilterStore.Intent.OnClickOption(it))
            component.obtainEvent(ShopFilterStore.Intent.OnClickApplyFilter)
        }
    )
}