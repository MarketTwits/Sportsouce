package com.markettwits.root_profile

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.profile.api.root.RootAuthFlowComponent
import com.markettwits.profile.root.RootAuthorizedProfileComponent
import com.markettwits.unauthorized.presentation.component.UnAuthorizedProfileComponent
import kotlinx.serialization.Serializable

interface RootProfileComponent {
    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed class Config {
        @Serializable
        data object AuthProfile : Config()

        @Serializable
        data object UnAuthProfile : Config()

        @Serializable
        data object AuthFlow : Config()
    }


    sealed class Child {
        data class AuthProfile(val component: RootAuthorizedProfileComponent) : Child()
        data class UnAuthProfile(val component: UnAuthorizedProfileComponent) : Child()
        data class AuthFlow(val component: RootAuthFlowComponent) : Child()
    }
}