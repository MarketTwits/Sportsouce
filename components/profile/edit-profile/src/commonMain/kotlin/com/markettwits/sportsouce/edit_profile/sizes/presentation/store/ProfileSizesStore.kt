package com.markettwits.sportsouce.edit_profile.sizes.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.*
import com.markettwits.sportsouce.edit_profile.sizes.domain.ProfileSizeField
import com.markettwits.sportsouce.edit_profile.sizes.domain.ProfileSizes
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore.*

interface ProfileSizesStore : Store<Intent, State, Label> {
    data class State(
        val data: ProfileSizes? = null,
        val saved: ProfileSizes? = null,
        val isLoading: Boolean = true,
        val isSaving: Boolean = false,
        val loadError: String? = null,
        val errors: Map<ProfileSizeField, String> = emptyMap(),
        val validationAttempt: Int = 0,
        val confirmDiscard: Boolean = false,
        val event: StateEventWithContent<EventContent> = consumed(),
    ) {
        val hasChanges get() = data != null && data != saved
    }

    sealed interface Intent {
        data object Retry : Intent
        data object Save : Intent
        data object Reset : Intent
        data object GoBack : Intent
        data object Discard : Intent
        data object KeepEditing : Intent
        data object ConsumeEvent : Intent
        data class Edit(val field: ProfileSizeField, val value: String) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data object Saving : Message
        data class Loaded(val data: ProfileSizes) : Message
        data class LoadFailed(val message: String) : Message
        data class Edited(val data: ProfileSizes, val errors: Map<ProfileSizeField, String>) : Message
        data class Invalid(val errors: Map<ProfileSizeField, String>) : Message
        data class Saved(val data: ProfileSizes) : Message
        data class SaveFailed(val message: String) : Message
        data class ConfirmDiscard(val visible: Boolean) : Message
        data object Reset : Message
        data object ConsumeEvent : Message
    }

    sealed interface Label { data object GoBack : Label }
}
