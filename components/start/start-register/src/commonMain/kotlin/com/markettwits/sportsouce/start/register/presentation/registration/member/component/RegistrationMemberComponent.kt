package com.markettwits.sportsouce.start.register.presentation.registration.member.component

import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore
import kotlinx.coroutines.flow.StateFlow

interface RegistrationMemberComponent {

    val model: StateFlow<RegistrationMemberStore.State>

    fun obtainEvent(event: RegistrationMemberStore.Intent)
}
