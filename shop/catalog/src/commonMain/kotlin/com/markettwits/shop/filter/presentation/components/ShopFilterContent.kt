package com.markettwits.shop.filter.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.top_bar.TopBarBase
import com.markettwits.shop.catalog.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.presentation.store.ShopFilterStore

@Composable
internal fun ShopFilterContent(
    modifier: Modifier = Modifier,
    state: ShopFilterStore.State,
    onClickGoBack: () -> Unit,
    onMaxPriceChange: (Int) -> Unit,
    onMinPriceChange: (Int) -> Unit,
    onOptionClick: (String) -> Unit,
    onClickCategory: (ShopCategoryItem?) -> Unit,
    onClickApplyFilter: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBarBase(title = "Фильтр", goBack = onClickGoBack)
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ShopFilterApplyButton(onClick = onClickApplyFilter)
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ShopFilterCategories(
                categories = state.categories,
                currentPath = state.currentCategoryPath,
                onClickCategory = onClickCategory,
            )
            Spacer(modifier = Modifier.height(16.dp))
            ShopFilterPriceSlider(
                shopFilterPrice = state.selectedPrice,
                onMaxPriceChange = onMaxPriceChange,
                onMinPriceChange = onMinPriceChange,
            )
            Spacer(modifier = Modifier.height(16.dp))
            ShopFilterOptions(
                options = state.options,
                selectedOptions = state.selectedOptionUID,
                onOptionClick = onOptionClick
            )
        }
    }
    ShopFilterLoading(state.isLoading)
}
