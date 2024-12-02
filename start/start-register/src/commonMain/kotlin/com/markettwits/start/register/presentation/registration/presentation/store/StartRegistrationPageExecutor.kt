package com.markettwits.start.register.presentation.registration.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import com.markettwits.start.register.presentation.registration.presentation.components.registration.createStartStagesContent
import com.markettwits.start.register.presentation.registration.presentation.components.registration.getPayStage
import com.markettwits.start.register.presentation.registration.presentation.components.registration.getRegistrationsStage
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.Action
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.Intent
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.Label
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.Message
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.State
import kotlinx.coroutines.launch

class StartRegistrationPageExecutor : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {

            is Intent.OnClickGoBack -> publish(Label.GoBack)

            is Intent.UpdateStagePage -> {
                val newPages = replaceStagePage(getState().stages,intent.stagePage)
                dispatch(Message.UpdateStages(newPages))
            }
            is Intent.OnPageSelected -> {
                publish(Label.OnPageSelected(getState().stages,intent.pageIndex))
            }
            is Intent.OnConsumedEvent -> {
                dispatch(Message.UpdateEvent(consumed()))
            }
            is Intent.OnSendEvent -> {
                dispatch(Message.UpdateEvent(triggered(intent.eventContent)))
            }

            is Intent.OnApplyPromo -> {
                dispatch(Message.UpdatePromo(intent.promo))
            }
        }
    }

    override fun executeAction(action: Action, getState: () -> State) {
       when(action){
           is Action.Loaded -> {
               val stages = action.distances.createStartStagesContent(
                   getState().registrationInfo
               )
               dispatch(Message.UpdateStages(stages))
               publish(Label.OnPageSelected(stages,0))
           }
           is Action.Failed -> {
               dispatch(Message.UpdateStageError(action.error))
           }
           is Action.Loading -> {
               dispatch(Message.UpdateStageLoading)
           }
       }
    }


    private fun replaceStagePage(
        list: List<StartRegistrationStagePage>,
        updatedPage: StartRegistrationStagePage
    ): List<StartRegistrationStagePage> {
        return list.map { page ->
            if (page.title == updatedPage.title) updatedPage else page
        }
    }

}
