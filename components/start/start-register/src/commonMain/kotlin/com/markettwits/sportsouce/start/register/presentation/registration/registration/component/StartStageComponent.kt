package com.markettwits.sportsouce.start.register.presentation.registration.registration.component

import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage

interface StartStageComponent {

    val value : StartRegistrationStagePage

    data object Empty : StartStageComponent {
        override val value: StartRegistrationStagePage = StartRegistrationStagePage.Empty
    }
}