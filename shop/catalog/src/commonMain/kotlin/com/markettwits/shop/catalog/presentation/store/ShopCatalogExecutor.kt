package com.markettwits.shop.catalog.presentation.store

import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Intent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Label
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Message
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.State
import com.markettwits.shop.filter.domain.models.ShopFilterPrice
import kotlinx.coroutines.launch

class ShopCatalogExecutor(private val repository: ShopCatalogRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickItem -> publish(Label.OnClickItem(intent.id))
            is Intent.OnClickFilter -> publish(Label.GoFilter(getState().filterState))
            is Intent.OnClickCategory -> {
                dispatch(Message.UpdateCategories(intent.categoryId))
                //  launch(intent.categoryId)
            }

            is Intent.ApplyFilter -> {
                val categoryId =
                    if (intent.state.currentCategoryPath.isNotEmpty())
                        intent.state.currentCategoryPath.last().id
                    else null
                launch(categoryId, intent.state.selectedOptionUID, intent.state.selectedPrice)
                dispatch(Message.FilterApplied(intent.state))
            }
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch(null, emptySet(), ShopFilterPrice.EMPTY)
    }

    private fun launchCategories() {
        scope.launch {
            repository.categories().fold(onSuccess = {
                dispatch(Message.CategoriesLoaded(it))
            }, onFailure = {
                dispatch(Message.Failed(it.message.toString()))
            })
        }
    }

    private fun launch(
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


}
