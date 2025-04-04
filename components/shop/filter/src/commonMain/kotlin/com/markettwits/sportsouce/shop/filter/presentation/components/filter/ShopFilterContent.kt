package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.sportsouce.shop.filter.domain.models.ShopOptionInfo
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore

@Composable
internal fun ShopFilterContent(
    modifier: Modifier = Modifier,
    state: ShopFilterStore.State,
    onClickGoBack: () -> Unit,
    onClickRetry: () -> Unit,
    onClickReset: () -> Unit,
    onMaxPriceChange: (String) -> Unit,
    onMinPriceChange: (String) -> Unit,
    onOptionClick: (ShopOptionInfo.Value) -> Unit,
    onClickCategory: (ShopCategoryItem?) -> Unit,
    onClickApplyFilter: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.primary,
        floatingActionButton = {
            if (!state.isError)
                ShopFilterApplyButton(
                    onClickApply = onClickApplyFilter,
                    onClickReset = onClickReset
                )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 40.dp
                )
        ) {
            ShopFilterCategories(
                categories = state.categories,
                currentPath = state.selectedCategoryPath,
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
    ShopFilterError(
        isError = state.isError,
        message = state.message,
        onClickRetry = onClickRetry
    )
    ShopFilterLoading(state.isLoading)
}
