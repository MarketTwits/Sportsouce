package com.markettwits.shop.catalog.presentation.store

import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Intent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Label
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Message
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.State
import com.markettwits.shop.filter.domain.models.ShopFilterPrice

internal class ShopCatalogExecutor(private val repository: ShopCatalogRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickItem -> publish(Label.OnClickItem(intent.item))
            is Intent.OnClickFilter -> publish(Label.GoFilter(getState().filterState))
            is Intent.OnClickSearch -> publish(Label.GoSearch(getState().queryState))
            is Intent.OnClickCategory -> publish(Label.OnClickCategory(intent.categoryItem))
            is Intent.ApplyQuery -> launchWithQuery(intent.query)
            is Intent.ApplyFilter -> {
                val categoryId =
                    if (intent.state.currentCategoryPath.isNotEmpty())
                        intent.state.currentCategoryPath.last().id
                    else null
                launchWithFilter(
                    categoryId = categoryId,
                    options = intent.state.selectedOptionUID,
                    price = intent.state.selectedPrice
                )
                dispatch(Message.FilterApplied(intent.state))
            }
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launchWithFilter(null, emptySet(), ShopFilterPrice.EMPTY)
    }

    private fun launchWithFilter(
        categoryId: Int?,
        options: Set<String>,
        price: ShopFilterPrice,
    ) {
        dispatch(
            Message.Loaded(
                repository.paddingProducts(
                    categoryId,
                    options,
                    price
                ).cachedIn(scope)
            )
        )
    }

    private fun launchWithQuery(
        query: String
    ) {
        dispatch(Message.QueryApplied(query))
        dispatch(Message.Loaded(repository.paddingProducts(query).cachedIn(scope)))
    }
}
