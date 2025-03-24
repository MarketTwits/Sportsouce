package com.markettwits.start.root

import com.arkivanov.decompose.value.Value
import com.markettwits.profile.api.root.RootAuthFlowComponent
import com.markettwits.start.register.presentation.distances.component.StartDistancesComponent
import com.markettwits.start.register.presentation.distances.component.StartDistancesInput
import com.markettwits.start.register.presentation.registration.registration.component.StartRegistrationInput
import com.markettwits.start.register.presentation.registration.registration.component.StartRegistrationPageComponent
import com.markettwits.start.register.presentation.success.RegisterSuccessComponent
import kotlinx.serialization.Serializable

interface RootStartRegister {

    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, Child>>

    @Serializable
    sealed class Config {

        @Serializable
        data object StartRegistrationSuccess : Config()

        @Serializable
        data class StartRegistrationPage(val input: StartRegistrationInput) : Config()

        @Serializable
        data class StartRegistrationDistancesPage(val input: StartDistancesInput) : Config()

        @Serializable
        data object AuthFlow : Config()
    }

    sealed interface Child {

        data class StartRegistrationSuccess(val component: RegisterSuccessComponent) : Child

        data class StartRegistrationPage(val component: StartRegistrationPageComponent) : Child

        data class StartRegistrationDistances(val component: StartDistancesComponent) : Child

        data class AuthFlow(val component: RootAuthFlowComponent) : Child
    }

}