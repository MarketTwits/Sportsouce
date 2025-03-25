package com.markettwits.sportsouce.auth.flow.api.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.component.ForgotPasswordComponent
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component.SignInScreenComponent
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.component.SignUpComponent
import kotlinx.serialization.Serializable

interface RootAuthFlowComponent {

    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed interface Config {
        @Serializable
        data object SignIn : Config

        @Serializable
        data object SignUp : Config

        @Serializable
        data object ForgotPassword : Config
    }

    sealed interface Child {

        data class SignIn(val component: SignInScreenComponent) : Child

        data class SignUp(val component: SignUpComponent) : Child

        data class ForgotPassword(val component: ForgotPasswordComponent) : Child
    }
}