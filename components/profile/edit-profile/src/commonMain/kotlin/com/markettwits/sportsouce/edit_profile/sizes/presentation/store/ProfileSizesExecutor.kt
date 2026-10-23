package com.markettwits.sportsouce.edit_profile.sizes.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.edit_profile.sizes.domain.ProfileSizesRepository
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore.*
import kotlinx.coroutines.launch

class ProfileSizesExecutor(private val repository: ProfileSizesRepository) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeAction(action: Unit) = load()

    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.GoBack -> if (!state().isSaving) {
                if (state().hasChanges) dispatch(Message.ConfirmDiscard(true)) else publish(Label.GoBack)
            }
            Intent.KeepEditing -> dispatch(Message.ConfirmDiscard(false))
            Intent.Discard -> { dispatch(Message.ConfirmDiscard(false)); publish(Label.GoBack) }
            Intent.ConsumeEvent -> dispatch(Message.ConsumeEvent)
            Intent.Retry -> if (!state().isLoading && state().data == null) load()
            Intent.Reset -> if (!state().isSaving) dispatch(Message.Reset)
            is Intent.Edit -> if (!state().isSaving) state().data?.let {
                val updated = it.updated(intent.field, intent.value)
                val errors = if (intent.field in state().errors) updated.errors().filterKeys { key -> key in state().errors }
                    else state().errors
                dispatch(Message.Edited(updated, errors))
            }
            Intent.Save -> save()
        }
    }

    private fun load() {
        dispatch(Message.Loading)
        scope.launch {
            repository.fetch().fold(onSuccess = { dispatch(Message.Loaded(it)) },
                onFailure = { dispatch(Message.LoadFailed(it.mapToSauceError().mapToString())) })
        }
    }

    private fun save() {
        val state = state()
        val data = state.data ?: return
        if (state.isLoading || state.isSaving || !state.hasChanges) return
        val errors = data.errors()
        if (errors.isNotEmpty()) { dispatch(Message.Invalid(errors)); return }
        dispatch(Message.Saving)
        scope.launch {
            repository.save(data.normalized()).fold(onSuccess = { dispatch(Message.Saved(it)) },
                onFailure = { dispatch(Message.SaveFailed(it.mapToSauceError().mapToString())) })
        }
    }
}
