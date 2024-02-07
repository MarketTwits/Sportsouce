package com.markettwits.start.presentation.order.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement
import com.markettwits.start.presentation.order.store.OrderStore.Intent
import com.markettwits.start.presentation.order.store.OrderStore.Label
import com.markettwits.start.presentation.order.store.OrderStore.State
import kotlinx.serialization.Serializable

interface OrderStore : Store<Intent, State, Label> {
    @Serializable
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val orderStatement: OrderStatement? = null,
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Action {
        data object Loading : Action
        data class InfoLoaded(val orderStatement: OrderStatement) : Action
        data class InfoFailed(val message: String) : Action
    }

    sealed interface Intent {
        data class OnClickPromo(val promo: String) : Intent
        data class OnClickMember(val member: StartStatement, val id: Int) : Intent
        data object OnConsumedEvent : Intent
        data object OnClickCheckedRules : Intent
        data object OnClickRegistry : Intent
        data object ChangePaymentType : Intent
        data class UpdateMember(val member: StartStatement, val id: Int) : Intent
        data class ApplyPromo(val promo: String, val percent: Int) : Intent
        data object GoBack : Intent
    }

    sealed interface Message {
        data class UpdateState(val state: State) : Message
        data object PreloadLoading : Message
        data class PreloadSuccess(val startStatement: OrderStatement) : Message
        data class PreloadFailed(val message: String) : Message
    }

    sealed interface Label {
        data class OnClickMember(val member: StartStatement, val memberId: Int) : Label
        data object GoBack : Label
        data class OnClickPromo(val promo: String) : Label
    }

}
