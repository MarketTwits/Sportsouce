package com.markettwits.sportsouce.start.register.presentation.registration.registration.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationInfo
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Intent
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Label
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.State

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
        data object OnConsumedEvent : Intent
        data class OnSendEvent(val eventContent: EventContent) : Intent
        data class OnPageSelected(val pageIndex : Int) : Intent
        data object OnClickGoBack : Intent
        data class UpdateStagePage(val stagePage: StartRegistrationStagePage) : Intent
    }

    sealed interface Message{
        data class UpdateStages(val stagePage: List<StartRegistrationStagePage>) : Message
        data class UpdateEvent(val eventContent: StateEventWithContent<EventContent>) : Message
        data object UpdateStageLoading : Message
        data class UpdateStageError(val error: SauceError) : Message
    }

    sealed interface Label {
        data object GoAuth : Label
        data object GoBack : Label
        data class OnPageSelected(val items : List<StartRegistrationStagePage>, val pageIndex : Int) : Label
    }

    sealed interface Action {
        data object Loading : Action
        data class Failed(val error : SauceError) : Action
        data class Loaded(val distances : List<StartRegistrationDistance>) : Action
        data object Unauthorized : Action
    }

}
