package com.markettwits.sportsouce.shop.filter.presentation.store

import com.markettwits.sportsouce.shop.filter.domain.ShopFilterRepository
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore.Intent

class ShopFilterExecutor(
    shopFilterRepository: ShopFilterRepository
) : ShopFilterExecutorHandler(shopFilterRepository) {


    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickApplyFilter -> {
                val state = state()
                onClickApplyFilter(state)
            }
            is Intent.OnClickGoBack -> onClickGoBack()

            is Intent.OnClickResetFilter -> onClickResetFilter()

            is Intent.OnClickOption -> onClickUpdateOption(
                option = intent.option,
                options = state().selectedOptionUID
            )

            is Intent.OnUpdatePrice -> onClickUpdatePrice(
                isMin = intent.isMin,
                value = intent.value,
                previousPrice = state().selectedPrice
            )

            is Intent.OnClickCategory -> {
                onClickCategory(state().selectedCategoryPath, intent.category)
            }

            is Intent.OnClickResetCategory -> onClickResetCategory()

            is Intent.OnClickRetry -> launch()
        }
    }

    override fun executeAction(action: Unit) {
        if (state().categories.isEmpty())
            launch()
    }

}
