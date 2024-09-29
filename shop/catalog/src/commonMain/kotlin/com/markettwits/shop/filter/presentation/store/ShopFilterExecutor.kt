package com.markettwits.shop.filter.presentation.store

import com.markettwits.shop.filter.domain.ShopFilterRepository
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Intent
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.State

class ShopFilterExecutor(
    private val shopFilterRepository: ShopFilterRepository,
) : ShopFilterExecutorHandler(shopFilterRepository) {


    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickApplyFilter -> onClickApplyFilter(getState())
            is Intent.OnClickGoBack -> onClickGoBack()
            is Intent.OnClickResetFilter -> onClickResetFilter()
            is Intent.OnClickOption -> onClickUpdateOption(
                uuid = intent.optionId,
                options = getState().selectedOptionUID
            )

            is Intent.OnUpdatePrice -> onClickUpdatePrice(
                isMin = intent.isMin,
                value = intent.value,
                previousPrice = getState().selectedPrice
            )

            is Intent.OnClickCategory -> {
                onClickCategory(getState().currentCategoryPath, intent.category)
            }
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        if (getState().categories.isEmpty())
            launch()
    }

}
