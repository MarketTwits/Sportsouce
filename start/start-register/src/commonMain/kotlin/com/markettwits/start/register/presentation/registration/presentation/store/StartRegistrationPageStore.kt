package com.markettwits.start.register.presentation.registration.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationInfo
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationDistance
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.Intent
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.Label
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.State

interface StartRegistrationPageStore : Store<Intent, State, Label> {

    data class State(
        val stages : List<StartRegistrationStagePage>,
        val eventWithContent: StateEventWithContent<EventContent> = consumed(),
        val registrationInfo : StartRegistrationInfo,
        val pagesState : PagesState = PagesState()
    )

    data class PagesState(
        val isLoading : Boolean = true,
        val error: SauceError? = null
    )

    sealed interface Intent{
        data class OnApplyPromo(val promo: String) : Intent
        data object OnConsumedEvent : Intent
        data class OnSendEvent(val eventContent: EventContent) : Intent
        data class OnPageSelected(val pageIndex : Int) : Intent
        data object OnClickGoBack : Intent
        data class UpdateStagePage(val stagePage: StartRegistrationStagePage) : Intent
    }

    sealed interface Message{
        data class UpdateStages(val stagePage: List<StartRegistrationStagePage>) : Message
        data class UpdateEvent(val eventContent: StateEventWithContent<EventContent>) : Message
        data class UpdatePromo(val promo : String) : Message
        data object UpdateStageLoading : Message
        data class UpdateStageError(val error: SauceError) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data class OnPageSelected(val items : List<StartRegistrationStagePage>, val pageIndex : Int) : Label
    }

    sealed interface Action {
        data object Loading : Action
        data class Failed(val error : SauceError) : Action
        data class Loaded(val distances : List<StartRegistrationDistance>) : Action
    }

}
