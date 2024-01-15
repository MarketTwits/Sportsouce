package com.markettwits.start.presentation.registration

import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.registration.store.StartRegistrationStore
import kotlinx.coroutines.flow.StateFlow

interface StartRegistrationComponent {
    fun obtainEvent(event: StartRegistrationStore.Intent)
    val value: StateFlow<StartRegistrationStore.State>
}