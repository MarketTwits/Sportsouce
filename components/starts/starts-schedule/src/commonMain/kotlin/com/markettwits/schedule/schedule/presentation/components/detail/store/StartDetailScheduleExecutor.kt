package com.markettwits.schedule.schedule.presentation.components.detail.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.Intent
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.Label
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.Message
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.State

class StartDetailScheduleExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickBack -> publish(Label.OnClickBack)
            is Intent.OnClickItem -> publish(Label.OnClickItem(intent.id))
        }
    }
}
