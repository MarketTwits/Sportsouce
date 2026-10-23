package com.markettwits.sportsouce.edit_profile.sizes.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.items.event.*
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore.*

object ProfileSizesReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State = when (msg) {
        Message.Loading -> copy(isLoading = true, loadError = null)
        is Message.Loaded -> State(data = msg.data, saved = msg.data, isLoading = false)
        is Message.LoadFailed -> copy(isLoading = false, loadError = msg.message)
        is Message.Edited -> copy(data = msg.data, errors = msg.errors, event = consumed())
        is Message.Invalid -> copy(errors = msg.errors, validationAttempt = validationAttempt + 1)
        Message.Saving -> copy(isSaving = true, errors = emptyMap(), event = consumed())
        is Message.Saved -> copy(data = msg.data, saved = msg.data, isSaving = false,
            event = triggered(EventContent(true, "Размеры сохранены")))
        is Message.SaveFailed -> copy(isSaving = false, event = triggered(EventContent(false, msg.message)))
        is Message.ConfirmDiscard -> copy(confirmDiscard = msg.visible)
        Message.Reset -> copy(data = saved, errors = emptyMap(), event = consumed())
        Message.ConsumeEvent -> copy(event = consumed())
    }
}
