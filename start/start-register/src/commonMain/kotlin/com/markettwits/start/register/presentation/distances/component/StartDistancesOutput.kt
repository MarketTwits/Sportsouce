package com.markettwits.start.register.presentation.distances.component

import com.markettwits.start.register.presentation.registration.registration.component.StartRegistrationInput

interface StartDistancesOutput {

    fun onClickGoBack()

    fun onClickDistance(distinctDistance: StartRegistrationInput)
}