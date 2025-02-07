package com.markettwits.shop.search.presentation.store


import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.search.presentation.store.ShopSearchStore.Intent
import com.markettwits.shop.search.presentation.store.ShopSearchStore.Label
import com.markettwits.shop.search.presentation.store.ShopSearchStore.Message
import com.markettwits.shop.search.presentation.store.ShopSearchStore.State


class ShopSearchExecutor() : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnClickHistoryItem -> {}
            is Intent.OnApplyQuery -> publish(Label.OnApplyQuery(state().query))
            is Intent.OnClearQuery -> dispatch(Message.QueryWasUpdated(""))
            is Intent.OnUpdateQuery -> dispatch(Message.QueryWasUpdated(intent.query))
        }
    }

}
