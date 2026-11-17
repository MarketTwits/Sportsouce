package com.markettwits.sportsouce.shop.item.presentation.store

import app.cash.paging.cachedIn
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.sportsouce.shop.item.domain.ShopItemRepository
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class ShopItemPageExecutor(
    private val repository: ShopItemRepository,
    private val intentAction: IntentAction,
    private val productId: String,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var requestedItemId = productId
    private var loadJob: Job? = null

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickOption -> launch(intent.itemId)
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickRetry -> launch(requestedItemId)
            is Intent.OnClickAddToFavorite -> TODO()
            is Intent.OnClickShare -> state().shopItem?.let {
                intentAction.sharePlainText(it.fullPathUrl)
            }
            is Intent.OnClickProduct -> publish(Label.PushProduct(intent.item))
        }
    }

    override fun executeAction(action: Unit) {
        val item = state().shopItem
        if (item != null){
            Message.Loaded(item = item, emptyList())
        }
        launch(productId)
        loadSimilarProducts(item?.categories?.firstOrNull()?.id)
    }

    private fun launch(itemId: String) {
        loadJob?.cancel()
        requestedItemId = itemId
        loadJob = scope.launch {
            dispatch(Message.Loading)
            val result = repository.item(itemId)
            ensureActive()
            result.fold(
                onSuccess = {
                    dispatch(Message.Loaded(item = it.first, shopItemOptions = it.second))
                    publish(Label.UpdateItem(it.first))
                    loadSimilarProducts(it.first.categories.firstOrNull()?.id)
                }, onFailure = {
                    dispatch(Message.Failed(it.mapToSauceError()))
                }
            )
        }
    }

    private fun loadSimilarProducts(categoryId: Int?) {
        dispatch(
            Message.SimilarProductsLoaded(
                repository.similarProducts(categoryId).cachedIn(scope)
            )
        )
    }
}
