package com.markettwits.shop.item.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.shop.item.domain.ShopItemRepository
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.Intent
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.Label
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.Message
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.State
import kotlinx.coroutines.launch

class ShopItemPageExecutor(
    private val repository: ShopItemRepository,
    private val intentAction: IntentAction,
    private val productId: String,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnClickOption -> launch(intent.itemId)
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickRetry -> launch(getState().item?.product?.id)
            is Intent.OnClickAddToFavorite -> TODO()
            is Intent.OnClickShare -> getState().item?.let {
                intentAction.sharePlainText(it.product.fullPathUrl)
            }
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        val item = getState().item
        if (item != null){
            Message.Loaded(item)
        }
        launch(productId)
    }

    private fun launch(itemId: String?) {
        scope.launch {
            dispatch(Message.Loading)
            repository.item(itemId ?: productId).fold(
                onSuccess = {
                    dispatch(Message.Loaded(it))
                    publish(Label.UpdateItem(it))
                }, onFailure = {
                    dispatch(Message.Failed(it.message.toString()))
                }
            )
        }
    }
}
