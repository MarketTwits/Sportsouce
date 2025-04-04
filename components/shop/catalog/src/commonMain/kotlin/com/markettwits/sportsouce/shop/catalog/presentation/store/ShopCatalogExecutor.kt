package com.markettwits.sportsouce.shop.catalog.presentation.store

import app.cash.paging.cachedIn
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore.Intent
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore.Label
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore.Message
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore.State

internal class ShopCatalogExecutor(private val repository: ShopCatalogRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickItem -> publish(Label.OnClickItem(intent.item))
            is Intent.OnClickFilter -> onClickFilter()
            is Intent.OnClickSearch -> publish(Label.GoSearch(state().queryState))
            is Intent.ApplyQuery -> launchWithQuery(intent.query)
            is Intent.ApplyFilter -> {
                launchWithFilter(
                    categoryId = intent.state.categoryId,
                    options = intent.state.options,
                    maxPrice = intent.state.maxPrice,
                    minPrice = intent.state.minPrice
                )
                if (state().isShowFilter)
                    onClickFilter()
            }
        }
    }

    override fun executeAction(action: Unit) {
        launchWithFilter(null, emptyList(), null, null)
    }

    private fun launchWithFilter(
        categoryId: Int?,
        options: List<String>,
        maxPrice: Int?,
        minPrice: Int?
    ) {
        dispatch(
            Message.Loaded(
                repository.paddingProducts(
                    categoryId = categoryId,
                    options = options.ifEmpty { null },
                    maxPrice = maxPrice,
                    minPrice = minPrice
                ).cachedIn(scope)
            )
        )
        dispatch(Message.QueryApplied(""))
    }

    private fun launchWithQuery(
        query: String,
    ) {
        dispatch(Message.QueryApplied(query))
        dispatch(Message.Loaded(repository.paddingProducts(query).cachedIn(scope)))
    }

    private fun onClickFilter() {
        dispatch(Message.ChangeFilterVisibility(!state().isShowFilter))
    }

}
