package com.markettwits.shop.catalog.presentation.store

import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Intent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Label
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Message
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.State
import com.markettwits.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.shop.filter.domain.models.mapToStringOptions
import com.markettwits.shop.filter.presentation.store.ShopFilterStore

internal class ShopCatalogExecutor(private val repository: ShopCatalogRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickItem -> publish(Label.OnClickItem(intent.item))
            is Intent.OnClickFilter -> publish(Label.GoFilter)
            is Intent.OnClickSearch -> publish(Label.GoSearch(getState().queryState))
            is Intent.ApplyQuery -> launchWithQuery(intent.query,getState().filterState)
            is Intent.ApplyFilter -> {
                val categoryId =
                    if (intent.state.selectedCategoryPath.isNotEmpty())
                        intent.state.selectedCategoryPath.last().id
                    else null
                launchWithFilter(
                    categoryId = categoryId,
                    options = intent.state.selectedOptionUID.mapToStringOptions(),
                    price = intent.state.selectedPrice
                )
                dispatch(Message.FilterApplied(intent.state))
            }
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launchWithFilter(null, emptyList(), ShopFilterPrice.EMPTY)
    }

    private fun launchWithFilter(
        categoryId: Int?,
        options: List<String>,
        price: ShopFilterPrice,
    ) {
        dispatch(
            Message.Loaded(
                repository.paddingProducts(
                    categoryId = categoryId,
                    options = options.ifEmpty { null },
                    price = price
                ).cachedIn(scope)
            )
        )
        dispatch(Message.QueryApplied(""))
    }

    private fun launchWithQuery(
        query: String,
        filterState : ShopFilterStore.State?
    ) {
        dispatch(Message.QueryApplied(query))
        dispatch(Message.Loaded(repository.paddingProducts(query).cachedIn(scope)))
        filterState?.let {
            dispatch(Message.FilterApplied(clearFilterStateChosen(filterState)))
        }

    }

    private fun clearFilterStateChosen(filterState: ShopFilterStore.State) :ShopFilterStore.State{
        return filterState.copy(
            selectedPrice = ShopFilterPrice.EMPTY,
            selectedOptionUID = emptyList(),
            options = emptyList(),
            selectedCategoryPath = emptyList(),
            isApplied = false
        )
    }
}
