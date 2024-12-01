package com.markettwits.start.register.presentation.registration.presentation.component

import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface StartStageComponent {

    val state: StateFlow<StartRegistrationStagePage>

    data object Empty : StartStageComponent {
        override val state: StateFlow<StartRegistrationStagePage> = MutableStateFlow(
            StartRegistrationStagePage.Empty)
    }
}