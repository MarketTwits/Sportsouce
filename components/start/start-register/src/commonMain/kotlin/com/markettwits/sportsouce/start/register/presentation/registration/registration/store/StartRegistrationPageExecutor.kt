package com.markettwits.sportsouce.start.register.presentation.registration.registration.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.createStartStagesContent
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Action
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Intent
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Label
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Message
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.State

class StartRegistrationPageExecutor : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {

            is Intent.OnClickGoBack -> publish(Label.GoBack)

            is Intent.UpdateStagePage -> {
                val newPages = replaceStagePage(state().stages,intent.stagePage)
                dispatch(Message.UpdateStages(newPages))
            }
            is Intent.OnPageSelected -> {
                publish(Label.OnPageSelected(state().stages,intent.pageIndex))
            }
            is Intent.OnConsumedEvent -> {
                dispatch(Message.UpdateEvent(consumed()))
            }
            is Intent.OnSendEvent -> {
                dispatch(Message.UpdateEvent(triggered(intent.eventContent)))
            }
        }
    }

    override fun executeAction(action: Action) {
       when(action){
           is Action.Loaded -> {
               val stages = action.distances.createStartStagesContent(
                   state().registrationInfo
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
           is Action.Unauthorized -> publish(Label.GoAuth)
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
