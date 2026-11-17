package com.markettwits.sportsouce.shop.filter.presentation.components.filter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.SauceActionFooter
import com.markettwits.core_ui.items.components.progress.CircularProgressIndicatorBase
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.sportsouce.shop.filter.domain.models.ShopOptionInfo
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold

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
        containerColor = SauceTheme.colors.surface,
        topBar = { ShopFilterTopBar(onClickBack = onClickGoBack) },
        bottomBar = {
            if (!state.isLoading && state.error == null) SauceActionFooter {
                ShopFilterApplyButton(
                    onClickApply = onClickApplyFilter,
                    onClickReset = onClickReset
                )
            }
        },
    ) { paddingValues ->
        val contentModifier = Modifier.padding(paddingValues).consumeWindowInsets(paddingValues)
        val error = state.error
        when {
            state.isLoading -> Box(contentModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicatorBase(Modifier.semantics { contentDescription = "Загрузка фильтров" })
            }
            error != null -> error.SauceErrorScreen(
                modifier = contentModifier,
                onClickRetry = onClickRetry,
            )
            else -> Column(contentModifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
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
                    onOptionClick = onOptionClick,
                )
            }
        }
    }
}
