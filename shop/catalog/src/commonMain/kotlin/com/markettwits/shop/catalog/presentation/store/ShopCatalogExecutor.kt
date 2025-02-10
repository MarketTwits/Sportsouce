package com.markettwits.shop.catalog.presentation.store

import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.*

internal class ShopCatalogExecutor(private val repository: ShopCatalogRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickItem -> publish(Label.OnClickItem(intent.item))
            is Intent.OnClickFilter -> publish(Label.GoFilter)
            is Intent.OnClickSearch -> publish(Label.GoSearch(state().queryState))
            is Intent.ApplyQuery -> launchWithQuery(intent.query)
            is Intent.ApplyFilter -> {
                launchWithFilter(
                    categoryId = intent.state.categoryId,
                    options = intent.state.options,
                    maxPrice = intent.state.maxPrice,
                    minPrice = intent.state.minPrice
                )
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
        minPrice : Int?
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

}
