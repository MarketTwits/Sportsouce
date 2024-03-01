package com.markettwits.start.presentation.order.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.common.StateEventWithContentTest
import com.markettwits.start.presentation.common.consumed
import com.markettwits.start.presentation.order.domain.OrderStatement
import kotlinx.serialization.Serializable

interface OrderStore : Store<OrderStore.Intent, OrderStore.State, OrderStore.Label> {
    @Serializable
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val orderStatement: OrderStatement? = null,
        val button: Button = Button(),
        val event: StateEventWithContentTest = consumed(),
    ) {
        @Serializable
        data class Button(
            val isEnabled: Boolean = false,
            val isLoading: Boolean = false
        )
    }

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
        data class ChangePaymentType(val payNow: Boolean) : Intent
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
